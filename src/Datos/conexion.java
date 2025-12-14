/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Asus
 */
public class conexion {
    public static Connection realizarconexion()throws ClassNotFoundException, SQLException{
        String url,user,password;
        Class.forName("com.mysql.cj.jdbc.Driver");
        url="jdbc:mysql://localhost:3306/music_hub";
        user="root";
        password="tucontraseña";
        
        System.out.println("Intentando conectar a: " + url);
        System.out.println("Usuario: " + user);
        System.out.println("Contraseña: " + password);
        return DriverManager.getConnection(url, user, password);
    }
}
