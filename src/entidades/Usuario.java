/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades;

import java.time.LocalDate;

/**
 *
 * @author chris
 */
public class Usuario {
    
    private int id;
    private String nombre;
    private String email;
    private String contraseña;
    private LocalDate fechaRegistro;
    private LocalDate fechaDeNacimiento;
    private String genero;

    public Usuario() {
    }

    public Usuario(String nombre, String email, String contraseña, LocalDate fechaRegistro,LocalDate fechaDeNacimiento, String genero) {
        this.id = 0;
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.fechaRegistro = fechaRegistro;
        this.fechaDeNacimiento = fechaDeNacimiento; 
        this.genero = genero;

    }

    public Usuario(int id, String nombre, String email, String contraseña, LocalDate fechaDeNacimiento,String genero) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.fechaRegistro = fechaRegistro;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.genero = genero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(LocalDate fechadDeNacimiento) {
        this.fechaDeNacimiento = fechadDeNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    public boolean validarEmail() {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    public boolean esNuevo() {
        return id == 0;
    }
}
