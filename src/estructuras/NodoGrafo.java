/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

/**
 * Nodo genérico para el grafo
 * @author chris
 */
public class NodoGrafo<T> {
    
    private T info;
    private ListaDoblementeEnlazada<NodoGrafo<T>> adyacentes;
    private double peso;
    
    public NodoGrafo() {
        this.adyacentes = new ListaDoblementeEnlazada<>();
        this.peso = 0.0;
    }
    
    public NodoGrafo(T info) {
        this.info = info;
        this.adyacentes = new ListaDoblementeEnlazada<>();
        this.peso = 0.0;
    }
    
    public NodoGrafo(T info, double peso) {
        this.info = info;
        this.adyacentes = new ListaDoblementeEnlazada<>();
        this.peso = peso;
    }
    
    public T getInfo() {
        return info;
    }
    
    public void setInfo(T info) {
        this.info = info;
    }
    
    public ListaDoblementeEnlazada<NodoGrafo<T>> getAdyacentes() {
        return adyacentes;
    }
    
    public void setAdyacentes(ListaDoblementeEnlazada<NodoGrafo<T>> adyacentes) {
        this.adyacentes = adyacentes;
    }
    
    public double getPeso() {
        return peso;
    }
    
    public void setPeso(double peso) {
        this.peso = peso;
    }
}
