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
    private Usuario usuario;
    private Cancion cancion;
    private double similitud;

    public Recomendacion() {
    }

    public Recomendacion(Cancion cancion, double similitud,Usuario usuario) {
        
        this.cancion = cancion;
        this.similitud = similitud;
        this.usuario=usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
}
