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
        // 1. VALIDACIÓN
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }

        // 2. LLAMADA A DAL
        return DALArtista.buscarArtistaPorNombre(nombre);
    }
}
