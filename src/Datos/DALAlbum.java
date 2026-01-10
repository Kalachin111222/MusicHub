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

}
