/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

/**
 *
 * @author chris
 */
public class NodoSimple<T> {
    
    private T info;
    private NodoSimple<T> sgte;

    public NodoSimple() {
    }

    public NodoSimple(T info) {
        this.info = info;
    }

    public NodoSimple(T info, NodoSimple<T> sgte) {
        this.info = info;
        this.sgte = sgte;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public NodoSimple<T> getSgte() {
        return sgte;
    }

    public void setSgte(NodoSimple<T> sgte) {
        this.sgte = sgte;
    }
}
