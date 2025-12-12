/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.Comparator;

/**
 *
 * @author chris
 */
public class ListaDoblementeEnlazada<T> {
    
    private NodoDoble<T> primero;
    private NodoDoble<T> ultimo;
    
    public ListaDoblementeEnlazada() {
    }
    
    public NodoDoble<T> getPrimero() {
        return primero;
    }
    
    public void setPrimero(NodoDoble<T> primero) {
        this.primero = primero;
    }
    
    public NodoDoble<T> getUltimo() {
        return ultimo;
    }
    
    public void setUltimo(NodoDoble<T> ultimo) {
        this.ultimo = ultimo;
    }
    
    public boolean esVacia() {
        return primero == null && ultimo == null;
    }
    
    public void insertarAlInicio(T valor) {
        NodoDoble<T> nuevo = new NodoDoble<>(valor);
        if (esVacia()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            nuevo.setSgte(primero);
            primero.setAnt(nuevo);
            primero = nuevo;
        }
    }
    
    public void insertarAlFinal(T valor) {
        NodoDoble<T> nuevo = new NodoDoble<>(valor);
        if (esVacia()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            nuevo.setAnt(ultimo);
            ultimo.setSgte(nuevo);
            ultimo = nuevo;
        }
    }
    
    public int contar() {
        NodoDoble<T> p = primero;
        int c = 0;
        while (p != null) {
            c++;
            p = p.getSgte();
        }
        return c;
    }
    
    public NodoDoble<T> buscar(T dato, Comparator<T> criterio) {
        NodoDoble<T> p = primero;
        while (p != null) {
            if (criterio.compare(p.getInfo(), dato) == 0) {
                return p;
            }
            p = p.getSgte();
        }
        return null;
    }
    
    public boolean eliminar(T dato, Comparator<T> criterio) {
        if (esVacia()) {
            return false;
        } else {
            if (criterio.compare(primero.getInfo(), dato) == 0) {
                if (primero.getSgte() == null) {
                    primero = null;
                    ultimo = null;
                } else {
                    primero = primero.getSgte();
                    primero.setAnt(null);
                }
                return true;
            } else {
                NodoDoble<T> p = primero.getSgte();
                while (p != null && criterio.compare(p.getInfo(), dato) != 0) {
                    p = p.getSgte();
                }
                if (p != null) {
                    if (p == ultimo) {
                        ultimo = ultimo.getAnt();
                        ultimo.setSgte(null);
                    } else {
                        p.getAnt().setSgte(p.getSgte());
                        p.getSgte().setAnt(p.getAnt());
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
    
    public void ordenar(Comparator<T> criterio) {
        if (esVacia() || primero.getSgte() == null) return;
        
        NodoDoble<T> p = primero, q;
        T temp;
        while (p.getSgte() != null) {
            q = p.getSgte();
            while (q != null) {
                if (criterio.compare(p.getInfo(), q.getInfo()) > 0) {
                    temp = p.getInfo();
                    p.setInfo(q.getInfo());
                    q.setInfo(temp);
                }
                q = q.getSgte();
            }
            p = p.getSgte();
        }
    }
    
    // Método para obtener todos los elementos (útil para GUI)
    public T[] obtenerElementos() {
        if (esVacia()) return null;
        
        int tamaño = contar();
        @SuppressWarnings("unchecked")
        T[] elementos = (T[]) new Object[tamaño];
        
        NodoDoble<T> p = primero;
        int i = 0;
        while (p != null) {
            elementos[i++] = p.getInfo();
            p = p.getSgte();
        }
        return elementos;
    }
    
    // Obtener elemento por índice
    public T obtenerPorIndice(int indice) {
        if (indice < 0 || indice >= contar()) return null;
        
        NodoDoble<T> p = primero;
        int i = 0;
        while (p != null && i < indice) {
            p = p.getSgte();
            i++;
        }
        return p != null ? p.getInfo() : null;
    }
}
