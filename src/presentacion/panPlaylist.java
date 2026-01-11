/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package presentacion;

import entidades.Album;
import entidades.Cancion;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 *
 * @author ArcosArce
 */
public class panPlaylist extends javax.swing.JPanel {

    /**
     * Creates new form panDetalleAlbum
     */
    private FrmPrincipal principal;
    private entidades.Playlist playlistActual; // <--- Cambiado de Album a Playlist
    private java.util.List<Cancion> listaCanciones;

    // Constructor para NetBeans
    public panPlaylist() {
        initComponents();
    }

    // CONSTRUCTOR CORRECTO: Se llama desde el mouseClicked del menú lateral
    public panPlaylist(FrmPrincipal principal, entidades.Playlist playlist) {
        this.principal = principal;
        this.playlistActual = playlist;
        
        initComponents(); 
        personalizarPanel(); 
        cargarDatosPlaylist(); // <--- Nuevo método
    }

    private void personalizarPanel() {
        panListaCanciones.setLayout(new BoxLayout(panListaCanciones, BoxLayout.Y_AXIS));
        btnPlay.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void cargarDatosPlaylist() {
        if (playlistActual == null) return;

        // 1. Configurar textos principales
        lblAlbum.setText(playlistActual.getNombre()); // Reutilizamos el JLabel lblAlbum para el nombre
        
        // Texto secundario (cantidad de canciones, fecha, etc.)
        // jLabel2 es el label pequeño que está debajo del título
        jLabel2.setText("Playlist de " + logica.BLLUsuario.getUsuarioActual().getNombre() + 
                         " • Creada: " + playlistActual.getFechaCreacion());

        // 2. Limpiar lista visual
        panListaCanciones.removeAll();

        try {
            // BUSCAR CANCIONES DE LA PLAYLIST EN LA BD
            // Usamos el método que ya tienes en BLLPlaylist
            this.listaCanciones = logica.BLLPlaylist.listarCancionesDePlaylist(playlistActual.getId());

            if (this.listaCanciones != null && !this.listaCanciones.isEmpty()) {
                int contador = 1;
                for (Cancion c : this.listaCanciones) {
                    agregarFilaCancion(contador++, c);
                }
            } else {
                JLabel lblVacio = new JLabel("Esta playlist aún no tiene canciones.");
                lblVacio.setForeground(Color.GRAY);
                lblVacio.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
                panListaCanciones.add(lblVacio);
            }

        } catch (Exception e) {
            System.err.println("Error cargando canciones de playlist: " + e.getMessage());
        }

        panListaCanciones.revalidate();
        panListaCanciones.repaint();
    }
    
    private void agregarFilaCancion(int numero, Cancion cancion) {
        // 1. Crear el contenedor de la fila
        JPanel fila = new JPanel();
        fila.setLayout(new BorderLayout());
        fila.setMaximumSize(new Dimension(2000, 45)); // Permite que se estire a lo ancho
        fila.setPreferredSize(new Dimension(0, 45));   // Altura fija de 45px

        // Mejora visual: Empezamos transparente
        fila.setOpaque(false); 
        fila.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 40, 40))); // Línea sutil abajo

        // 2. Texto Izquierda: "1. Título de la Canción"
        JLabel lblInfo = new JLabel("  " + numero + ".    " + cancion.getTitulo());
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // 3. Texto Derecha: "03:45" (Duración)
        JLabel lblDuracion = new JLabel(formatearTiempo(cancion.getDuracion()) + "    ");
        lblDuracion.setForeground(Color.GRAY);
        lblDuracion.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Agregar elementos al panel de la fila
        fila.add(lblInfo, BorderLayout.CENTER);
        fila.add(lblDuracion, BorderLayout.EAST);

        // 4. --- EVENTOS DEL MOUSE (Interactividad) ---
        fila.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Al entrar el mouse: Fondo gris oscuro y cursor de mano
                fila.setOpaque(true);
                fila.setBackground(new Color(45, 45, 45)); 
                fila.setCursor(new Cursor(Cursor.HAND_CURSOR));
                fila.repaint(); // Refrescar color
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Al salir el mouse: Volver a ser transparente
                fila.setOpaque(false);
                fila.repaint(); // Refrescar color
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    if (principal != null) {
                        // Creamos una lista que contiene ÚNICAMENTE esta canción
                        java.util.List<Cancion> listaSoloUna = new java.util.ArrayList<>();
                        listaSoloUna.add(cancion); 

                        // Enviamos la lista de una sola canción al reproductor
                        principal.reproducirDesdePanel(listaSoloUna, 0);
                    }
                }
            }
        });

        // 5. Agregar la fila terminada al panel principal de la lista
        panListaCanciones.add(fila);
    }
    
    private void reproducirAlbumCompleto() {
        if (principal != null && listaCanciones != null && !listaCanciones.isEmpty()) {
            // Enviamos la lista completa y empezamos por la canción 0 (la primera)
            principal.reproducirDesdePanel(listaCanciones, 0);
        }
    }

    private String formatearTiempo(double segundos) {
        int min = (int) segundos / 60;
        int seg = (int) segundos % 60;
        return String.format("%02d:%02d", min, seg);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblAlbum = new javax.swing.JLabel();
        btnPlay = new BotonPersonalizado(new java.awt.Color(25,25,25), new java.awt.Color(50,50,50), new java.awt.Color(40,40,40));
        panListaCanciones = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(25, 25, 25));

        lblAlbum.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        lblAlbum.setForeground(new java.awt.Color(255, 255, 255));
        lblAlbum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAlbum.setText("[Nombre Album]");

        btnPlay.setBackground(new java.awt.Color(25, 25, 25));
        btnPlay.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/play.png"))); // NOI18N
        btnPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayActionPerformed(evt);
            }
        });

        panListaCanciones.setBackground(new java.awt.Color(25, 25, 25));
        panListaCanciones.setLayout(new javax.swing.BoxLayout(panListaCanciones, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setFont(new java.awt.Font("Roboto", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("15 canciones · 45 min · Creada: 10/01/2026");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPlay)
                            .addComponent(panListaCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, 928, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 40, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, 1002, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(lblAlbum)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(40, 40, 40)
                .addComponent(btnPlay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panListaCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        reproducirAlbumCompleto();
    }//GEN-LAST:event_btnPlayActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPlay;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblAlbum;
    private javax.swing.JPanel panListaCanciones;
    // End of variables declaration//GEN-END:variables
}
