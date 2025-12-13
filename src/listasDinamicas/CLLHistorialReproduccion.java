/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listasDinamicas;

import entidades.HistorialReproduccion;
import entidades.Cancion;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para Historial - Gestiona estado temporal
 */
public class CLLHistorialReproduccion {
    private static final CLLHistorialReproduccion instancia = new CLLHistorialReproduccion();
    
    private CLLHistorialReproduccion() {}
    
    public static CLLHistorialReproduccion getInstancia() {
        return instancia;
    }
    
    private List<HistorialReproduccion> listaHistorial = new ArrayList<>();
    private List<Cancion> cancionesRecientes = new ArrayList<>();
    
    // ===== GESTIÓN DE LISTAS =====
    
    public void setListaHistorial(List<HistorialReproduccion> historial) {
        this.listaHistorial = new ArrayList<>(historial);
    }
    
    public void agregarALista(HistorialReproduccion historial) {
        listaHistorial.add(historial);
    }
    
    public void limpiarLista() {
        listaHistorial.clear();
    }
    
    public List<HistorialReproduccion> getListaHistorial() {
        return new ArrayList<>(listaHistorial);
    }
    
    public int getTamanioLista() {
        return listaHistorial.size();
    }
    
    // ===== CANCIONES RECIENTES =====
    
    public void setCancionesRecientes(List<Cancion> canciones) {
        this.cancionesRecientes = new ArrayList<>(canciones);
    }
    
    public List<Cancion> getCancionesRecientes() {
        return new ArrayList<>(cancionesRecientes);
    }
    
    public void agregarCancionReciente(Cancion cancion) {
        // Evitar duplicados consecutivos
        if (cancionesRecientes.isEmpty() || 
            !cancionesRecientes.get(0).equals(cancion)) {
            cancionesRecientes.add(0, cancion); // Agregar al inicio
            
            // Mantener solo las últimas 50
            if (cancionesRecientes.size() > 50) {
                cancionesRecientes.remove(cancionesRecientes.size() - 1);
            }
        }
    }
    
    public void limpiarRecientes() {
        cancionesRecientes.clear();
    }
}
