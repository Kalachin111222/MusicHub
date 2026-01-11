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
    // AGREGA ESTA LÍNEA:
    private java.util.List<Cancion> listaCanciones;
    private entidades.Artista artistaActual;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR 1: EL QUE USA NETBEANS (NO TOCAR)
    // -------------------------------------------------------------------------
    public panDetalleAlbum() {
        initComponents();
    }

    // -------------------------------------------------------------------------
    // CONSTRUCTOR 2: EL QUE USAMOS NOSOTROS (RECIBE DATOS)
    // -------------------------------------------------------------------------
    public panDetalleAlbum(FrmPrincipal principal, Album album, entidades.Artista artista) {
        this.principal = principal;
        this.albumActual = album;
        this.artistaActual = artista; // <--- Guardamos el artista recibido
        
        initComponents(); 
        personalizarPanel(); 
        cargarDatosAlbum(); 
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
        // 1. Validamos que exista álbum
        if (albumActual == null) return;

        // ---------------------------------------------------------
        // 2. CONFIGURAR TEXTOS (TÍTULO Y ARTISTA)
        // ---------------------------------------------------------
        lblAlbum.setText(albumActual.getTitulo());

        // Aquí usamos la variable 'artistaActual' que llenamos en el Constructor
        if (artistaActual != null) {
            lblArtista.setText(artistaActual.getNombre());
        } else {
            lblArtista.setText("Artista Desconocido");
        }

        // ---------------------------------------------------------
        // 3. CARGAR IMAGEN DEL ÁLBUM (En un hilo separado para no congelar)
        // ---------------------------------------------------------
        new Thread(() -> {
            try {
                String urlString = albumActual.getUrlImagen();
                
                // Corrección para enlaces de Google Drive
                if (urlString != null && !urlString.isEmpty()) {
                    if (urlString.contains("drive.google.com") && urlString.contains("/d/")) {
                        java.util.regex.Matcher m = java.util.regex.Pattern.compile("/d/([a-zA-Z0-9_-]+)").matcher(urlString);
                        if (m.find()) {
                            urlString = "https://drive.google.com/uc?export=download&id=" + m.group(1);
                        }
                    }

                    java.net.URL url = new java.net.URL(urlString);
                    java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(url);

                    if (img != null) {
                        // Redimensionamos a 200x200
                        java.awt.Image dimg = img.getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
                        javax.swing.ImageIcon icono = new javax.swing.ImageIcon(dimg);

                        // Actualizamos la interfaz
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            lblImagen.setText("");
                            lblImagen.setIcon(icono);
                        });
                    }
                }
            } catch (Exception e) {
                System.out.println("Error cargando carátula detalle: " + e.getMessage());
            }
        }).start();

        // ---------------------------------------------------------
        // 4. CARGAR LISTA DE CANCIONES DESDE LA BASE DE DATOS
        // ---------------------------------------------------------
        panListaCanciones.removeAll(); // Limpiamos la lista visual anterior

        try {
            // Buscamos las canciones de este álbum en la BD
            this.listaCanciones = logica.BLLCancion.listarCancionesPorAlbum(albumActual.getId());

            if (this.listaCanciones != null && !this.listaCanciones.isEmpty()) {
                
                // --- AQUÍ ESTÁ EL TRUCO: COMPLETAR LOS DATOS FALTANTES ---
                // Recorremos las canciones recuperadas para 'inyectarles' el Álbum y el Artista
                // Así, cuando las reproduzcas, el reproductor tendrá toda la info.
                
                for (Cancion c : this.listaCanciones) {
                    // 1. Le asignamos el objeto Album actual
                    c.setAlbum(this.albumActual);
                    
                    // 2. Le asignamos el objeto Artista actual
                    if (this.artistaActual != null) {
                        c.setArtista(this.artistaActual); 
                        c.setNombreArtista(this.artistaActual.getNombre());
                    }
                }

                // --- DIBUJAR LAS FILAS EN PANTALLA ---
                int contador = 1;
                for (Cancion c : this.listaCanciones) {
                    agregarFilaCancion(contador++, c);
                }
                
            } else {
                // Si no hay canciones, mostramos un mensaje
                javax.swing.JLabel lblVacio = new javax.swing.JLabel("No hay canciones disponibles.");
                lblVacio.setForeground(java.awt.Color.GRAY);
                lblVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
                panListaCanciones.add(lblVacio);
            }

        } catch (Exception e) {
            System.out.println("Error trayendo canciones: " + e.getMessage());
            e.printStackTrace();
        }

        // Refrescar panel visualmente
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
