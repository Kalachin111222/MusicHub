/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listasDinamicas;

import entidades.Recomendacion;
import entidades.Cancion;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para Recomendaciones - Gestiona estado temporal
 */
public class CLLRecomendacion {
    private static final CLLRecomendacion instancia = new CLLRecomendacion();
    
    private CLLRecomendacion() {}
    
    public static CLLRecomendacion getInstancia() {
        return instancia;
    }
    
    private List<Recomendacion> listaRecomendaciones = new ArrayList<>();
    private List<Cancion> cancionesRecomendadas = new ArrayList<>();
    private String ultimoGeneroExplorado;
    
    // ===== GESTIÓN DE RECOMENDACIONES =====
    
    public void setListaRecomendaciones(List<Recomendacion> recomendaciones) {
        this.listaRecomendaciones = new ArrayList<>(recomendaciones);
        // Extraer las canciones de las recomendaciones
        cancionesRecomendadas.clear();
        for (Recomendacion rec : recomendaciones) {
            cancionesRecomendadas.add(rec.getCancion());
        }
    }
    
    public List<Recomendacion> getListaRecomendaciones() {
        return new ArrayList<>(listaRecomendaciones);
    }
    
    public List<Cancion> getCancionesRecomendadas() {
        return new ArrayList<>(cancionesRecomendadas);
    }
    
    public void limpiarRecomendaciones() {
        listaRecomendaciones.clear();
        cancionesRecomendadas.clear();
    }
    
    // ===== GESTIÓN DE EXPLORACIÓN POR GÉNERO =====
    
    public void setCancionesPorGenero(List<Cancion> canciones, String genero) {
        this.cancionesRecomendadas = new ArrayList<>(canciones);
        this.ultimoGeneroExplorado = genero;
    }
    
    public String getUltimoGeneroExplorado() {
        return ultimoGeneroExplorado;
    }
    
    // ===== UTILIDADES =====
    
    public int getTamanioLista() {
        return cancionesRecomendadas.size();
    }
    
    public Cancion getCancionRecomendada(int pos) {
        if (pos >= 0 && pos < cancionesRecomendadas.size()) {
            return cancionesRecomendadas.get(pos);
        }
        return null;
    }
    
    public Recomendacion getRecomendacion(int pos) {
        if (pos >= 0 && pos < listaRecomendaciones.size()) {
            return listaRecomendaciones.get(pos);
        }
        return null;
    }
    
    public boolean hayRecomendaciones() {
        return !cancionesRecomendadas.isEmpty();
    }
}
