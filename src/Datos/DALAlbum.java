/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;
import entidades.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Asus
 */
public class DALAlbum {
    
    private static Connection cn = null;
    private static CallableStatement cs = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    
    
    public static String obtenerUrlImagenAlbum(int albumId) {

    String urlImagen = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_obtener_url_imagen_album(?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, albumId);

        rs = cs.executeQuery();

        if (rs.next()) {
            urlImagen = rs.getString("url_imagen");
        }

    } catch (ClassNotFoundException | SQLException ex) {
        System.out.println("Error DAL: " + ex.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
            if (cn != null) cn.close();
        } catch (SQLException ex) {
            System.out.println("Error cerrando recursos: " + ex.getMessage());
        }
    }

    return urlImagen;
}

    public static int obtenerDuracionTotalAlbum(int albumId) {
    int duracionTotal = 0;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_obtener_duracion_total_album(?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, albumId);

        rs = cs.executeQuery();

        if (rs.next()) {
            duracionTotal = rs.getInt("total_duracion");
        }

    } catch (ClassNotFoundException | SQLException ex) {
        System.out.println("Error DAL: " + ex.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
            if (cn != null) cn.close();
        } catch (SQLException ex) {
            System.out.println("Error cerrando recursos: " + ex.getMessage());
        }
    }

    return duracionTotal; // en segundos
}
    
    public static List<Album> listarAlbumesPorArtista(int artistaId) {
    List<Album> lista = new ArrayList<>();

    try {
        cn = conexion.realizarconexion();
        String sql = "{call sp_listar_albumes_x_artista(?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, artistaId);

        rs = cs.executeQuery();

        while (rs.next()) {
            Album a = new Album();
            a.setId(rs.getInt("id"));
            a.setTitulo(rs.getString("titulo"));
            a.setAnio(rs.getInt("anio"));
            a.setUrlImagen(rs.getString("url_imagen"));

            Artista ar = new Artista();
            ar.setId(rs.getInt("artista_id"));
            a.setArtista(ar);

            lista.add(a);
        }

    } catch (ClassNotFoundException | SQLException ex) {
        System.out.println("Error DAL: " + ex.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
            if (cn != null) cn.close();
        } catch (SQLException ex) {
            System.out.println("Error cerrando recursos: " + ex.getMessage());
        }
    }

    return lista;
}


}
