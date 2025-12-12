/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.Comparator;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author chris
 */
public class ColaPrioridad<T> {
    
    private NodoSimple<T> primero;
    private NodoSimple<T> ultimo;

    public ColaPrioridad() {
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
    
    public boolean esVacia(){
        return primero == null;
    }
    
    public void encolar(T valor, Comparator<T> criterio){
        NodoSimple<T> nuevo = new NodoSimple(valor);
        
        if(esVacia()){
            primero = nuevo;
            ultimo = nuevo;
        } else {
            if(criterio.compare(primero.getInfo(), valor) > 0){
                nuevo.setSgte(primero);
                primero = nuevo;
            } else {
                NodoSimple<T> ant = primero, p = primero.getSgte();
                
                while(p != null && criterio.compare(p.getInfo(), valor) <= 0){
                    ant = p;
                    p = p.getSgte();                    
                }
                if(p==null){
                    ant.setSgte(nuevo);
                    ultimo = nuevo;
                } else {
                    nuevo.setSgte(p);
                    ant.setSgte(nuevo);
                }
            }
        }
    }
    
    public T desencolar() {
        if (esVacia()) {
            throw new IllegalStateException("Cola de prioridad vacía");
        }
        T valor = primero.getInfo();
        primero = primero.getSgte();
        if (primero == null) {
            ultimo = null;
        }
        return valor;
    }
    
    public T cima() {
        if (esVacia()) {
            throw new IllegalStateException("Cola de prioridad vacía");
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
    
    // depende de santiago si coloca un metodo para obtener los elementos
    public void mostrar(DefaultTableModel modelo) {
        // Esto lo definiria santiago
    }

}
