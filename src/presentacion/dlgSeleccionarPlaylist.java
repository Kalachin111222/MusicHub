/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author ArcosArce
 */

import entidades.Cancion;
import entidades.Playlist;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import logica.BLLPlaylist;

public class dlgSeleccionarPlaylist extends javax.swing.JDialog {

    private Cancion cancionAAgregar;

    public dlgSeleccionarPlaylist(java.awt.Frame parent, boolean modal, Cancion cancion) {
        super(parent, modal);
        this.cancionAAgregar = cancion;
        initComponents();
        configurarVentana();
        cargarPlaylists();
    }

    private void configurarVentana() {
        setTitle("Añadir a playlist");
        setSize(350, 450);
        setLocationRelativeTo(getParent());
        panContenedor.setLayout(new BoxLayout(panContenedor, BoxLayout.Y_AXIS));
        getContentPane().setBackground(new Color(25, 25, 25));
    }

    private void cargarPlaylists() {
        panContenedor.removeAll();
        // Obtenemos las playlists que ya están en memoria (CLL)
        List<Playlist> misPlaylists = BLLPlaylist.obtenerPlaylistsUsuario(listasDinamicas.CLLUsuario.getInstancia().getUsuario().getId());

        for (Playlist p : misPlaylists) {
            JButton btnP = new JButton(p.getNombre());
            btnP.setMaximumSize(new Dimension(350, 50));
            btnP.setForeground(Color.WHITE);
            btnP.setBackground(new Color(35, 35, 35));
            btnP.setFocusPainted(false);
            btnP.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 50)));
            btnP.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Evento al elegir la playlist
            btnP.addActionListener(e -> {
                boolean exito = BLLPlaylist.agregarCancionPlaylist(p.getId(), cancionAAgregar);
                if (exito) dispose(); // Cerramos el diálogo si se agregó bien
            });

            panContenedor.add(btnP);
        }
        panContenedor.revalidate();
        panContenedor.repaint();
    }

    // Código simplificado de los componentes (Asegúrate de que existan en el diseño)
    private void initComponents() {
        scpScroll = new javax.swing.JScrollPane();
        panContenedor = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBackground(new java.awt.Color(25, 25, 25));

        lblTitulo.setFont(new java.awt.Font("SansSerif", 1, 18)); 
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setText("Añadir a playlist");
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        panContenedor.setBackground(new java.awt.Color(25, 25, 25));
        scpScroll.setViewportView(panContenedor);
        scpScroll.setBorder(null);

        getContentPane().add(lblTitulo, java.awt.BorderLayout.NORTH);
        getContentPane().add(scpScroll, java.awt.BorderLayout.CENTER);
    }

    private javax.swing.JPanel panContenedor;
    private javax.swing.JScrollPane scpScroll;
    private javax.swing.JLabel lblTitulo;
}