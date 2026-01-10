/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

/**
 *
 * @author chris
 */

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author chris
 */
public class Cancion {
    
    private int id;
    private String titulo;
    private int duracion;
    private String genero;
    private String urlAudio;
    
    
    private Artista artista;
    private Album album;
     
    private int numeroReproducciones;

    public Cancion() {
    }

    public Cancion(String titulo, int duracion, String genero, String nombreArtista, String nombreAlbum, String urlAudio) {
        this.id = 0;
        this.titulo = titulo;
        this.duracion = duracion;
        this.genero = genero;
        this.urlAudio = urlAudio;
        
        this.artista = new Artista(); 
        this.artista.setNombre(nombreArtista);
        
        this.album = new Album();
        this.album.setTitulo(nombreAlbum);
        this.numeroReproducciones = 0;
    }

    public Cancion(int id, String titulo, int duracion, String genero, Artista artista, Album album, int popularidad, int numeroReproducciones, String urlAudio, boolean activa) {
        this.id = id;
        this.titulo = titulo;
        this.duracion = duracion;
        this.genero = genero;
        this.artista = artista;
        this.album = album;
        this.numeroReproducciones = numeroReproducciones;
        this.urlAudio = urlAudio;
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
    
    public String getUrlAudio() {
        return urlAudio; 
    }
    public void setUrlAudio(String urlAudio) {
        this.urlAudio = urlAudio; 
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
    
    public int getNumeroReproducciones() {
        return numeroReproducciones; 
    }
    public void setNumeroReproducciones(int numeroReproducciones) {
        this.numeroReproducciones = numeroReproducciones; 
    }
    
    public String getNombreArtista() {
        return (artista != null) ? artista.getNombre() : "Desconocido";
    }
}