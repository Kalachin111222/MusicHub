/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

/**
 *
 * @author chris
 */
public class NodoDoble<T> {
    
    private T info;
    private NodoDoble<T> sgte;
    private NodoDoble<T> ant;

    public NodoDoble() {
    }

    public NodoDoble(T info) {
        this.info = info;
    }

    public NodoDoble(T info, NodoDoble<T> sgte, NodoDoble<T> ant) {
        this.info = info;
        this.sgte = sgte;
        this.ant = ant;
    }

    public T getInfo() {
        return info;
    }

    public void setInfo(T info) {
        this.info = info;
    }

    public NodoDoble<T> getSgte() {
        return sgte;
    }

    public void setSgte(NodoDoble<T> sgte) {
        this.sgte = sgte;
    }

    public NodoDoble<T> getAnt() {
        return ant;
    }

    public void setAnt(NodoDoble<T> ant) {
        this.ant = ant;
    }
}
