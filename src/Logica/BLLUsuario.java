/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import listasDinamicas.CLLUsuario;
import Datos.DALUsuario;
import entidades.Usuario;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 */
public class BLLUsuario {
    
    
    public static boolean procesarRegistro(String nombre, String email, String password) {
        // 1. Validar datos
        if (!validarDatosRegistro(nombre, email, password, password)) {
            return false;
        }
        
        // 2. Verificar si el email ya existe
        if (DALUsuario.existeEmail(email.trim())) {
            mostrarError("El email ya está registrado.");
            return false;
        }
        
        // 3. Crear usuario
        Usuario nuevoUsuario = new Usuario(
            nombre.trim(),
            email.trim().toLowerCase(),
            password,
            LocalDate.now()
        );
        
        // 4. Insertar en BD
        String mensaje = DALUsuario.insertarUsuario(nuevoUsuario);
        
        if (mensaje != null) {
            mostrarError("Error al registrar: " + mensaje);
            return false;
        }
        
        // 5. Obtener el usuario registrado con su ID
        Usuario usuarioRegistrado = DALUsuario.obtenerUsuarioPorEmail(email.trim());
        if (usuarioRegistrado != null) {
            CLLUsuario.getInstancia().setUsuario(usuarioRegistrado);
        }
        
        mostrarExito("Usuario registrado exitosamente.");
        return true;
    }
    
    
    public static Usuario procesarLogin(String email, String password) {
        // 1. Validar datos
        if (!validarDatosLogin(email, password)) {
            return null;
        }
        
        // 2. Buscar usuario por email
        Usuario usuario = DALUsuario.obtenerUsuarioPorEmail(email.trim());
        
        if (usuario == null) {
            mostrarError("Email no registrado.");
            return null;
        }
        
        // 3. Verificar contraseña
        if (!usuario.getContraseña().equals(password)) {
            mostrarError("Contraseña incorrecta.");
            return null;
        }
        
        // 4. Establecer sesión
        CLLUsuario.getInstancia().setUsuario(usuario);
        
        return usuario;
    }
        
    public static boolean actualizarPerfil(String nombre, String email) {
        Usuario usuarioActual = CLLUsuario.getInstancia().getUsuario();
        
        if (usuarioActual == null) {
            mostrarError("No hay usuario en sesión.");
            return false;
        }
        
        // 1. Validar datos
        if (!validarNombre(nombre) || !validarEmail(email)) {
            return false;
        }
        
        // 2. Verificar si el email ya existe (excepto el actual)
        if (!email.trim().equalsIgnoreCase(usuarioActual.getEmail())) {
            if (DALUsuario.existeEmail(email.trim())) {
                mostrarError("El email ya está en uso.");
                return false;
            }
        }
        
        // 3. Actualizar datos
        usuarioActual.setNombre(nombre.trim());
        usuarioActual.setEmail(email.trim().toLowerCase());
        
        String mensaje = DALUsuario.actualizarUsuario(usuarioActual);
        
        if (mensaje != null) {
            mostrarError("Error al actualizar: " + mensaje);
            return false;
        }
        
        // 4. Actualizar en CLL
        CLLUsuario.getInstancia().setUsuario(usuarioActual);
        
        mostrarExito("Perfil actualizado exitosamente.");
        return true;
    }
        
    public static boolean cambiarContraseña(String contraseñaActual, String nuevaContraseña, 
                                           String confirmarNuevaContraseña) {
        Usuario usuarioActual = CLLUsuario.getInstancia().getUsuario();
        
        if (usuarioActual == null) {
            mostrarError("No hay usuario en sesión.");
            return false;
        }
        
        // 1. Verificar contraseña actual
        if (!usuarioActual.getContraseña().equals(contraseñaActual)) {
            mostrarError("La contraseña actual es incorrecta.");
            return false;
        }
        
        // 2. Validar nueva contraseña
        if (!validarContraseña(nuevaContraseña)) {
            return false;
        }
        
        // 3. Verificar confirmación
        if (!nuevaContraseña.equals(confirmarNuevaContraseña)) {
            mostrarError("Las contraseñas nuevas no coinciden.");
            return false;
        }
        
        // 4. Verificar que sea diferente
        if (contraseñaActual.equals(nuevaContraseña)) {
            mostrarError("La nueva contraseña debe ser diferente.");
            return false;
        }
        
        // 5. Actualizar contraseña
        usuarioActual.setContraseña(nuevaContraseña);
        
        String mensaje = DALUsuario.actualizarUsuario(usuarioActual);
        
        if (mensaje != null) {
            mostrarError("Error al cambiar contraseña: " + mensaje);
            return false;
        }
        
        mostrarExito("Contraseña cambiada exitosamente.");
        return true;
    }
        
    public static boolean eliminarCuenta() {
        Usuario usuarioActual = CLLUsuario.getInstancia().getUsuario();
        
        if (usuarioActual == null) {
            mostrarError("No hay usuario en sesión.");
            return false;
        }
        
        // Confirmar eliminación
        int confirmacion = JOptionPane.showConfirmDialog(
            null,
            "¿Está seguro de eliminar su cuenta?\nEsta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return false;
        }
        
        String mensaje = DALUsuario.eliminarUsuario(usuarioActual.getId());
        
        if (mensaje != null) {
            mostrarError("Error al eliminar cuenta: " + mensaje);
            return false;
        }
        
        // Cerrar sesión
        cerrarSesion();
        
        mostrarExito("Cuenta eliminada exitosamente.");
        return true;
    }
        
    private static boolean validarDatosRegistro(String nombre, String email, 
                                               String password, String confirmarPassword) {
        if (!validarNombre(nombre)) {
            return false;
        }
        
        if (!validarEmail(email)) {
            return false;
        }
        
        if (!validarContraseña(password)) {
            return false;
        }
        
        if (!password.equals(confirmarPassword)) {
            mostrarError("Las contraseñas no coinciden.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarDatosLogin(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            mostrarError("El email no puede estar vacío.");
            return false;
        }
        
        if (password == null || password.trim().isEmpty()) {
            mostrarError("La contraseña no puede estar vacía.");
            return false;
        }
        
        if (!validarFormatoEmail(email)) {
            mostrarError("El formato del email no es válido.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarError("El nombre no puede estar vacío.");
            return false;
        }
        
        if (nombre.trim().length() < 3) {
            mostrarError("El nombre debe tener al menos 3 caracteres.");
            return false;
        }
        
        if (nombre.trim().length() > 50) {
            mostrarError("El nombre no puede exceder 50 caracteres.");
            return false;
        }
        
        if (!nombre.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            mostrarError("El nombre solo puede contener letras y espacios.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            mostrarError("El email no puede estar vacío.");
            return false;
        }
        
        if (!validarFormatoEmail(email)) {
            mostrarError("El formato del email no es válido.");
            return false;
        }
        
        if (email.trim().length() > 100) {
            mostrarError("El email no puede exceder 100 caracteres.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarFormatoEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email.trim()).matches();
    }
    
    private static boolean validarContraseña(String password) {
        if (password == null || password.isEmpty()) {
            mostrarError("La contraseña no puede estar vacía.");
            return false;
        }
        
        if (password.length() < 6) {
            mostrarError("La contraseña debe tener al menos 6 caracteres.");
            return false;
        }
        
        if (password.length() > 20) {
            mostrarError("La contraseña no puede exceder 20 caracteres.");
            return false;
        }
        
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
            mostrarError("La contraseña debe contener al menos una letra y un número.");
            return false;
        }
        
        return true;
    }
        
    public static void cerrarSesion() {
        CLLUsuario.getInstancia().cerrarSesion();
    }
    
    public static Usuario getUsuarioActual() {
        return CLLUsuario.getInstancia().getUsuario();
    }
    
    public static boolean hayUsuarioLogueado() {
        return CLLUsuario.getInstancia().hayUsuarioLogueado();
    }
    
    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            null,
            mensaje,
            "Error de validación",
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    public static void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(
            null,
            mensaje,
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}