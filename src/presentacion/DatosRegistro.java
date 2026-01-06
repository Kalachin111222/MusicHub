/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author ArcosArce
 */
import java.time.LocalDate;

public class DatosRegistro {
    // Paso Inicial
    private String email;
    
    // Paso 1
    private String password;
    private String confirmarPassword;
    
    // Paso 2
    private String nombre;
    private LocalDate fechaNacimiento;
    private String genero;
    
    // Getters y Setters
    public String getEmail(){
        return email; 
    }
    public void setEmail(String email){
        this.email = email; 
    }
    
    public String getPassword(){
        return password; 
    }
    public void setPassword(String password){
        this.password = password; 
    }
    
    public String getConfirmarPassword(){ 
        return confirmarPassword; 
    }
    public void setConfirmarPassword(String confirmarPassword){
        this.confirmarPassword = confirmarPassword; 
    }
    
    public String getNombre(){
        return nombre;
    }
    public void setNombre(String nombre){ this.nombre = nombre;
    }
    
    public LocalDate getFechaNacimiento(){
        return fechaNacimiento; 
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { 
        this.fechaNacimiento = fechaNacimiento; 
    }
    
    public String getGenero(){
        return genero; 
    }
    public void setGenero(String genero){
        this.genero = genero; 
    }
    
}