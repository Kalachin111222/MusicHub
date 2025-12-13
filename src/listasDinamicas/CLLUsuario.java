/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package listasDinamicas;

import entidades.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para Usuario - Maneja SOLO la lógica de control y estado
 * NO realiza operaciones CRUD (eso lo harán los árboles AVL)
 */
public class CLLUsuario {
    // Singleton
    private static final CLLUsuario instancia = new CLLUsuario();
    
    private CLLUsuario() {}
    
    public static CLLUsuario getInstancia() {
        return instancia;
    }
    
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private Usuario usuarioActual; // Usuario logueado actualmente
    
    // ===== GESTIÓN DE SESIÓN =====
    
    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
    }
    
    public Usuario getUsuario() {
        return usuarioActual;
    }
    
    public boolean hayUsuarioLogueado() {
        return usuarioActual != null;
    }
    
    public void cerrarSesion() {
        this.usuarioActual = null;
        this.listaUsuarios.clear();
    }
    
    // GESTIÓN DE LISTAS (para resultados temporales)
    
    public void agregarALista(Usuario usuario) {
        if (!listaUsuarios.contains(usuario)) {
            listaUsuarios.add(usuario);
        }
    }
    
    public void limpiarLista() {
        listaUsuarios.clear();
    }
    
    public Usuario getUsuarioOfLista(int pos) {
        if (pos >= 0 && pos < listaUsuarios.size()) {
            return listaUsuarios.get(pos);
        }
        return null;
    }
    
    public int getTamanioLista() {
        return listaUsuarios.size();
    }
    
    public List<Usuario> getListaUsuarios() {
        return new ArrayList<>(listaUsuarios);
    }
}
