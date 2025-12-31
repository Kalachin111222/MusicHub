package logica;

import listasDinamicas.CLLUsuario;
import Datos.DALUsuario;
import entidades.Usuario;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class BLLUsuario {
    
    public static boolean procesarRegistro(String nombre, String email, String password, 
                                          String confirmarPassword, String fechaNacimiento, 
                                          String genero) {
        if (!validarDatosRegistro(nombre, email, password, confirmarPassword, 
                                  fechaNacimiento, genero)) {
            return false;
        }
        if (DALUsuario.existeEmail(email.trim())) {
            mostrarError("El email ya está registrado.");
            return false;
        }
        Usuario nuevoUsuario = new Usuario(
            nombre.trim(),
            email.trim().toLowerCase(),
            password,
            LocalDate.now(),
            fechaNacimiento.trim(),
            genero.trim()
        );
        String mensaje = DALUsuario.insertarUsuario(nuevoUsuario);
        
        if (mensaje != null) {
            mostrarError("Error al registrar: " + mensaje);
            return false;
        }
        Usuario usuarioRegistrado = DALUsuario.obtenerUsuarioPorEmail(email.trim());
        if (usuarioRegistrado != null) {
            CLLUsuario.getInstancia().setUsuario(usuarioRegistrado);
        }
        
        mostrarExito("Usuario registrado exitosamente.");
        return true;
    }
    
    public static Usuario procesarLogin(String email, String password) {
        if (!validarDatosLogin(email, password)) {
            return null;
        }
        Usuario usuario = DALUsuario.obtenerUsuarioPorEmail(email.trim());
        
        if (usuario == null) {
            mostrarError("Email no registrado.");
            return null;
        }
        if (!usuario.getContraseña().equals(password)) {
            mostrarError("Contraseña incorrecta.");
            return null;
        }
        CLLUsuario.getInstancia().setUsuario(usuario);
        
        return usuario;
    }
        
    public static boolean actualizarPerfil(String nombre, String email, 
                                          String fechaNacimiento, String genero) {
        Usuario usuarioActual = CLLUsuario.getInstancia().getUsuario();
        
        if (usuarioActual == null) {
            mostrarError("No hay usuario en sesión.");
            return false;
        }
        if (!validarNombre(nombre) || !validarEmail(email) || 
            !validarFechaNacimiento(fechaNacimiento) || !validarGenero(genero)) {
            return false;
        }
        if (!email.trim().equalsIgnoreCase(usuarioActual.getEmail())) {
            if (DALUsuario.existeEmail(email.trim())) {
                mostrarError("El email ya está en uso.");
                return false;
            }
        }
        usuarioActual.setNombre(nombre.trim());
        usuarioActual.setEmail(email.trim().toLowerCase());
        usuarioActual.setFechaDeNacimiento(fechaNacimiento.trim());
        usuarioActual.setGenero(genero.trim());
        
        String mensaje = DALUsuario.actualizarUsuario(usuarioActual);
        
        if (mensaje != null) {
            mostrarError("Error al actualizar: " + mensaje);
            return false;
        }
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
        if (!usuarioActual.getContraseña().equals(contraseñaActual)) {
            mostrarError("La contraseña actual es incorrecta.");
            return false;
        }
        if (!validarContraseña(nuevaContraseña)) {
            return false;
        }
        if (!nuevaContraseña.equals(confirmarNuevaContraseña)) {
            mostrarError("Las contraseñas nuevas no coinciden.");
            return false;
        }
        if (contraseñaActual.equals(nuevaContraseña)) {
            mostrarError("La nueva contraseña debe ser diferente.");
            return false;
        }
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
        cerrarSesion();
        
        mostrarExito("Cuenta eliminada exitosamente.");
        return true;
    }
        
    private static boolean validarDatosRegistro(String nombre, String email, 
                                               String password, String confirmarPassword,
                                               String fechaNacimiento, String genero) {
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
        
        if (!validarFechaNacimiento(fechaNacimiento)) {
            return false;
        }
        
        if (!validarGenero(genero)) {
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
    
    private static boolean validarFechaNacimiento(String fechaNacimiento) {
        if (fechaNacimiento == null || fechaNacimiento.trim().isEmpty()) {
            mostrarError("La fecha de nacimiento no puede estar vacía.");
            return false;
        }
        
        // Validar formato de fecha (yyyy-MM-dd)
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fecha = LocalDate.parse(fechaNacimiento.trim(), formatter);
            
            // Verificar que la fecha no sea futura
            if (fecha.isAfter(LocalDate.now())) {
                mostrarError("La fecha de nacimiento no puede ser futura.");
                return false;
            }
            
            // Verificar edad mínima (por ejemplo, 13 años)
            LocalDate fechaMinima = LocalDate.now().minusYears(13);
            if (fecha.isAfter(fechaMinima)) {
                mostrarError("Debes tener al menos 13 años para registrarte.");
                return false;
            }
            
            // Verificar edad máxima razonable (por ejemplo, 120 años)
            LocalDate fechaMaxima = LocalDate.now().minusYears(120);
            if (fecha.isBefore(fechaMaxima)) {
                mostrarError("La fecha de nacimiento no es válida.");
                return false;
            }
            
        } catch (DateTimeParseException e) {
            mostrarError("Formato de fecha inválido. Use yyyy-MM-dd (ej: 2000-01-15).");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            mostrarError("Debe seleccionar un género.");
            return false;
        }
        
        // Validar que sea uno de los valores permitidos
        String generoLower = genero.trim().toLowerCase();
        if (!generoLower.equals("masculino") && 
            !generoLower.equals("femenino") && 
            !generoLower.equals("otro") &&
            !generoLower.equals("prefiero no decir")) {
            mostrarError("El género seleccionado no es válido.");
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