/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

/**
 *
 * @author ArcosArce
 */
public class NodoCircularDoble<T> {
    private T info;
    private NodoCircularDoble<T> sgte;
    private NodoCircularDoble<T> ant;
    
    public NodoCircularDoble(){
        info = null;
        sgte = null;
        ant = null;
    }
    
    public NodoCircularDoble(T info){
        this.info = info;
        sgte = null;
        ant = null;
    }
    
    public NodoCircularDoble(T info,NodoCircularDoble<T> sgte, NodoCircularDoble<T> ant){
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

    public NodoCircularDoble<T> getSgte() {
        return sgte;
    }

    public void setSgte(NodoCircularDoble<T> sgte) {
        this.sgte = sgte;
    }

    public NodoCircularDoble<T> getAnt() {
        return ant;
    }

    public void setAnt(NodoCircularDoble<T> ant) {
        this.ant = ant;
    }
    
}

