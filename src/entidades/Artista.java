/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author chris
 */
public class Artista {
    
    private int id;
    private String nombre;
    private String genero;
    private String descripcion;

    public Artista() {
    }

    // Para objetos nuevos (aún no guardados en BD)
    public Artista(String nombre, String genero, String descripcion) {
        this.id = 0;
        this.nombre = nombre;
        this.genero = genero;
        this.descripcion = descripcion;
    }
    
    // Para objetos traídos de la BD
    public Artista(int id, String nombre, String genero, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public boolean esNuevo() {
        return id == 0;
    }
}
