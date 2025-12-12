/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.time.LocalDateTime;

/**
 *
 * @author chris
 */
public class HistorialReproduccion {
    
    private int id;
    private Usuario usuario;
    private Cancion cancion;
    private LocalDateTime fechaReproduccion; // incluye la hora

    public HistorialReproduccion() {
    }

    public HistorialReproduccion(Usuario usuario, Cancion cancion, LocalDateTime fechaReproduccion) {
        this.id = 0;
        this.usuario = usuario;
        this.cancion = cancion;
        this.fechaReproduccion = fechaReproduccion;
    }

    public HistorialReproduccion(int id, Usuario usuario, Cancion cancion, LocalDateTime fechaReproduccion) {
        this.id = id;
        this.usuario = usuario;
        this.cancion = cancion;
        this.fechaReproduccion = fechaReproduccion;
    }
    
    public boolean esNuevo() {
        return id == 0;
    }
}
