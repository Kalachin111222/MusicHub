/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package presentacion;

import entidades.Artista;

/**
 *
 * @author ArcosArce
 */
public class panArtista extends javax.swing.JPanel {

    /**
     * Creates new form panPlaylist
     */
    private FrmPrincipal parent;
    private Artista artistaActual;
    
    public panArtista(FrmPrincipal parent) {
        this.parent = parent;
        initComponents();
        configurarGrid(); // <--- AGREGA ESTO
    }
    
    public panArtista(FrmPrincipal parent, Artista artista) {
        this.parent = parent;
        this.artistaActual = artista; // Guardamos el objeto recibido
        initComponents();
        configurarGrid();
        
        if (artista != null) {
            cargarDatosArtista(artista);
        }
    }
    
    // =========================================================================
    //  LOGICA MANUAL (Copiar y Pegar debajo de "Variables declaration")
    // =========================================================================

    // Método que llamaremos al iniciar el panel para asegurar que el Grid funcione bien
    public void configurarGrid() {
        // --- CAMBIO CLAVE ---
        // Usamos FlowLayout (Alineado a la izquierda) en lugar de GridLayout.
        // Esto evita que las tarjetas se estiren como chicle.
        panGrid.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 20)); 
        
        panGrid.setBackground(new java.awt.Color(25, 25, 25));
    }

    // Este es el método que FrmPrincipal llamará cuando el usuario elija un artista
    public void cargarDatosArtista(entidades.Artista artista) {
        this.artistaActual = artista;
        // 1. Poner nombre del artista
        jLabel1.setText(artista.getNombre());
        
        // 2. Limpiar lo anterior
        panGrid.removeAll();

        try {
            java.util.List<entidades.Album> listaAlbumes = logica.BLLAlbum.listarAlbumesPorArtista(artista.getId());
            
            if (listaAlbumes == null) {
                listaAlbumes = new java.util.ArrayList<>();
            }

            System.out.println("Álbumes encontrados en BD para " + artista.getNombre() + ": " + listaAlbumes.size());

            for (entidades.Album alb : listaAlbumes) {
                agregarTarjetaAlbum(alb); 
            }

        } catch (Exception e) {
            System.out.println("Error al cargar álbumes del artista: " + e.getMessage());
            e.printStackTrace();
        }

        // 5. Refrescar visualmente
        panGrid.revalidate();
        panGrid.repaint();
    }
    
    
    // Método auxiliar para crear el diseño de cada "cuadradito" de álbum
    // Método auxiliar para crear el diseño de cada "cuadradito" de álbum
    private void agregarTarjetaAlbum(entidades.Album album) {
        // Panel contenedor de la tarjeta
        javax.swing.JPanel card = new javax.swing.JPanel();
        card.setLayout(new java.awt.BorderLayout());
        card.setBackground(new java.awt.Color(40, 40, 40));
        card.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(60, 60, 60)));

        // --- AQUÍ ESTÁ EL TRUCO PARA QUE NO CREZCAN ---
        // Definimos un tamaño fijo para la tarjeta
        java.awt.Dimension dimensionFija = new java.awt.Dimension(180, 240);
        card.setPreferredSize(dimensionFija);
        card.setMaximumSize(dimensionFija);
        card.setMinimumSize(dimensionFija);
        // ---------------------------------------------

        // 1. Label para la Imagen (con lógica de carga en hilo separado)
        javax.swing.JLabel lblImg = new javax.swing.JLabel("Cargando...");
        lblImg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImg.setForeground(java.awt.Color.GRAY);
        lblImg.setPreferredSize(new java.awt.Dimension(150, 150)); // Tamaño zona imagen

        // Lógica de descarga (Tu código corregido)
        new Thread(() -> {
            try {
                String urlString = album.getUrlImagen();
                if (urlString != null && !urlString.isEmpty()) {
                    // Fix Google Drive
                    if (urlString.contains("drive.google.com") && urlString.contains("/d/")) {
                         java.util.regex.Matcher m = java.util.regex.Pattern.compile("/d/([a-zA-Z0-9_-]+)").matcher(urlString);
                         if (m.find()) urlString = "https://drive.google.com/uc?export=download&id=" + m.group(1);
                    }
                    
                    java.net.URL url = new java.net.URL(urlString);
                    java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(url);

                    if (img != null) {
                        // Redimensionar imagen a 150x150
                        java.awt.Image dimg = img.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
                        javax.swing.ImageIcon icono = new javax.swing.ImageIcon(dimg);
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            lblImg.setText("");
                            lblImg.setIcon(icono);
                        });
                    }
                } else {
                    javax.swing.SwingUtilities.invokeLater(() -> lblImg.setText("Sin Img"));
                }
            } catch (Exception ex) {
                System.out.println("Error img: " + ex.getMessage());
            }
        }).start();

        // 2. Label Título (Cortar si es muy largo)
        String titulo = album.getTitulo();
        if (titulo.length() > 22) titulo = titulo.substring(0, 19) + "...";
        
        javax.swing.JLabel lblTitulo = new javax.swing.JLabel(titulo);
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setForeground(java.awt.Color.WHITE);
        lblTitulo.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
        lblTitulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 10, 5));

        card.add(lblImg, java.awt.BorderLayout.CENTER);
        card.add(lblTitulo, java.awt.BorderLayout.SOUTH);

        // Eventos Mouse
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                parent.mostrarPanel(new panDetalleAlbum(parent, album,artistaActual));
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new java.awt.Color(60, 60, 60));
                card.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(new java.awt.Color(40, 40, 40));
            }
        });

        panGrid.add(card);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        panGrid = new javax.swing.JPanel();

        setBackground(new java.awt.Color(25, 25, 25));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("[Nombre del Artista]");

        panGrid.setBackground(new java.awt.Color(25, 25, 25));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(panGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 918, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel1)
                .addGap(38, 38, 38)
                .addComponent(panGrid, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(163, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel panGrid;
    // End of variables declaration//GEN-END:variables
}
