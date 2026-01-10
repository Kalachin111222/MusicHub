/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import listasDinamicas.CLLAlbum;
import Datos.DALAlbum;
import entidades.Album;
import javax.swing.JOptionPane;

/**
 * Lógica de negocio para Album - Integra con DAL
 */
public class BLLAlbum {
    
    
    public static String obtenerUrlImagenAlbum(int albumId) {
        if (!validarIdAlbum(albumId)) {
            return null;
        }
        
        String urlImagen = DALAlbum.obtenerUrlImagenAlbum(albumId);
        
        if (urlImagen == null || urlImagen.trim().isEmpty()) {
            mostrarAdvertencia("No se encontró imagen para este álbum.");
            return null;
        }
        
        return urlImagen;
    }
    
    
    public static void seleccionarAlbum(Album album) {
        if (album == null) {
            mostrarError("No se puede seleccionar un álbum nulo.");
            return;
        }
        
        CLLAlbum.getInstancia().setAlbumActual(album);
    }
    
    public static Album obtenerAlbumSeleccionado() {
        return CLLAlbum.getInstancia().getAlbumActual();
    }
    
    public static boolean hayAlbumSeleccionado() {
        return CLLAlbum.getInstancia().hayAlbumSeleccionado();
    }
    
    public static void limpiarSeleccion() {
        CLLAlbum.getInstancia().limpiarSeleccion();
    }
    
    
    public static void agregarAlbumALista(Album album) {
        if (album == null) {
            mostrarError("No se puede agregar un álbum nulo a la lista.");
            return;
        }
        
        CLLAlbum.getInstancia().agregarALista(album);
    }
    
    public static void limpiarListaAlbumes() {
        CLLAlbum.getInstancia().limpiarLista();
    }
    
    public static Album obtenerAlbumDeLista(int posicion) {
        if (posicion < 0 || posicion >= CLLAlbum.getInstancia().getTamanioLista()) {
            mostrarError("Posición inválida en la lista de álbumes.");
            return null;
        }
        
        return CLLAlbum.getInstancia().getAlbumDeLista(posicion);
    }
    
    public static int obtenerTamanioListaAlbumes() {
        return CLLAlbum.getInstancia().getTamanioLista();
    }
    
    
    private static boolean validarIdAlbum(int albumId) {
        if (albumId <= 0) {
            mostrarError("El ID del álbum debe ser mayor a 0.");
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
    
    public static void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(
            null,
            mensaje,
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
