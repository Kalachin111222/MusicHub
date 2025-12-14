/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;
import entidades.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Asus
 */
public class DALPlaylist {
    private static Connection cn = null;
    private static CallableStatement cs = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    
  public static String crearPlaylist(Playlist p) {

    String mensaje = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_crear_playlist(?, ?, ?)}";
        cs = cn.prepareCall(sql);

        cs.setString(1, p.getNombre());
        cs.setInt(2, p.getUsuario().getId());
        cs.setDate(3, java.sql.Date.valueOf(p.getFechaCreacion()));

        cs.executeUpdate();

    } catch (ClassNotFoundException | SQLException ex) {
        mensaje = ex.getMessage();
    } finally {
        try {
            if (cs != null) cs.close();
            if (cn != null) cn.close();
        } catch (SQLException ex) {
            mensaje = ex.getMessage();
        }
    }

    return mensaje;
}
  
public static String agregarCancionAPlaylist(int playlistId, int cancionId) {

    String mensaje = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_agregar_cancion_playlist(?, ?)}";
        cs = cn.prepareCall(sql);

        cs.setInt(1, playlistId);
        cs.setInt(2, cancionId);

        cs.executeUpdate();

    } catch (SQLException ex) {

        if (ex.getErrorCode() == 1062) {
            mensaje = "La canción ya está en la playlist";
        } else if (ex.getErrorCode() == 1452) {
            mensaje = "Playlist o canción no existen";
        } else {
            mensaje = ex.getMessage();
        }

    } catch (ClassNotFoundException ex) {
        mensaje = ex.getMessage();
    } finally {
        try {
            if (cs != null) cs.close();
            if (cn != null) cn.close();
        } catch (SQLException ex) {
            mensaje = ex.getMessage();
        }
    }

    return mensaje;
}

public static List<Cancion> listarCancionesDePlaylist(int playlistId) {

    List<Cancion> lista = new ArrayList<>();

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_listar_canciones_de_playlist(?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, playlistId);

        rs = cs.executeQuery();

        while (rs.next()) {
            Cancion c = new Cancion();
            c.setId(rs.getInt("id"));
            c.setTitulo(rs.getString("titulo"));
            c.setDuracion(rs.getInt("duracion"));
            c.setGenero(rs.getString("genero"));
            c.setNumeroReproducciones(rs.getInt("numero_reproducciones"));
            c.setUrlAudio(rs.getString("url_audio"));

            Artista ar = new Artista();
            ar.setId(rs.getInt("artista_id"));
            ar.setNombre(rs.getString("artista_nombre"));
            c.setArtista(ar);

            Album al = new Album();
            al.setId(rs.getInt("album_id"));
            al.setTitulo(rs.getString("album_titulo"));
            c.setAlbum(al);

            lista.add(c);
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

public static String quitarCancionDePlaylist(int playlistId, int cancionId) {

    String mensaje = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_quitar_cancion_playlist(?, ?)}";
        cs = cn.prepareCall(sql);

        cs.setInt(1, playlistId);
        cs.setInt(2, cancionId);

        int filas = cs.executeUpdate();

        if (filas == 0) {
            mensaje = "La canción no está en la playlist";
        }

    } catch (ClassNotFoundException | SQLException ex) {
        mensaje = ex.getMessage();
    } finally {
        try {
            if (cs != null) cs.close();
            if (cn != null) cn.close();
        } catch (SQLException ex) {
            mensaje = ex.getMessage();
        }
    }

    return mensaje;
}


  
}
