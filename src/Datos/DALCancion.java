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
public class DALCancion {
    
    private static Connection cn = null;
    private static CallableStatement cs = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    
    public static String insertarCancion(Cancion c) {

    String mensaje = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_insertar_cancion(?, ?, ?, ?, ?, ?)}";
        cs = cn.prepareCall(sql);

        cs.setString(1, c.getTitulo());
        cs.setInt(2, c.getDuracion());
        cs.setString(3, c.getGenero());
        cs.setInt(4, c.getArtista().getId());
        cs.setInt(5, c.getAlbum().getId());
        cs.setString(6, c.getUrlAudio());

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
    
    public static Cancion obtenerCancionPorId(int id) {

    Cancion c = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_obtener_cancion_por_id(?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, id);

        rs = cs.executeQuery();

        if (rs.next()) {
            c = new Cancion();
            c.setId(rs.getInt("id"));
            c.setTitulo(rs.getString("titulo"));
            c.setDuracion(rs.getInt("duracion"));
            c.setGenero(rs.getString("genero"));
            c.setNumeroReproducciones(rs.getInt("numero_reproducciones"));
            c.setUrlAudio(rs.getString("url_audio"));

            Artista a = new Artista();
            a.setId(rs.getInt("artista_id"));
            a.setNombre(rs.getString("artista_nombre"));

            Album al = new Album();
            al.setId(rs.getInt("album_id"));
            al.setTitulo(rs.getString("album_titulo"));

            c.setArtista(a);
            c.setAlbum(al);
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

    return c;
}
    
public static String actualizarCancion(Cancion c) {

    String mensaje = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_actualizar_cancion(?, ?, ?, ?, ?, ?, ?)}";
        cs = cn.prepareCall(sql);

        cs.setInt(1, c.getId());
        cs.setString(2, c.getTitulo());
        cs.setInt(3, c.getDuracion());
        cs.setString(4, c.getGenero());
        cs.setInt(5, c.getArtista().getId());
        cs.setInt(6, c.getAlbum().getId());
        cs.setString(7, c.getUrlAudio());

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
 



public static List<Cancion> listarCancionesPorArtista(int artistaId) {

    List<Cancion> lista = new ArrayList<>();

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_listar_canciones_por_artista(?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, artistaId);

        rs = cs.executeQuery();

        while (rs.next()) {
            Cancion c = new Cancion();
            c.setId(rs.getInt("id"));
            c.setTitulo(rs.getString("titulo"));
            c.setDuracion(rs.getInt("duracion"));
            c.setGenero(rs.getString("genero"));
            c.setNumeroReproducciones(rs.getInt("numero_reproducciones"));
            c.setUrlAudio(rs.getString("url_audio"));

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

public static List<Cancion> listarCancionesPorAlbum(int albumId) {

    List<Cancion> lista = new ArrayList<>();

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_listar_canciones_por_album(?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, albumId);

        rs = cs.executeQuery();

        while (rs.next()) {
            Cancion c = new Cancion();
            c.setId(rs.getInt("id"));
            c.setTitulo(rs.getString("titulo"));
            c.setDuracion(rs.getInt("duracion"));
            c.setGenero(rs.getString("genero"));
            c.setNumeroReproducciones(rs.getInt("numero_reproducciones"));
            c.setUrlAudio(rs.getString("url_audio"));

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

public static List<Cancion> buscarCancionesPorTitulo(String texto) {

    List<Cancion> lista = new ArrayList<>();

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_buscar_canciones_por_titulo(?)}";
        cs = cn.prepareCall(sql);
        cs.setString(1, texto);

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

public static String incrementarReproducciones(int cancionId) {

    String mensaje = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_incrementar_reproducciones(?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, cancionId);

        int filas = cs.executeUpdate();

        if (filas == 0) {
            mensaje = "Canción no encontrada o inactiva";
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

public static List<Cancion> listarCancionesMasPopulares() {

    List<Cancion> lista = new ArrayList<>();

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_listar_canciones_mas_populares()}";
        cs = cn.prepareCall(sql);

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

public static List<Cancion> listarCancionesPorGeneroNoEscuchadas(int usuarioId, String genero) {
    List<Cancion> lista = new ArrayList<>();

    try {
        cn = conexion.realizarconexion();
        String sql = "{call sp_listar_canciones_por_genero_no_escuchadas(?, ?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, usuarioId);
        cs.setString(2, genero);
        rs = cs.executeQuery();

        while (rs.next()) {
            Cancion c = new Cancion();
            c.setId(rs.getInt("id"));
            c.setTitulo(rs.getString("titulo"));
            c.setGenero(rs.getString("genero"));
            c.setDuracion(rs.getInt("duracion"));
            c.setUrlAudio(rs.getString("url_audio"));
            // Puedes agregar artista y album si quieres
            lista.add(c);
        }

    } catch (Exception e) {
        System.out.println("Error DAL: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
            if (cn != null) cn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    return lista;
}

public static String obtenerUrlAudioCancion(int cancionId) {

    String urlAudio = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_obtener_url_audio_cancion(?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, cancionId);

        rs = cs.executeQuery();

        if (rs.next()) {
            urlAudio = rs.getString("url_audio");
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

    return urlAudio;
}



}
