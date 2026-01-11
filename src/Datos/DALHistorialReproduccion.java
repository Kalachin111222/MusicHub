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
public class DALHistorialReproduccion {
    private static Connection cn = null;
    private static CallableStatement cs = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    
    public static String registrarReproduccion(int usuarioId, int cancionId) {

    String mensaje = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_registrar_reproduccion(?, ?)}";
        cs = cn.prepareCall(sql);

        cs.setInt(1, usuarioId);
        cs.setInt(2, cancionId);

        cs.executeUpdate();

    } catch (SQLException ex) {
        if (ex.getErrorCode() == 1452) {
            mensaje = "Usuario o canción no existen";
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
    
    public static List<HistorialReproduccion> listarHistorialPorUsuario(int usuarioId) {

    List<HistorialReproduccion> lista = new ArrayList<>();

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_listar_historial_por_usuario(?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, usuarioId);

        rs = cs.executeQuery();

        while (rs.next()) {

            HistorialReproduccion h = new HistorialReproduccion();
            h.setId(rs.getInt("id"));
            h.setFechaReproduccion(
                rs.getTimestamp("fecha_reproduccion").toLocalDateTime()
            );

            Cancion c = new Cancion();
            c.setId(rs.getInt("cancion_id"));
            c.setTitulo(rs.getString("titulo"));
            c.setDuracion(rs.getInt("duracion"));
            c.setUrlAudio(rs.getString("url_audio"));

            Artista ar = new Artista();
            ar.setId(rs.getInt("artista_id"));
            ar.setNombre(rs.getString("artista_nombre"));

            c.setArtista(ar);
            h.setCancion(c);

            lista.add(h);
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

public static List<HistorialReproduccion> listarUltimasReproducciones(int usuarioId, int limite) {

    List<HistorialReproduccion> lista = new ArrayList<>();

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_listar_ultimas_reproducciones(?, ?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, usuarioId);
        cs.setInt(2, limite);

        rs = cs.executeQuery();

        while (rs.next()) {

            HistorialReproduccion h = new HistorialReproduccion();
            h.setId(rs.getInt("id"));
            h.setFechaReproduccion(
                rs.getTimestamp("fecha_reproduccion").toLocalDateTime()
            );

            Cancion c = new Cancion();
            c.setId(rs.getInt("cancion_id"));
            c.setTitulo(rs.getString("titulo"));
            c.setDuracion(rs.getInt("duracion"));
            c.setUrlAudio(rs.getString("url_audio"));

            Artista ar = new Artista();
            ar.setId(rs.getInt("artista_id"));
            ar.setNombre(rs.getString("artista_nombre"));

            c.setArtista(ar);
            h.setCancion(c);

            lista.add(h);
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

    public static String obtenerGeneroFavorito(int usuarioId) {
        String genero = null;
        try {
            cn = conexion.realizarconexion();
            String sql = "{call sp_obtener_genero_favorito(?)}";
            cs = cn.prepareCall(sql);
            cs.setInt(1, usuarioId);
            rs = cs.executeQuery();

            if (rs.next()) {
                genero = rs.getString("genero");
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
        return genero;
    }

}
