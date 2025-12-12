/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author chris
 */
public class Playlist {
    
    private int id;
    private String nombre;
    private Usuario usuario;
    private LocalDate fechaCreacion; 
    private ArrayList<Cancion> canciones;

    public Playlist() {
        this.canciones = new ArrayList<>();
    }
    
    public Playlist(String nombre, Usuario usuario, LocalDate fechaCreacion) {
        this.id = 0;
        this.nombre = nombre;
        this.usuario = usuario;
        this.fechaCreacion = fechaCreacion;
        this.canciones = new ArrayList<>();
    }

    public Playlist(int id, String nombre, Usuario usuario, LocalDate fechaCreacion, ArrayList<Cancion> canciones) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.fechaCreacion = fechaCreacion;
        this.canciones = canciones;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ArrayList<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(ArrayList<Cancion> canciones) {
        this.canciones = canciones;
    }
    
    public void agregarCancion(Cancion c) {
        canciones.add(c);
    }
    
    public void eliminarCancion(int index) {
        if (index >= 0 && index < canciones.size()) {
            canciones.remove(index);
        }
    }
    
    public void eliminarCancionPorId(int cancionId) {
        canciones.removeIf(c -> c.getId() == cancionId);
    }
    
    public int obtenerCantidadCanciones() {
        return canciones.size();
    }
    
    public Cancion buscarCancionPorTitulo(String titulo) {
        for (Cancion c : canciones) {
            if (c.getTitulo().equalsIgnoreCase(titulo)) {
                return c;
            }
        }
        return null;
    }
    
    public int obtenerDuracionTotal() {
        int total = 0;
        for (Cancion c : canciones) {
            total += c.getDuracion();
        }
        return total;
    }
    
    public boolean esNuevo() {
        return id == 0;
    }
}
