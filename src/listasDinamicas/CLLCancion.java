/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listasDinamicas;

import entidades.Cancion;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para Cancion - Gestiona estado y reproducción
 */
public class CLLCancion {
    private static final CLLCancion instancia = new CLLCancion();
    
    private CLLCancion() {}
    
    public static CLLCancion getInstancia() {
        return instancia;
    }
    
    private List<Cancion> listaCanciones = new ArrayList<>();
    private Cancion cancionActual; // Canción reproduciéndose
    private int indiceActual = -1;
    
    // ===== GESTIÓN DE REPRODUCCIÓN =====
    
    public void setCancionActual(Cancion cancion) {
        this.cancionActual = cancion;
    }
    
    public Cancion getCancionActual() {
        return cancionActual;
    }
    
    public boolean hayCancionReproduciendose() {
        return cancionActual != null;
    }
    
    public void detenerReproduccion() {
        this.cancionActual = null;
        this.indiceActual = -1;
    }
    
    public void setIndiceActual(int indice) {
        this.indiceActual = indice;
    }
    
    public int getIndiceActual() {
        return indiceActual;
    }
    
    // ===== NAVEGACIÓN EN PLAYLIST/LISTA =====
    
    public Cancion siguienteCancion() {
        if (listaCanciones.isEmpty()) {
            return null;
        }
        
        indiceActual++;
        if (indiceActual >= listaCanciones.size()) {
            indiceActual = 0; // Volver al inicio
        }
        
        cancionActual = listaCanciones.get(indiceActual);
        return cancionActual;
    }
    
    public Cancion anteriorCancion() {
        if (listaCanciones.isEmpty()) {
            return null;
        }
        
        indiceActual--;
        if (indiceActual < 0) {
            indiceActual = listaCanciones.size() - 1; // Ir al final
        }
        
        cancionActual = listaCanciones.get(indiceActual);
        return cancionActual;
    }
    
    // ===== GESTIÓN DE LISTAS =====
    
    public void setListaCanciones(List<Cancion> canciones) {
        this.listaCanciones = new ArrayList<>(canciones);
    }
    
    public void agregarALista(Cancion cancion) {
        if (!listaCanciones.contains(cancion)) {
            listaCanciones.add(cancion);
        }
    }
    
    public void limpiarLista() {
        listaCanciones.clear();
        detenerReproduccion();
    }
    
    public List<Cancion> getListaCanciones() {
        return new ArrayList<>(listaCanciones);
    }
    
    public int getTamanioLista() {
        return listaCanciones.size();
    }
    
    public Cancion getCancionDeLista(int pos) {
        if (pos >= 0 && pos < listaCanciones.size()) {
            return listaCanciones.get(pos);
        }
        return null;
    }
}
