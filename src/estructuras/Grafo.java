/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.Comparator;

/**
 * Grafo genérico con pesos
 * @author chris
 * @param <T>
 */
public class Grafo<T> {
    
    private ListaDoblementeEnlazada<NodoGrafo<T>> vertices;
    
    public Grafo() {
        this.vertices = new ListaDoblementeEnlazada<>();
    }
    
    public ListaDoblementeEnlazada<NodoGrafo<T>> getVertices() {
        return vertices;
    }
    
    public void setVertices(ListaDoblementeEnlazada<NodoGrafo<T>> vertices) {
        this.vertices = vertices;
    }
    
    /**
     * Verifica si el grafo está vacío
     */
    public boolean esVacio() {
        return vertices.esVacia();
    }
    
    /**
     * Agrega un vértice al grafo
     */
    public void agregarVertice(T info) {
        NodoGrafo<T> nuevoNodo = new NodoGrafo<>(info);
        vertices.insertarAlFinal(nuevoNodo);
    }
    
    /**
     * Busca un vértice por su información usando un comparador
     */
    public NodoGrafo<T> buscarVertice(T info, Comparator<T> criterio) {
        NodoDoble<NodoGrafo<T>> p = vertices.getPrimero();
        
        while (p != null) {
            if (criterio.compare(p.getInfo().getInfo(), info) == 0) {
                return p.getInfo();
            }
            p = p.getSgte();
        }
        
        return null;
    }
    
    /**
     * Conecta dos vértices de forma bidireccional con peso
     */
    public boolean conectarVertices(T origen, T destino, double peso, Comparator<T> criterio) {
        NodoGrafo<T> nodoOrigen = buscarVertice(origen, criterio);
        NodoGrafo<T> nodoDestino = buscarVertice(destino, criterio);
        
        if (nodoOrigen == null || nodoDestino == null) {
            return false;
        }
        
        // Crear nodos con peso para las listas de adyacencia
        NodoGrafo<T> destinoConPeso = new NodoGrafo<>(destino, peso);
        NodoGrafo<T> origenConPeso = new NodoGrafo<>(origen, peso);
        
        // Conexión bidireccional
        nodoOrigen.getAdyacentes().insertarAlFinal(destinoConPeso);
        nodoDestino.getAdyacentes().insertarAlFinal(origenConPeso);
        
        return true;
    }
    
    /**
     * Obtiene los adyacentes de un vértice
     */
    public ListaDoblementeEnlazada<NodoGrafo<T>> obtenerAdyacentes(T info, Comparator<T> criterio) {
        NodoGrafo<T> nodo = buscarVertice(info, criterio);
        
        if (nodo != null) {
            return nodo.getAdyacentes();
        }
        
        return new ListaDoblementeEnlazada<>();
    }
    
    /**
     * Cuenta el número de vértices
     */
    public int contarVertices() {
        return vertices.contar();
    }
    
    /**
     * Obtiene el grado de un vértice (número de conexiones)
     */
    public int obtenerGrado(T info, Comparator<T> criterio) {
        NodoGrafo<T> nodo = buscarVertice(info, criterio);
        
        if (nodo == null) {
            return 0;
        }
        
        return nodo.getAdyacentes().contar();
    }
    
    /**
     * Recorrido BFS (Búsqueda en Amplitud)
     */
    public ListaDoblementeEnlazada<T> recorridoBFS(T inicio, Comparator<T> criterio) {
        ListaDoblementeEnlazada<T> resultado = new ListaDoblementeEnlazada<>();
        ListaDoblementeEnlazada<T> visitados = new ListaDoblementeEnlazada<>();
        Cola<NodoGrafo<T>> cola = new Cola<>();
        
        NodoGrafo<T> nodoInicio = buscarVertice(inicio, criterio);
        if (nodoInicio == null) {
            return resultado;
        }
        
        cola.encolar(nodoInicio);
        visitados.insertarAlFinal(inicio);
        
        while (!cola.isEmpty()) {
            NodoGrafo<T> actual = cola.desencolar();
            resultado.insertarAlFinal(actual.getInfo());
            
            // Explorar adyacentes
            NodoDoble<NodoGrafo<T>> adyacente = actual.getAdyacentes().getPrimero();
            while (adyacente != null) {
                T infoAdyacente = adyacente.getInfo().getInfo();
                
                // Verificar si no ha sido visitado
                if (visitados.buscar(infoAdyacente, criterio) == null) {
                    NodoGrafo<T> nodoAdyacente = buscarVertice(infoAdyacente, criterio);
                    if (nodoAdyacente != null) {
                        cola.encolar(nodoAdyacente);
                        visitados.insertarAlFinal(infoAdyacente);
                    }
                }
                
                adyacente = adyacente.getSgte();
            }
        }
        
        return resultado;
    }
    
    /**
     * Recorrido DFS (Búsqueda en Profundidad)
     */
    public ListaDoblementeEnlazada<T> recorridoDFS(T inicio, Comparator<T> criterio) {
        ListaDoblementeEnlazada<T> resultado = new ListaDoblementeEnlazada<>();
        ListaDoblementeEnlazada<T> visitados = new ListaDoblementeEnlazada<>();
        Pila<NodoGrafo<T>> pila = new Pila<>();
        
        NodoGrafo<T> nodoInicio = buscarVertice(inicio, criterio);
        if (nodoInicio == null) {
            return resultado;
        }
        
        pila.push(nodoInicio);
        
        while (!pila.isEmpty()) {
            NodoGrafo<T> actual = pila.pop();
            T infoActual = actual.getInfo();
            
            // Si no ha sido visitado
            if (visitados.buscar(infoActual, criterio) == null) {
                resultado.insertarAlFinal(infoActual);
                visitados.insertarAlFinal(infoActual);
                
                // Explorar adyacentes
                NodoDoble<NodoGrafo<T>> adyacente = actual.getAdyacentes().getPrimero();
                while (adyacente != null) {
                    T infoAdyacente = adyacente.getInfo().getInfo();
                    
                    if (visitados.buscar(infoAdyacente, criterio) == null) {
                        NodoGrafo<T> nodoAdyacente = buscarVertice(infoAdyacente, criterio);
                        if (nodoAdyacente != null) {
                            pila.push(nodoAdyacente);
                        }
                    }
                    
                    adyacente = adyacente.getSgte();
                }
            }
        }
        
        return resultado;
    }
    
    /**
     * Limpia todo el grafo
     */
    public void limpiar() {
        vertices = new ListaDoblementeEnlazada<>();
    }
}
