/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.util.ArrayList;

/**
 *
 * @author chris
 */
public class Album {
    
    // Hola
    private int id;
    private String titulo;
    private int anio;
    private String urlImagen;
    private Artista artista;
    private ArrayList<Cancion> canciones;
    

    public Album() {
        this.canciones = new ArrayList<>();
    }

    public Album(String titulo, int anio, Artista artista) {
        this.id = 0;
        this.titulo = titulo;
        this.anio = anio;
        this.artista = artista;
        this.canciones = new ArrayList<>();
    }
    
    public Album(int id, String titulo, int añoLanzamiento, Artista artista,String urlImagen) {
        this.id = id;
        this.titulo = titulo;
        this.anio = anio;
        this.urlImagen=urlImagen;
        this.artista = artista;
        this.canciones = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    
    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
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
    
    public int obtenerCantidadCanciones() {
        return canciones.size();
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
