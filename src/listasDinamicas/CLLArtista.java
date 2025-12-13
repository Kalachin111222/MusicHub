/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listasDinamicas;

import entidades.Artista;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para Artista - Gestiona estado temporal
 */
public class CLLArtista {
    private static final CLLArtista instancia = new CLLArtista();
    
    private CLLArtista() {}
    
    public static CLLArtista getInstancia() {
        return instancia;
    }
    
    private List<Artista> listaArtistas = new ArrayList<>();
    private Artista artistaActual; // Artista seleccionado
    
    // ===== GESTIÓN DE ARTISTA ACTUAL =====
    
    public void setArtistaActual(Artista artista) {
        this.artistaActual = artista;
    }
    
    public Artista getArtistaActual() {
        return artistaActual;
    }
    
    public boolean hayArtistaSeleccionado() {
        return artistaActual != null;
    }
    
    public void limpiarSeleccion() {
        this.artistaActual = null;
    }
    
    // ===== GESTIÓN DE LISTAS =====
    
    public void setListaArtistas(List<Artista> artistas) {
        this.listaArtistas = new ArrayList<>(artistas);
    }
    
    public void agregarALista(Artista artista) {
        if (!listaArtistas.contains(artista)) {
            listaArtistas.add(artista);
        }
    }
    
    public void limpiarLista() {
        listaArtistas.clear();
    }
    
    public List<Artista> getListaArtistas() {
        return new ArrayList<>(listaArtistas);
    }
    
    public int getTamanioLista() {
        return listaArtistas.size();
    }
    
    public Artista getArtistaDeLista(int pos) {
        if (pos >= 0 && pos < listaArtistas.size()) {
            return listaArtistas.get(pos);
        }
        return null;
    }
}
