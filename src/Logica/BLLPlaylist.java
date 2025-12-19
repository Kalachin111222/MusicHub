/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import listasDinamicas.*;
import Datos.DALPlaylist;
import entidades.Playlist;
import entidades.Cancion;
import entidades.Usuario;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.List;

/**
 */
public class BLLPlaylist {
        
    public static boolean crearNuevaPlaylist(String nombre, int usuarioId) {
        // 1. Validar nombre
        if (!validarNombrePlaylist(nombre)) {
            return false;
        }
        
        // 2. Verificar usuario
        Usuario usuario = CLLUsuario.getInstancia().getUsuario();
        if (usuario == null || usuario.getId() != usuarioId) {
            mostrarError("Debe iniciar sesión para crear playlists.");
            return false;
        }
        
        // 3. Crear playlist
        Playlist nuevaPlaylist = new Playlist();
        nuevaPlaylist.setNombre(nombre.trim());
        nuevaPlaylist.setUsuario(usuario);
        nuevaPlaylist.setFechaCreacion(LocalDate.now());
        
        // 4. Insertar en BD
        String mensaje = DALPlaylist.crearPlaylist(nuevaPlaylist);
        
        if (mensaje != null) {
            mostrarError("Error al crear playlist: " + mensaje);
            return false;
        }
        
        mostrarExito("Playlist creada exitosamente.");
        return true;
    }
        
    public static boolean agregarCancionPlaylist(int playlistId, Cancion cancion) {
        // 1. Validar canción
        if (cancion == null) {
            mostrarError("No hay canción seleccionada.");
            return false;
        }
        
        // 2. Verificar usuario logueado
        if (!CLLUsuario.getInstancia().hayUsuarioLogueado()) {
            mostrarError("Debe iniciar sesión.");
            return false;
        }
        
        // 3. Agregar a BD
        String mensaje = DALPlaylist.agregarCancionAPlaylist(playlistId, cancion.getId());
        
        if (mensaje != null) {
            if (mensaje.contains("ya está")) {
                mostrarAdvertencia(mensaje);
            } else {
                mostrarError("Error: " + mensaje);
            }
            return false;
        }
        
        mostrarExito("Canción agregada a la playlist.");
        return true;
    }
        
    public static boolean quitarCancionDePlaylist(int playlistId, int cancionId) {
        // 1. Confirmar acción
        int confirmacion = JOptionPane.showConfirmDialog(
            null,
            "¿Está seguro de quitar esta canción de la playlist?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion != JOptionPane.YES_OPTION) {
            return false;
        }
        
        // 2. Quitar de BD
        String mensaje = DALPlaylist.quitarCancionDePlaylist(playlistId, cancionId);
        
        if (mensaje != null) {
            mostrarError("Error: " + mensaje);
            return false;
        }
        
        mostrarExito("Canción quitada de la playlist.");
        return true;
    }
        
    public static List<Playlist> obtenerPlaylistsUsuario(int usuarioId) {
        // Esta función ya no se usa con DAL directo
        // Las playlists se deben obtener desde tu sistema de gestión
        return CLLPlaylist.getInstancia().getListaPlaylists();
    }
        
    public static List<Cancion> listarCancionesDePlaylist(int playlistId) {
        List<Cancion> canciones = DALPlaylist.listarCancionesDePlaylist(playlistId);
        
        if (canciones == null || canciones.isEmpty()) {
            mostrarAdvertencia("La playlist está vacía.");
            return canciones;
        }
        
        return canciones;
    }
        
    private static boolean validarNombrePlaylist(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarError("El nombre de la playlist no puede estar vacío.");
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
        
        return true;
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
    
    private static void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(
            null,
            mensaje,
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
