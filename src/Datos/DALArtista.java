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
public class DALArtista {
  
    private static Connection cn = null;
    private static CallableStatement cs = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    
    public static Artista buscarArtistaPorNombre(String nombre) {
        Artista artista = null;

        try {
            cn = conexion.realizarconexion();
            String sql = "{call sp_buscar_artista(?)}";
            cs = cn.prepareCall(sql);
            cs.setString(1, nombre);

            rs = cs.executeQuery();

            if (rs.next()) {
                artista = new Artista();
                artista.setId(rs.getInt("id"));
                artista.setNombre(rs.getString("nombre"));
                artista.setGenero(rs.getString("genero"));
                artista.setDescripcion(rs.getString("descripcion"));
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

        return artista;
    }

}
