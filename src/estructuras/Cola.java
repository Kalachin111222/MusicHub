/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.NoSuchElementException;
import javax.swing.DefaultListModel;

/**
 *
 * @author chris
 */
public class Cola<T> {
    private NodoSimple<T> primero;
    private NodoSimple<T> ultimo;
    
    public Cola() {
    }
    
    public NodoSimple<T> getPrimero() {
        return primero;
    }
    
    public void setPrimero(NodoSimple<T> primero) {
        this.primero = primero;
    }
    
    public NodoSimple<T> getUltimo() {
        return ultimo;
    }
    
    public void setUltimo(NodoSimple<T> ultimo) {
        this.ultimo = ultimo;
    }
    
    public boolean isEmpty() {
        return primero == null && ultimo == null;
    }
    
    public void encolar(T valor) {
        NodoSimple<T> nuevo = new NodoSimple<>(valor);
        if (isEmpty()) {
            primero = nuevo;
        } else {
            ultimo.setSgte(nuevo);
        }
        ultimo = nuevo;
    }
    
    public T desencolar() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cola vacía");
        }
        T x = primero.getInfo();
        primero = primero.getSgte();
        if (primero == null) {
            ultimo = null;
        }
        return x;
    }
    
    public T cima() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cola vacía");
        }
        return primero.getInfo();
    }
    
    public void eliminarTodos() {
        while (primero != null) {
            primero = primero.getSgte();
        }
        ultimo = null;
    }
    
    public int contar() {
        NodoSimple<T> p = primero;
        int c = 0;
        while (p != null) {
            c++;
            p = p.getSgte();
        }
        return c;
    }
}
