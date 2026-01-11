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
public class panDetalleAlbum extends javax.swing.JPanel {

    /**
     * Creates new form panDetalleAlbum
     */
    private FrmPrincipal principal;
    private Album albumActual;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR 1: EL QUE USA NETBEANS (NO TOCAR)
    // -------------------------------------------------------------------------
    public panDetalleAlbum() {
        initComponents();
    }

    // -------------------------------------------------------------------------
    // CONSTRUCTOR 2: EL QUE USAMOS NOSOTROS (RECIBE DATOS)
    // -------------------------------------------------------------------------
    public panDetalleAlbum(FrmPrincipal principal, Album album) {
        this.principal = principal;
        this.albumActual = album;
        
        initComponents(); // 1. NetBeans dibuja el diseño base
        personalizarPanel(); // 2. Nosotros corregimos detalles (como el layout vertical)
        cargarDatosAlbum(); // 3. Llenamos los datos
    }

    // Método para arreglar cosas que el diseñador de NetBeans no hace bien
    private void personalizarPanel() {
        // CORRECCIÓN CLAVE: Cambiamos a Y_AXIS para que sea una lista VERTICAL
        // (NetBeans lo puso en LINE_AXIS/Horizontal por defecto)
        panListaCanciones.setLayout(new BoxLayout(panListaCanciones, BoxLayout.Y_AXIS));
        
        // Manito en el botón de Play
        btnPlay.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void cargarDatosAlbum() {
        if (albumActual == null) return;

        // Llenamos la cabecera
        lblAlbum.setText(albumActual.getTitulo()); // Asegúrate que en tu entidad Album sea getNombre o getTitulo
        
        // Si tu objeto Album tiene el artista enlazado:
        // lblArtista.setText(albumActual.getArtista().getNombre());
        
        // Limpiamos la lista visual anterior
        panListaCanciones.removeAll();

        // --- SIMULACIÓN DE CANCIONES ---
        // Generamos 10 falsas para probar el diseño (luego conectas la BD aquí)
        for (int i = 1; i <= 10; i++) {
            Cancion c = new Cancion();
            c.setTitulo("Canción " + i + " de " + albumActual.getTitulo());
            c.setDuracion(180 + (i * 10)); // Duración variable
            
            agregarFilaCancion(i, c);
        }
        
        // Refrescamos para que aparezcan
        panListaCanciones.revalidate();
        panListaCanciones.repaint();
    }

    private void agregarFilaCancion(int numero, Cancion cancion) {
        // Panel que representa UN RENGLÓN (una canción)
        JPanel fila = new JPanel();
        fila.setLayout(new BorderLayout());
        fila.setMaximumSize(new Dimension(2000, 45)); // Ancho infinito, Alto fijo 45px
        fila.setPreferredSize(new Dimension(0, 45));
        fila.setBackground(new Color(25, 25, 25)); // Mismo fondo
        fila.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 40, 40))); // Línea abajo

        // Texto Izquierda: "1. Titulo Cancion"
        JLabel lblInfo = new JLabel("  " + numero + ".    " + cancion.getTitulo());
        lblInfo.setForeground(Color.WHITE);
        lblInfo.setFont(new java.awt.Font("Roboto", java.awt.Font.PLAIN, 14));
        
        // Texto Derecha: "03:45"
        JLabel lblDuracion = new JLabel(formatearTiempo(cancion.getDuracion()) + "   ");
        lblDuracion.setForeground(Color.GRAY);
        
        fila.add(lblInfo, BorderLayout.CENTER);
        fila.add(lblDuracion, BorderLayout.EAST);

        // --- EVENTOS DEL MOUSE ---
        fila.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                fila.setBackground(new Color(50, 50, 50)); // Resaltar
                fila.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                fila.setBackground(new Color(25, 25, 25)); // Normal
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    System.out.println("Reproduciendo: " + cancion.getTitulo());
                    // AQUÍ CONECTARÁS CON EL GESTOR DE AUDIO:
                    // principal.reproducirCancion(cancion);
                }
            }
        });

        // Agregamos la fila al panel contenedor
        panListaCanciones.add(fila);
    }
    
    private void reproducirAlbumCompleto() {
        System.out.println("Reproduciendo álbum completo: " + albumActual.getTitulo());
        // Lógica para enviar todas las canciones al gestorAudio
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

        lblImagen = new javax.swing.JLabel();
        lblArtista = new javax.swing.JLabel();
        lblAlbum = new javax.swing.JLabel();
        btnPlay = new BotonPersonalizado(new java.awt.Color(25,25,25), new java.awt.Color(50,50,50), new java.awt.Color(40,40,40));
        panListaCanciones = new javax.swing.JPanel();

        setBackground(new java.awt.Color(25, 25, 25));

        lblImagen.setBackground(new java.awt.Color(25, 25, 25));

        lblArtista.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        lblArtista.setForeground(new java.awt.Color(255, 255, 255));
        lblArtista.setText("[Nombre Artista]");

        lblAlbum.setFont(new java.awt.Font("Roboto", 1, 36)); // NOI18N
        lblAlbum.setForeground(new java.awt.Color(255, 255, 255));
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
                            .addComponent(lblArtista, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPlay)
                            .addComponent(panListaCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, 928, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblAlbum)
                        .addGap(18, 18, 18)
                        .addComponent(lblArtista)
                        .addGap(38, 38, 38))
                    .addComponent(lblImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(btnPlay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panListaCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, 515, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(100, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("");
    }// </editor-fold>//GEN-END:initComponents

    private void btnPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayActionPerformed
        reproducirAlbumCompleto();
    }//GEN-LAST:event_btnPlayActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPlay;
    private javax.swing.JLabel lblAlbum;
    private javax.swing.JLabel lblArtista;
    private javax.swing.JLabel lblImagen;
    private javax.swing.JPanel panListaCanciones;
    // End of variables declaration//GEN-END:variables
}
