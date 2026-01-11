/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;
import entidades.Artista;
import Datos.DALArtista;

/**
 *
 * @author Administrador
 */
public class BLLArtista {

    
    public static Artista obtenerDatosArtista(String nombre) {
        // 1. VALIDACIÓN: ¿Me dieron un nombre real?
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("BLL: El nombre del artista está vacío.");
            return null;
        }

        // 2. CONEXIÓN: Llamamos a la DAL
        Artista artista = DALArtista.buscarArtistaPorNombre(nombre);

        // 3. POST-VALIDACIÓN (Opcional)
        if (artista == null) {
            System.out.println("BLL: No se encontró al artista " + nombre + " en la BD.");
        }

        return artista;
    }
}
