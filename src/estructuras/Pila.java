/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.EmptyStackException;

/**
 *
 * @author chris
 */
public class Pila<T> {
    
    private NodoSimple<T> tope;
    
    public Pila() {
    }
    
    public NodoSimple<T> getTope() {
        return tope;
    }
    
    public void setTope(NodoSimple<T> tope) {
        this.tope = tope;
    }
    
    public boolean isEmpty() {
        return tope == null;
    }
    
    public void push(T valor) {
        NodoSimple<T> nuevo = new NodoSimple<>(valor);
        nuevo.setSgte(tope);
        tope = nuevo;
    }
    
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T x = tope.getInfo();
        tope = tope.getSgte();
        return x;
    }
    
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return tope.getInfo();
    }
    
    public void removeAll() {
        while (tope != null) {
            tope = tope.getSgte();
        }
    }
    
    // Contar elementos en la pila
    public int contar() {
        NodoSimple<T> p = tope;
        int c = 0;
        while (p != null) {
            c++;
            p = p.getSgte();
        }
        return c;
    }
}
