/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listasDinamicas;

import entidades.Playlist;
import entidades.Cancion;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para Playlist - Gestiona estado temporal
 */
public class CLLPlaylist {
    private static final CLLPlaylist instancia = new CLLPlaylist();
    
    private CLLPlaylist() {}
    
    public static CLLPlaylist getInstancia() {
        return instancia;
    }
    
    private List<Playlist> listaPlaylists = new ArrayList<>();
    private Playlist playlistActual; // Playlist seleccionada
    
    // ===== GESTIÓN DE PLAYLIST ACTUAL =====
    
    public void setPlaylistActual(Playlist playlist) {
        this.playlistActual = playlist;
    }
    
    public Playlist getPlaylistActual() {
        return playlistActual;
    }
    
    public boolean hayPlaylistSeleccionada() {
        return playlistActual != null;
    }
    
    public void limpiarSeleccion() {
        this.playlistActual = null;
    }
    
    // ===== GESTIÓN DE LISTAS =====
    
    public void setListaPlaylists(List<Playlist> playlists) {
        this.listaPlaylists = new ArrayList<>(playlists);
    }
    
    public void agregarALista(Playlist playlist) {
        if (!listaPlaylists.contains(playlist)) {
            listaPlaylists.add(playlist);
        }
    }
    
    public void limpiarLista() {
        listaPlaylists.clear();
    }
    
    public List<Playlist> getListaPlaylists() {
        return new ArrayList<>(listaPlaylists);
    }
    
    public int getTamanioLista() {
        return listaPlaylists.size();
    }
    
    public Playlist getPlaylistDeLista(int pos) {
        if (pos >= 0 && pos < listaPlaylists.size()) {
            return listaPlaylists.get(pos);
        }
        return null;
    }
}
