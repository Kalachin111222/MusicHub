/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listasDinamicas;

/**
 *
 * @author Administrador
 */

/*
 * Motor del reproductor reconstruido con Pila y Cola personalizadas.
 */

import entidades.Cancion;
import estructuras.Pila;
import java.util.ArrayList;
import java.util.List;

public class CLLReproductor {
    
    private static final CLLReproductor instancia = new CLLReproductor();

    private Pila<Cancion> historial;      // El pasado
    private Cancion cancionActual;        // El presente
    private Pila<Cancion> pilaSiguientes; // El futuro

    private CLLReproductor() {
        this.historial = new Pila<>();
        this.pilaSiguientes = new Pila<>();
        this.cancionActual = null;
    }

    public static CLLReproductor getInstancia() {
        return instancia;
    }

    public void setNuevaCola(List<Cancion> nuevasCanciones, int indiceInicial) {
        historial.removeAll();
        pilaSiguientes.removeAll();
        
        if (nuevasCanciones == null || nuevasCanciones.isEmpty()) return;
        if (indiceInicial < 0 || indiceInicial >= nuevasCanciones.size()) indiceInicial = 0;

        // Llenamos historial con las anteriores al click
        for (int i = 0; i < indiceInicial; i++) {
            historial.push(nuevasCanciones.get(i));
        }

        cancionActual = nuevasCanciones.get(indiceInicial);

        // Llenamos la pila de siguientes EN REVERSA para que el tope sea la siguiente canción
        for (int i = nuevasCanciones.size() - 1; i > indiceInicial; i--) {
            pilaSiguientes.push(nuevasCanciones.get(i));
        }
    }

    public void insertar(Cancion c) {
        if (c == null) return; // Validación

        // SIEMPRE agregar a la cola de siguientes, incluso si no hay canción actual
        Pila<Cancion> aux = new Pila<>();

        // Vaciar pilaSiguientes en auxiliar
        while(!pilaSiguientes.isEmpty()) {
            aux.push(pilaSiguientes.pop());
        }

        // Agregar la nueva canción al fondo
        pilaSiguientes.push(c); 

        // Restaurar el orden original
        while(!aux.isEmpty()) {
            pilaSiguientes.push(aux.pop());
        }

        // Si no hay canción actual, promover la primera de la cola
        if (cancionActual == null && !pilaSiguientes.isEmpty()) {
            cancionActual = pilaSiguientes.pop();
        }
    }

    public Cancion obtenerSiguiente() {
        if (pilaSiguientes.isEmpty()) return null;
        if (cancionActual != null) historial.push(cancionActual);
        cancionActual = pilaSiguientes.pop();
        return cancionActual;
    }

    public Cancion obtenerAnterior() {
        if (historial.isEmpty()) return null;
        if (cancionActual != null) pilaSiguientes.push(cancionActual);
        cancionActual = historial.pop();
        return cancionActual;
    }

    public List<Cancion> getColaFutura() {
        List<Cancion> listaVisual = new ArrayList<>();
        Pila<Cancion> aux = new Pila<>();
        // Clonamos visualmente para no destruir la pila original
        while(!pilaSiguientes.isEmpty()){
            Cancion c = pilaSiguientes.pop();
            listaVisual.add(c);
            aux.push(c);
        }
        while(!aux.isEmpty()) pilaSiguientes.push(aux.pop());
        return listaVisual;
    }

    public Cancion getActual() { return cancionActual; }
}