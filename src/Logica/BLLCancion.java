/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import listasDinamicas.*;
import Datos.DALCancion;
import entidades.Cancion;
import entidades.Artista;
import entidades.Album;
import javax.swing.JOptionPane;
import java.util.List;

/**
 */
public class BLLCancion {
    
    
    public static boolean insertarCancion(String titulo, String duracionStr, String genero,
                                         Artista artista, Album album, String urlAudio) {
        // 1. Validar datos
        if (!validarDatosCancion(titulo, duracionStr, genero, artista)) {
            return false;
        }
        
        if (urlAudio == null || urlAudio.trim().isEmpty()) {
            mostrarError("Debe proporcionar la URL del audio.");
            return false;
        }
        
        // 2. Crear canción
        int duracion = Integer.parseInt(duracionStr.trim());
        
        Cancion nuevaCancion = new Cancion();
        nuevaCancion.setTitulo(titulo.trim());
        nuevaCancion.setDuracion(duracion);
        nuevaCancion.setGenero(genero.trim());
        nuevaCancion.setArtista(artista);
        nuevaCancion.setAlbum(album);
        nuevaCancion.setUrlAudio(urlAudio.trim());
        nuevaCancion.setNumeroReproducciones(0);
        
        // 3. Insertar en BD
        String mensaje = DALCancion.insertarCancion(nuevaCancion);
        
        if (mensaje != null) {
            mostrarError("Error al insertar canción: " + mensaje);
            return false;
        }
        
        mostrarExito("Canción agregada exitosamente.");
        return true;
    }
        
    public static boolean actualizarCancion(Cancion cancion, String titulo, String duracionStr,
                                           String genero, Artista artista, Album album, String urlAudio) {
        // 1. Validar datos
        if (!validarDatosCancion(titulo, duracionStr, genero, artista)) {
            return false;
        }
        
        if (urlAudio == null || urlAudio.trim().isEmpty()) {
            mostrarError("Debe proporcionar la URL del audio.");
            return false;
        }
        
        // 2. Actualizar datos
        int duracion = Integer.parseInt(duracionStr.trim());
        
        cancion.setTitulo(titulo.trim());
        cancion.setDuracion(duracion);
        cancion.setGenero(genero.trim());
        cancion.setArtista(artista);
        cancion.setAlbum(album);
        cancion.setUrlAudio(urlAudio.trim());
        
        // 3. Actualizar en BD
        String mensaje = DALCancion.actualizarCancion(cancion);
        
        if (mensaje != null) {
            mostrarError("Error al actualizar canción: " + mensaje);
            return false;
        }
        
        mostrarExito("Canción actualizada exitosamente.");
        return true;
    }
    
    public static List<Cancion> buscarCanciones(String criterio, String valor) {
        if (!validarCriterioBusqueda(valor)) {
            return null;
        }
        
        List<Cancion> resultados = null;
        
        switch (criterio.toLowerCase()) {
            case "titulo":
                resultados = DALCancion.buscarCancionesPorTitulo(valor.trim());
                break;
            case "genero":
                resultados = DALCancion.buscarCancionesPorTitulo(valor.trim()); // Buscar por título que contenga el género
                break;
            default:
                mostrarError("Criterio de búsqueda no válido.");
                return null;
        }
        
        // Guardar en CLL
        if (resultados != null && !resultados.isEmpty()) {
            CLLCancion.getInstancia().setListaCanciones(resultados);
        }
        
        return resultados;
    }
    
    public static List<Cancion> obtenerCancionesParaMostrar() {
        List<Cancion> canciones = DALCancion.listarCancionesMasPopulares();
        
        if (canciones != null && !canciones.isEmpty()) {
            CLLCancion.getInstancia().setListaCanciones(canciones);
        }
        
        return canciones;
    }
    
    public static List<Cancion> ordenarCanciones(List<Cancion> canciones, String criterio) {
        if (canciones == null || canciones.isEmpty()) {
            return canciones;
        }
        
        switch (criterio.toLowerCase()) {
            case "titulo":
                canciones.sort((c1, c2) -> c1.getTitulo().compareToIgnoreCase(c2.getTitulo()));
                break;
            case "reproducciones":
                canciones.sort((c1, c2) -> Integer.compare(c2.getNumeroReproducciones(), c1.getNumeroReproducciones()));
                break;
            case "duracion":
                canciones.sort((c1, c2) -> Integer.compare(c1.getDuracion(), c2.getDuracion()));
                break;
            default:
                mostrarAdvertencia("Criterio de ordenamiento no reconocido.");
        }
        
        return canciones;
    }
    
    public static List<Cancion> listarCancionesPorArtista(int idArtista) {
        List<Cancion> canciones = DALCancion.listarCancionesPorArtista(idArtista);
        
        if (canciones != null && !canciones.isEmpty()) {
            CLLCancion.getInstancia().setListaCanciones(canciones);
        }
        
        return canciones;
    }
    
    public static List<Cancion> listarCancionesPorAlbum(int idAlbum) {
        List<Cancion> canciones = DALCancion.listarCancionesPorAlbum(idAlbum);
        
        if (canciones != null && !canciones.isEmpty()) {
            CLLCancion.getInstancia().setListaCanciones(canciones);
        }
        
        return canciones;
    }
        
    public static boolean reproducirCancion(Cancion cancion, int idUsuario) {
        if (!validarReproduccion(cancion, idUsuario)) {
            return false;
        }
        
        // Establecer como canción actual
        CLLCancion.getInstancia().setCancionActual(cancion);
        
        // Incrementar reproducciones
        String mensaje = DALCancion.incrementarReproducciones(cancion.getId());
        
        if (mensaje != null) {
            System.out.println("Advertencia: " + mensaje);
        }
        
        return true;
    }
    
    private static boolean validarReproduccion(Cancion cancion, int idUsuario) {
        if (cancion == null) {
            mostrarError("No hay canción seleccionada.");
            return false;
        }
        
        if (!CLLUsuario.getInstancia().hayUsuarioLogueado()) {
            mostrarError("Debe iniciar sesión para reproducir.");
            return false;
        }
        
        return true;
    }
        
    private static boolean validarDatosCancion(String titulo, String duracionStr,
                                              String genero, Artista artista) {
        if (!validarTitulo(titulo)) {
            return false;
        }
        
        if (!validarDuracion(duracionStr)) {
            return false;
        }
        
        if (!validarGenero(genero)) {
            return false;
        }
        
        if (artista == null) {
            mostrarError("Debe seleccionar un artista.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            mostrarError("El título no puede estar vacío.");
            return false;
        }
        
        if (titulo.trim().length() > 100) {
            mostrarError("El título no puede exceder 100 caracteres.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarDuracion(String duracionStr) {
        if (duracionStr == null || duracionStr.trim().isEmpty()) {
            mostrarError("La duración no puede estar vacía.");
            return false;
        }
        
        try {
            int duracion = Integer.parseInt(duracionStr.trim());
            
            if (duracion <= 0) {
                mostrarError("La duración debe ser mayor a 0.");
                return false;
            }
            
            if (duracion > 3600) {
                mostrarError("La duración no puede exceder 3600 segundos.");
                return false;
            }
            
        } catch (NumberFormatException e) {
            mostrarError("La duración debe ser un número válido.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            mostrarError("El género no puede estar vacío.");
            return false;
        }
        
        if (genero.trim().length() > 50) {
            mostrarError("El género no puede exceder 50 caracteres.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarCriterioBusqueda(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            mostrarError("Ingrese un criterio de búsqueda.");
            return false;
        }
        
        if (criterio.trim().length() < 2) {
            mostrarError("El criterio debe tener al menos 2 caracteres.");
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
