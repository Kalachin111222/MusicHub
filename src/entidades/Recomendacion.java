/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author chris
 */
public class Recomendacion {
    
    private Cancion cancion;
    private double similitud;

    public Recomendacion() {
    }

    public Recomendacion(Cancion cancion, double similitud) {
        this.cancion = cancion;
        this.similitud = similitud;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    public double getSimilitud() {
        return similitud;
    }

    public void setSimilitud(double similitud) {
        this.similitud = similitud;
    }
}
