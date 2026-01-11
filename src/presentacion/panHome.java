/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package presentacion;

import entidades.Cancion;
import estructuras.ListaCircularDoble;
import estructuras.NodoCircularDoble;
import java.awt.Color;
import javax.swing.JPanel;
import logica.BLLAlbum;

/**
 *
 * @author ArcosArce
 */
public class panHome extends javax.swing.JPanel {

    /**
     * Creates new form panHome
     */
    private FrmPrincipal parent;
    
    // --- VARIABLES CARRUSEL 1 (RECOMENDADAS) ---
    private ListaCircularDoble<Cancion> listaRecomendadas;
    private NodoCircularDoble<Cancion> nodoRecomendadas; // Antes "nodoActual"
    
    // --- VARIABLES CARRUSEL 2 (POPULARES) ---
    private ListaCircularDoble<Cancion> listaPopulares;
    private NodoCircularDoble<Cancion> nodoPopulares; 
    
    // Configuración general
    private int cantidadTarjetasVisibles = 4; 
    
    public panHome(FrmPrincipal parent) {
        this.parent = parent;
        initComponents();
        
        // --------------------------------------------------
        // CONFIGURACIÓN VISUAL CARRUSEL 1 (RECOMENDADOS)
        // --------------------------------------------------
        // panGrid es la variable original de NetBeans para el de arriba
        panGrid.setLayout(new java.awt.GridLayout(1, cantidadTarjetasVisibles, 15, 0));
        panGrid.setOpaque(false);
        
        // Ajuste de márgenes (Gap) para botones
        if (panCarruselRecomendados.getLayout() instanceof java.awt.BorderLayout) {
             ((java.awt.BorderLayout) panCarruselRecomendados.getLayout()).setHgap(30);
        }

        // --------------------------------------------------
        // CONFIGURACIÓN VISUAL CARRUSEL 2 (POPULARES)
        // --------------------------------------------------
        // panGridPopulares es el nuevo panel para el de abajo
        panGridPopulares.setLayout(new java.awt.GridLayout(1, cantidadTarjetasVisibles, 15, 0));
        panGridPopulares.setOpaque(false);
        
        // Ajuste de márgenes (Gap) para botones de abajo
        if (panCarruselPopulares.getLayout() instanceof java.awt.BorderLayout) {
             ((java.awt.BorderLayout) panCarruselPopulares.getLayout()).setHgap(30);
        }
    }
    
    // ==========================================
    // LÓGICA RECOMENDADAS (ARRIBA)
    // ==========================================
    public void setListaRecomendadas(ListaCircularDoble<Cancion> lista) {
        this.listaRecomendadas = lista;
        if (!lista.esVacia()) {
            this.nodoRecomendadas = lista.getL(); 
        }
        actualizarRecomendadas();
    }
    
    private void actualizarRecomendadas() {
        panGrid.removeAll();

        if (listaRecomendadas == null || listaRecomendadas.esVacia()) return;

        NodoCircularDoble<Cancion> temp = nodoRecomendadas; 

        for (int i = 0; i < cantidadTarjetasVisibles; i++) {
            JPanel tarjeta = crearTarjetaCancion(temp.getInfo());
            panGrid.add(tarjeta);
            temp = temp.getSgte();
        }

        panGrid.revalidate();
        panGrid.repaint();
    }

    public void setListaPopulares(ListaCircularDoble<Cancion> lista) {
        this.listaPopulares = lista;
        if (!lista.esVacia()) {
            this.nodoPopulares = lista.getL(); 
        }
        actualizarPopulares();
    }
    
    private void actualizarPopulares() {
        panGridPopulares.removeAll(); // Limpiamos panel de abajo

        if (listaPopulares == null || listaPopulares.esVacia()) return;

        NodoCircularDoble<Cancion> temp = nodoPopulares; 

        for (int i = 0; i < cantidadTarjetasVisibles; i++) {
            JPanel tarjeta = crearTarjetaCancion(temp.getInfo()); // O getInfo()
            panGridPopulares.add(tarjeta);
            temp = temp.getSgte(); // O getSgte()
        }

        panGridPopulares.revalidate();
        panGridPopulares.repaint();
    }
    
    private JPanel crearTarjetaCancion(Cancion c) {
    // --- 1. Configuración Visual ---
    JPanel panel = new JPanel();
    panel.setLayout(new java.awt.BorderLayout());
    panel.setBackground(new Color(50,50,50));
    panel.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(40,40,40), 1));
    panel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    
    javax.swing.JLabel lblImagen = new javax.swing.JLabel();
    lblImagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblImagen.setPreferredSize(new java.awt.Dimension(140, 140));
    
    // Placeholder "♫"
    lblImagen.setText("♫");
    lblImagen.setForeground(new Color(100, 100, 100));
    lblImagen.setFont(new java.awt.Font("Segoe UI Emoji", 0, 48));
    
    // --- 2. Cargar imagen del álbum ---
    if (c.getAlbum() != null) {
        int idAlbum = c.getAlbum().getId();
        
        new Thread(() -> {
            try {
                String urlDeLaBD = logica.BLLAlbum.obtenerUrlImagenAlbum(idAlbum);
                
                if (urlDeLaBD != null && !urlDeLaBD.isEmpty()) {
                    // Descargamos la imagen directamente
                    java.net.URL url = new java.net.URL(urlDeLaBD);
                    java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(url);
                    
                    if (img != null) {
                        java.awt.Image dimg = img.getScaledInstance(140, 140, java.awt.Image.SCALE_SMOOTH);
                        javax.swing.ImageIcon icono = new javax.swing.ImageIcon(dimg);
                        
                        // Actualizamos la UI
                        javax.swing.SwingUtilities.invokeLater(() -> {
                            lblImagen.setText("");
                            lblImagen.setIcon(icono);
                        });
                    }
                }
            } catch (Exception e) {
                System.out.println("No se pudo cargar imagen para album ID: " + idAlbum);
                e.printStackTrace();
            }
        }).start();
    }
    
    // --- 3. Resto de la tarjeta ---
    javax.swing.JLabel lblTitulo = new javax.swing.JLabel(c.getTitulo()); 
    lblTitulo.setForeground(Color.WHITE);
    lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblTitulo.setFont(new java.awt.Font("Roboto", 1, 12));
    lblTitulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 2, 5, 2));
    
    panel.add(lblImagen, java.awt.BorderLayout.CENTER);
    panel.add(lblTitulo, java.awt.BorderLayout.SOUTH);
    
    panel.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            System.out.println("Reproduciendo: " + c.getTitulo());
            java.util.List<Cancion> aReproducir = new java.util.ArrayList<>();
            aReproducir.add(c);
            listasDinamicas.CLLReproductor.getInstancia().setNuevaCola(aReproducir, 0);
            if (parent != null) parent.reproducirCancionActual();
        }
        
        @Override
        public void mouseEntered(java.awt.event.MouseEvent evt) { 
            panel.setBackground(new Color(70, 70, 70)); 
        }
        
        @Override
        public void mouseExited(java.awt.event.MouseEvent evt) { 
            panel.setBackground(new Color(50, 50, 50)); 
        }
    });
    
    return panel;
}
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblGenero = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblReproducciones = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblUltimasHoras = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        panCarruselRecomendados = new PanelPersonalizado();
        btnAntRecomendados = new BotonPersonalizado(new java.awt.Color(30,30,30));
        btnSigRecomendados = new BotonPersonalizado(new java.awt.Color(30,30,30));
        panGrid = new javax.swing.JPanel();
        panCarruselPopulares = new PanelPersonalizado();
        btnAntPopulares = new BotonPersonalizado(new java.awt.Color(30,30,30));
        btnSigPopulares = new BotonPersonalizado(new java.awt.Color(30,30,30));
        panGridPopulares = new javax.swing.JPanel();

        setBackground(new java.awt.Color(25, 25, 25));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/hola-blanco-chiquito.png"))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tus estadisticas");

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Bienvenido, [Nombre Usuario]");

        jPanel1.setBackground(new java.awt.Color(30, 30, 30));

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Genero favorito :");

        lblGenero.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        lblGenero.setForeground(new java.awt.Color(255, 255, 255));
        lblGenero.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                    .addComponent(lblGenero, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(lblGenero)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(30, 30, 30));

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Total reproducciones");

        lblReproducciones.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        lblReproducciones.setForeground(new java.awt.Color(255, 255, 255));
        lblReproducciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblReproducciones, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(lblReproducciones)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(30, 30, 30));

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Ultimas 24h :");

        lblUltimasHoras.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        lblUltimasHoras.setForeground(new java.awt.Color(255, 255, 255));
        lblUltimasHoras.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblUltimasHoras, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(lblUltimasHoras)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Recomendadas para ti");

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Canciones más populares");

        panCarruselRecomendados.setBackground(new java.awt.Color(25, 25, 25));
        panCarruselRecomendados.setLayout(new java.awt.BorderLayout());

        btnAntRecomendados.setBackground(new java.awt.Color(40, 40, 40));
        btnAntRecomendados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/atras-chiquito.png"))); // NOI18N
        btnAntRecomendados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAntRecomendadosActionPerformed(evt);
            }
        });
        panCarruselRecomendados.add(btnAntRecomendados, java.awt.BorderLayout.LINE_START);

        btnSigRecomendados.setBackground(new java.awt.Color(30, 30, 30));
        btnSigRecomendados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/adelante-chiquito.png"))); // NOI18N
        btnSigRecomendados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSigRecomendadosActionPerformed(evt);
            }
        });
        panCarruselRecomendados.add(btnSigRecomendados, java.awt.BorderLayout.LINE_END);

        panGrid.setBackground(new java.awt.Color(25, 25, 25));
        panGrid.setLayout(new java.awt.GridLayout(1, 0));
        panCarruselRecomendados.add(panGrid, java.awt.BorderLayout.CENTER);

        panCarruselPopulares.setBackground(new java.awt.Color(25, 25, 25));
        panCarruselPopulares.setLayout(new java.awt.BorderLayout());

        btnAntPopulares.setBackground(new java.awt.Color(40, 40, 40));
        btnAntPopulares.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/atras-chiquito.png"))); // NOI18N
        btnAntPopulares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAntPopularesActionPerformed(evt);
            }
        });
        panCarruselPopulares.add(btnAntPopulares, java.awt.BorderLayout.LINE_START);

        btnSigPopulares.setBackground(new java.awt.Color(30, 30, 30));
        btnSigPopulares.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/adelante-chiquito.png"))); // NOI18N
        btnSigPopulares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSigPopularesActionPerformed(evt);
            }
        });
        panCarruselPopulares.add(btnSigPopulares, java.awt.BorderLayout.LINE_END);

        panGridPopulares.setBackground(new java.awt.Color(25, 25, 25));
        panGridPopulares.setLayout(new java.awt.GridLayout(1, 0));
        panCarruselPopulares.add(panGridPopulares, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4)
                    .addComponent(jLabel1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3))
                    .addComponent(panCarruselRecomendados, javax.swing.GroupLayout.DEFAULT_SIZE, 973, Short.MAX_VALUE)
                    .addComponent(jLabel8)
                    .addComponent(panCarruselPopulares, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panCarruselRecomendados, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panCarruselPopulares, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(120, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSigRecomendadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSigRecomendadosActionPerformed
        if (nodoRecomendadas != null) {
            nodoRecomendadas = nodoRecomendadas.getAnt();
            actualizarRecomendadas();
        }
    }//GEN-LAST:event_btnSigRecomendadosActionPerformed

    private void btnAntRecomendadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAntRecomendadosActionPerformed
        if (nodoRecomendadas != null) {
            nodoRecomendadas = nodoRecomendadas.getSgte();
            actualizarRecomendadas();
        }
    }//GEN-LAST:event_btnAntRecomendadosActionPerformed

    private void btnAntPopularesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAntPopularesActionPerformed
        if (nodoPopulares != null) {
            nodoPopulares = nodoPopulares.getAnt();
            actualizarPopulares();
        }
    }//GEN-LAST:event_btnAntPopularesActionPerformed

    private void btnSigPopularesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSigPopularesActionPerformed
        if (nodoPopulares != null) {
            nodoPopulares = nodoPopulares.getSgte();
            actualizarPopulares();
        }
    }//GEN-LAST:event_btnSigPopularesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAntPopulares;
    private javax.swing.JButton btnAntRecomendados;
    private javax.swing.JButton btnSigPopulares;
    private javax.swing.JButton btnSigRecomendados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel lblGenero;
    private javax.swing.JLabel lblReproducciones;
    private javax.swing.JLabel lblUltimasHoras;
    private javax.swing.JPanel panCarruselPopulares;
    private javax.swing.JPanel panCarruselRecomendados;
    private javax.swing.JPanel panGrid;
    private javax.swing.JPanel panGridPopulares;
    // End of variables declaration//GEN-END:variables
}
