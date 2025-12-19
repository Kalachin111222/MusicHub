/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import listasDinamicas.*;
import Datos.DALHistorialReproduccion;
import entidades.HistorialReproduccion;
import entidades.Cancion;
import javax.swing.JOptionPane;
import java.util.List;

/**
 */
public class BLLHistorialReproduccion {
    
    
    public static boolean registrarReproduccion(int usuarioId, int cancionId) {
        // 1. Verificar usuario logueado
        if (!CLLUsuario.getInstancia().hayUsuarioLogueado()) {
            System.out.println("Error: No hay usuario logueado.");
            return false;
        }
        
        // 2. Validar IDs
        if (usuarioId <= 0 || cancionId <= 0) {
            System.out.println("Error: IDs inválidos.");
            return false;
        }
        
        // 3. Registrar en BD
        String mensaje = DALHistorialReproduccion.registrarReproduccion(usuarioId, cancionId);
        
        if (mensaje != null) {
            System.out.println("Error al registrar reproducción: " + mensaje);
            return false;
        }
        
        return true;
    }
        
    public static List<HistorialReproduccion> listarHistorialUsuario(int usuarioId) {
        // 1. Verificar usuario logueado
        if (!CLLUsuario.getInstancia().hayUsuarioLogueado()) {
            mostrarError("Debe iniciar sesión para ver el historial.");
            return null;
        }
        
        // 2. Validar que el usuario actual sea el mismo
        if (CLLUsuario.getInstancia().getUsuario().getId() != usuarioId) {
            mostrarError("No tiene permisos para ver este historial.");
            return null;
        }
        
        // 3. Obtener historial de BD
        List<HistorialReproduccion> historial = DALHistorialReproduccion.listarHistorialPorUsuario(usuarioId);
        
        if (historial == null || historial.isEmpty()) {
            mostrarAdvertencia("No hay historial de reproducciones.");
            return historial;
        }
        
        // 4. Guardar en CLL
        CLLHistorialReproduccion.getInstancia().setListaHistorial(historial);
        
        return historial;
    }
        
    public static List<HistorialReproduccion> listarUltimasReproducciones(int usuarioId, int limite) {
        // 1. Verificar usuario logueado
        if (!CLLUsuario.getInstancia().hayUsuarioLogueado()) {
            mostrarError("Debe iniciar sesión.");
            return null;
        }
        
        // 2. Validar límite
        if (!validarLimite(limite)) {
            return null;
        }
        
        // 3. Validar que el usuario actual sea el mismo
        if (CLLUsuario.getInstancia().getUsuario().getId() != usuarioId) {
            mostrarError("No tiene permisos para ver este historial.");
            return null;
        }
        
        // 4. Obtener últimas reproducciones de BD
        List<HistorialReproduccion> historial = DALHistorialReproduccion.listarUltimasReproducciones(usuarioId, limite);
        
        if (historial == null || historial.isEmpty()) {
            mostrarAdvertencia("No hay reproducciones recientes.");
            return historial;
        }
        
        // 5. Guardar en CLL
        CLLHistorialReproduccion.getInstancia().setListaHistorial(historial);
        
        return historial;
    }
        
    public static String obtenerGeneroFavorito(int usuarioId) {
        // 1. Verificar usuario logueado
        if (!CLLUsuario.getInstancia().hayUsuarioLogueado()) {
            mostrarError("Debe iniciar sesión.");
            return null;
        }
        
        // 2. Validar que el usuario actual sea el mismo
        if (CLLUsuario.getInstancia().getUsuario().getId() != usuarioId) {
            mostrarError("No tiene permisos para ver esta información.");
            return null;
        }
        
        // 3. Obtener género favorito de BD
        String generoFavorito = DALHistorialReproduccion.obtenerGeneroFavorito(usuarioId);
        
        if (generoFavorito == null || generoFavorito.trim().isEmpty()) {
            mostrarAdvertencia("No se pudo determinar el género favorito. Escucha más música.");
            return null;
        }
        
        return generoFavorito;
    }
        
    public static int contarReproducciones(int usuarioId) {
        List<HistorialReproduccion> historial = DALHistorialReproduccion.listarHistorialPorUsuario(usuarioId);
        
        if (historial != null) {
            return historial.size();
        }
        
        return 0;
    }
    
    public static List<Cancion> obtenerCancionesMasEscuchadas(int usuarioId, int limite) {
        // Esta funcionalidad requeriría un método adicional en DAL
        // Por ahora, retornamos las últimas reproducciones
        List<HistorialReproduccion> historial = listarUltimasReproducciones(usuarioId, limite);
        
        if (historial == null || historial.isEmpty()) {
            return null;
        }
        
        // Extraer las canciones del historial
        List<Cancion> canciones = new java.util.ArrayList<>();
        for (HistorialReproduccion h : historial) {
            if (h.getCancion() != null) {
                canciones.add(h.getCancion());
            }
        }
        
        return canciones;
    }
        
    private static boolean validarLimite(int limite) {
        if (limite <= 0) {
            mostrarError("El límite debe ser mayor a 0.");
            return false;
        }
        
        if (limite > 100) {
            mostrarError("El límite no puede exceder 100 registros.");
            return false;
        }
        
        return true;
    }
        
    public static void limpiarHistorialTemporal() {
        CLLHistorialReproduccion.getInstancia().limpiarLista();
        CLLHistorialReproduccion.getInstancia().limpiarRecientes();
    }
    
    public static List<HistorialReproduccion> obtenerHistorialActual() {
        return CLLHistorialReproduccion.getInstancia().getListaHistorial();
    }
    
    public static List<Cancion> obtenerCancionesRecientes() {
        return CLLHistorialReproduccion.getInstancia().getCancionesRecientes();
    }
    
    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
            null,
            mensaje,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    private static void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
            null,
            mensaje,
            "Advertencia",
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
