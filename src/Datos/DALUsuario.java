/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import entidades.*;
import java.sql.*;
/**
 *
 * @author Asus
 */
public class DALUsuario {
    
    private static Connection cn = null;
    private static CallableStatement cs = null;
    private static Statement st = null;
    private static ResultSet rs = null;
    public static String insertarUsuario(Usuario u){
        String mensaje =null;
        
        try{
            cn=conexion.realizarconexion();
            String sql="{call sp_insertar_usuario(?,?,?,?)}";
            cs=cn.prepareCall(sql);
            
            cs.setString(1, u.getNombre());
            cs.setString(2, u.getEmail());
            cs.setString(3, u.getContraseña());
            cs.setDate(4, Date.valueOf(u.getFechaRegistro()));
            
            cs.executeUpdate();
        }catch(ClassNotFoundException | SQLException ex){
            mensaje=ex.getMessage();
        }finally{
          // 5️⃣ Cerrar recursos
            try {
                if (cs != null) cs.close();
                if (cn != null) cn.close();
            } catch (SQLException ex) {
                mensaje = ex.getMessage();
            }
        }
  
        return mensaje;
    };


    public static String actualizarUsuario(Usuario u){
      String mensaje = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_actualizar_usuario(?, ?, ?, ?)}";
        cs = cn.prepareCall(sql);

        cs.setInt(1, u.getId());
        cs.setString(2, u.getNombre());
        cs.setString(3, u.getEmail());
        cs.setString(4, u.getContraseña());

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
    };

    public static String eliminarUsuario(int id){
       String mensaje = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_eliminar_usuario(?)}";
        cs = cn.prepareCall(sql);

        cs.setInt(1, id);

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
    };

    public static Usuario obtenerUsuarioPorId(int id){
        Usuario usuario = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_obtener_usuario_por_id(?)}";
        cs = cn.prepareCall(sql);
        cs.setInt(1, id);

        rs = cs.executeQuery();

        if (rs.next()) {
            usuario = new Usuario();
            usuario.setId(rs.getInt("id"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setEmail(rs.getString("email"));
            usuario.setContraseña(rs.getString("contrasena"));
            usuario.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate());
        }

    } catch (ClassNotFoundException | SQLException ex) {
        System.out.println("Error DAL: " + ex.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
            if (cn != null) cn.close();
        } catch (SQLException ex) {
            System.out.println("Error al cerrar recursos: " + ex.getMessage());
        }
    }

    return usuario;
    };

    public static Usuario obtenerUsuarioPorEmail(String email){
     Usuario usuario = null;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_obtener_usuario_por_email(?)}";
        cs = cn.prepareCall(sql);
        cs.setString(1, email);

        rs = cs.executeQuery();

        if (rs.next()) {
            usuario = new Usuario();
            usuario.setId(rs.getInt("id"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setEmail(rs.getString("email"));
            usuario.setContraseña(rs.getString("contrasena"));
            usuario.setFechaRegistro(rs.getDate("fecha_registro").toLocalDate());
        }

    } catch (ClassNotFoundException | SQLException ex) {
        System.out.println("Error DAL: " + ex.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
            if (cn != null) cn.close();
        } catch (SQLException ex) {
            System.out.println("Error al cerrar recursos: " + ex.getMessage());
        }
    }

    return usuario;   
    };

    public static boolean existeEmail(String email){
     boolean existe = false;

    try {
        cn = conexion.realizarconexion();

        String sql = "{call sp_existe_email(?)}";
        cs = cn.prepareCall(sql);
        cs.setString(1, email);

        rs = cs.executeQuery();

        if (rs.next()) {
            existe = rs.getInt("total") > 0;
        }

    } catch (ClassNotFoundException | SQLException ex) {
        System.out.println("Error DAL: " + ex.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (cs != null) cs.close();
            if (cn != null) cn.close();
        } catch (SQLException ex) {
            System.out.println("Error al cerrar recursos: " + ex.getMessage());
        }
    }

    return existe;   
    };


}
