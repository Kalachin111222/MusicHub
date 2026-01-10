/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listasDinamicas;

/**
 *
 * @author Administrador
 */
import java.util.ArrayList;
import java.util.List;
import entidades.Cancion;

public class CLLReproductor {
    private static final CLLReproductor instancia = new CLLReproductor();

    private List<Cancion> colaReproduccion; 
    private int indiceActual; 

    private CLLReproductor() {
        this.colaReproduccion = new ArrayList<>();
        this.indiceActual = -1; 
    }

    public static CLLReproductor getInstancia() {
        return instancia;
    }

    public void setNuevaCola(List<Cancion> nuevasCanciones) {
        if (nuevasCanciones == null || nuevasCanciones.isEmpty()) {
            this.colaReproduccion = new ArrayList<>();
            this.indiceActual = -1;
        } else {
            this.colaReproduccion = new ArrayList<>(nuevasCanciones);
            this.indiceActual = 0;
        }
    }

    public Cancion obtenerSiguiente() {
        if (colaReproduccion.isEmpty()) return null;
        indiceActual = (indiceActual + 1) % colaReproduccion.size();
        return colaReproduccion.get(indiceActual);
    }


    public Cancion obtenerAnterior() {
        if (colaReproduccion.isEmpty()) return null;
        indiceActual = (indiceActual - 1 + colaReproduccion.size()) % colaReproduccion.size();
        return colaReproduccion.get(indiceActual);
    }
    
    public Cancion getActual() {
        if (indiceActual != -1 && !colaReproduccion.isEmpty()) {
            return colaReproduccion.get(indiceActual);
        }
        return null;
    }
}
