/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listasDinamicas;

import entidades.Album;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para Album - Gestiona estado temporal
 */
public class CLLAlbum {
    private static final CLLAlbum instancia = new CLLAlbum();
    
    private CLLAlbum() {}
    
    public static CLLAlbum getInstancia() {
        return instancia;
    }
    
    private List<Album> listaAlbumes = new ArrayList<>();
    private Album albumActual; // Album seleccionado
    
    // ===== GESTIÓN DE ALBUM ACTUAL =====
    
    public void setAlbumActual(Album album) {
        this.albumActual = album;
    }
    
    public Album getAlbumActual() {
        return albumActual;
    }
    
    public boolean hayAlbumSeleccionado() {
        return albumActual != null;
    }
    
    public void limpiarSeleccion() {
        this.albumActual = null;
    }
    
    // ===== GESTIÓN DE LISTAS =====
    
    public void setListaAlbumes(List<Album> albumes) {
        this.listaAlbumes = new ArrayList<>(albumes);
    }
    
    public void agregarALista(Album album) {
        if (!listaAlbumes.contains(album)) {
            listaAlbumes.add(album);
        }
    }
    
    public void limpiarLista() {
        listaAlbumes.clear();
    }
    
    public List<Album> getListaAlbumes() {
        return new ArrayList<>(listaAlbumes);
    }
    
    public int getTamanioLista() {
        return listaAlbumes.size();
    }
    
    public Album getAlbumDeLista(int pos) {
        if (pos >= 0 && pos < listaAlbumes.size()) {
            return listaAlbumes.get(pos);
        }
        return null;
    }
}
