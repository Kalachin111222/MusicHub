/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author chris
 */
public class Cancion {
    
    private int id;
    private String titulo;
    private int duracion;
    private String genero;
    private Artista artista;
    private Album album;
    private int popularidad; // (0-100) 
    private int numeroReproducciones;

    public Cancion() {
    }

    public Cancion(String titulo, int duracion, String genero, Artista artista, Album album, int popularidad) {
        this.id = 0;
        this.titulo = titulo;
        this.duracion = duracion;
        this.genero = genero;
        this.artista = artista;
        this.album = album;
        this.popularidad = popularidad;
        this.numeroReproducciones = 0;
    }

    public Cancion(int id, String titulo, int duracion, String genero, Artista artista, Album album, int popularidad, int numeroReproducciones) {
        this.id = id;
        this.titulo = titulo;
        this.duracion = duracion;
        this.genero = genero;
        this.artista = artista;
        this.album = album;
        this.popularidad = popularidad;
        this.numeroReproducciones = numeroReproducciones;
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

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public int getPopularidad() {
        return popularidad;
    }

    public void setPopularidad(int popularidad) {
        this.popularidad = popularidad;
    }

    public int getNumeroReproducciones() {
        return numeroReproducciones;
    }

    public void setNumeroReproducciones(int numeroReproducciones) {
        this.numeroReproducciones = numeroReproducciones;
    }
    
    public void incrementarReproducciones() {
        this.numeroReproducciones++;
    }
    
    // Convertir duración de segundos a formato mm:ss
    public String getDuracionFormateada() {
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%d:%02d", minutos, segundos);
    }
    
    public int compararPorTitulo(Cancion otra) {
        return this.titulo.compareToIgnoreCase(otra.getTitulo());
    }
    
    public int compararPorPopularidad(Cancion otra) {
        return Integer.compare(otra.getPopularidad(), this.popularidad); // Descendente
    }
}
