/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package presentacion;

import entidades.Cancion;
import estructuras.ListaCircularDoble;
import estructuras.NodoCircularDoble;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
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
    private int cantidadTarjetasVisibles = 5; 
    
        // Caché para la cola lateral (pequeñas)
    private java.util.Map<Integer, javax.swing.Icon> cacheColaLateral = new java.util.HashMap<>();

    // Caché para las tarjetas (medianas) - ESTA ES LA QUE USA TU MÉTODO ACTUAL
    private java.util.Map<Integer, javax.swing.Icon> cacheImagenes = new java.util.HashMap<>();
    
    public panHome(FrmPrincipal parent) {

        this.parent = parent;
        initComponents();


        panGrid.setLayout(new java.awt.GridLayout(1, cantidadTarjetasVisibles, 15, 0));
        panGrid.setOpaque(false);


        if (panCarruselRecomendados.getLayout() instanceof java.awt.BorderLayout) {
            ((java.awt.BorderLayout) panCarruselRecomendados.getLayout()).setHgap(30);

        }

        panGridPopulares.setLayout(new java.awt.GridLayout(1, cantidadTarjetasVisibles, 15, 0));
        panGridPopulares.setOpaque(false);


        if (panCarruselPopulares.getLayout() instanceof java.awt.BorderLayout) {
            ((java.awt.BorderLayout) panCarruselPopulares.getLayout()).setHgap(30);
        }

        cargarDatosUsuario();
        
        setSize(1014, 709);

    }
    
    // MËTODO NUEVO PARA CARGAR ESTADÍSTICAS
    public void cargarDatosUsuario() {
        if (logica.BLLUsuario.hayUsuarioLogueado()) {
            entidades.Usuario usuario = logica.BLLUsuario.getUsuarioActual();
            int idUsuario = usuario.getId();

            // 1. Nombre del Usuario
            String nombreCompleto = "Bienvenido, " + usuario.getNombre();
            lblNombreUsuario.setText(nombreCompleto);
            truncarTexto(lblNombreUsuario, 380); // APLICAR TRUNCAMIENTO

            // 2. Género Favorito
            String genero = logica.BLLHistorialReproduccion.obtenerGeneroFavorito(idUsuario);
            String generoTexto = (genero != null) ? genero : "Sin datos";
            lblGeneroFavorito.setText(generoTexto);
            truncarTexto(lblGeneroFavorito, 280); // APLICAR TRUNCAMIENTO

            // 3. Total Reproducciones
            int total = logica.BLLHistorialReproduccion.contarReproducciones(idUsuario);
            lblReproducciones.setText(String.valueOf(total));
            truncarTexto(lblReproducciones, 280); // Por si acaso el número es muy grande

            // 4. Últimas 24h
            java.util.List<entidades.HistorialReproduccion> recientes = 
                    logica.BLLHistorialReproduccion.listarUltimasReproducciones(idUsuario, 20);

            int cantidadReciente = (recientes != null) ? recientes.size() : 0;
            String textoReciente = cantidadReciente + " canciones";
            lblCanciones24h.setText(textoReciente);
            truncarTexto(lblCanciones24h, 280); // APLICAR TRUNCAMIENTO

        } else {
            // Caso Invitado
            lblNombreUsuario.setText("Bienvenido, Invitado");
            truncarTexto(lblNombreUsuario, 380);

            lblGeneroFavorito.setText("-");
            lblReproducciones.setText("0");
            lblCanciones24h.setText("-");
        }
    }
    
    private void truncarTexto(javax.swing.JLabel label, int anchoMax) {
        String textoOriginal = label.getText();

        // Evitar procesar texto vacío o que ya tenga tooltip
        if (textoOriginal == null || textoOriginal.isEmpty()) return;

        java.awt.FontMetrics fm = label.getFontMetrics(label.getFont());
        int anchoTexto = fm.stringWidth(textoOriginal);

        if (anchoTexto > anchoMax) {
            String truncado = textoOriginal;
            String puntosSuspensivos = "...";

            // Reducir caracteres hasta que quepa
            while (fm.stringWidth(truncado + puntosSuspensivos) > anchoMax && truncado.length() > 0) {
                truncado = truncado.substring(0, truncado.length() - 1);
            }

            // Guardar el texto completo en el tooltip
            label.setToolTipText(textoOriginal);
            label.setText(truncado + puntosSuspensivos);
        } else {
            // Si cabe completo, no necesita tooltip
            label.setToolTipText(null);
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
    
   private JPanel crearTarjetaCancion(entidades.Cancion c) {
        // --- 1. Configuración Visual ---
        JPanel panel = new PanelPersonalizado(); // Tu clase con bordes redondeados
        panel.setLayout(new java.awt.BorderLayout());
        panel.setBackground(java.awt.Color.BLACK); 
        panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 25, 25), 1));
        panel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panel.setPreferredSize(new java.awt.Dimension(140, 170)); 

        javax.swing.JLabel lblImagen = new javax.swing.JLabel();
        lblImagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImagen.setPreferredSize(new java.awt.Dimension(120, 120));

        // Placeholder estilizado
        lblImagen.setText("♫");
        lblImagen.setForeground(new java.awt.Color(60, 60, 60));
        lblImagen.setFont(new java.awt.Font("Segoe UI Emoji", 0, 48));

        // --- 2. Lógica de Imagen con Caché específica de Tarjetas ---
        if (c.getAlbum() != null) {
            int idAlbum = c.getAlbum().getId();

            // Usamos cacheImagenes (específica para 120x120)
            if (cacheImagenes != null && cacheImagenes.containsKey(idAlbum)) {
                lblImagen.setText("");
                lblImagen.setIcon(cacheImagenes.get(idAlbum));
            } else {
                new Thread(() -> {
                    try {
                        String urlBD = logica.BLLAlbum.obtenerUrlImagenAlbum(idAlbum);

                        // Conversión de URL Google Drive
                        if (urlBD != null && urlBD.contains("drive.google.com")) {
                            java.util.regex.Matcher m = java.util.regex.Pattern.compile("/d/([a-zA-Z0-9_-]+)").matcher(urlBD);
                            if (m.find()) urlBD = "https://drive.google.com/uc?export=download&id=" + m.group(1);
                        }

                        if (urlBD != null && !urlBD.isEmpty()) {
                            java.net.URL url = new java.net.URL(urlBD);
                            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(url);
                            if (img != null) {
                                java.awt.Image dimg = img.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
                                javax.swing.ImageIcon icono = new javax.swing.ImageIcon(dimg);

                                if (cacheImagenes != null) cacheImagenes.put(idAlbum, icono); 

                                javax.swing.SwingUtilities.invokeLater(() -> {
                                    lblImagen.setText("");
                                    lblImagen.setIcon(icono);
                                });
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error en tarjeta ID: " + idAlbum);
                    }
                }).start();
            }
        }

        // --- 3. Título de la Canción ---
        javax.swing.JLabel lblTitulo = new javax.swing.JLabel(c.getTitulo()); 
        lblTitulo.setForeground(java.awt.Color.WHITE);
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitulo.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
        lblTitulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 8, 5));

        panel.add(lblImagen, java.awt.BorderLayout.CENTER);
        panel.add(lblTitulo, java.awt.BorderLayout.SOUTH);

        // --- 4. Eventos de Mouse ---
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                java.util.ArrayList<entidades.Cancion> listaSimple = new java.util.ArrayList<>();
                listaSimple.add(c);

                if (parent != null) {
                    // Se envía la canción individual al reproductor
                    parent.reproducirDesdePanel(listaSimple, 0);
                }
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) { 
                panel.setBackground(new java.awt.Color(35, 35, 35)); 
                panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100), 1));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) { 
                panel.setBackground(java.awt.Color.BLACK); 
                panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 25, 25), 1));
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
        lblNombreUsuario = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        lblGeneroFavorito = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblReproducciones = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblCanciones24h = new javax.swing.JLabel();
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
        setMaximumSize(new java.awt.Dimension(1014, 32767));
        setName(""); // NOI18N
        setLayout(null);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/hola-blanco-chiquito.png"))); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(20, 34, 24, 24);

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tus estadisticas");
        add(jLabel1);
        jLabel1.setBounds(20, 76, 108, 17);

        lblNombreUsuario.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        lblNombreUsuario.setForeground(new java.awt.Color(255, 255, 255));
        lblNombreUsuario.setText("Bienvenido, [Nombre Usuario]");
        add(lblNombreUsuario);
        lblNombreUsuario.setBounds(56, 36, 938, 22);

        jPanel1.setBackground(new java.awt.Color(30, 30, 30));

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Genero favorito :");

        lblGeneroFavorito.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        lblGeneroFavorito.setForeground(new java.awt.Color(255, 255, 255));
        lblGeneroFavorito.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Total reproducciones");

        lblReproducciones.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        lblReproducciones.setForeground(new java.awt.Color(255, 255, 255));
        lblReproducciones.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Ultimas 24h :");

        lblCanciones24h.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        lblCanciones24h.setForeground(new java.awt.Color(255, 255, 255));
        lblCanciones24h.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(lblGeneroFavorito, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(lblReproducciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(lblCanciones24h, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReproducciones, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGeneroFavorito, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCanciones24h, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        add(jPanel1);
        jPanel1.setBounds(70, 113, 864, 88);

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Recomendadas para ti");
        add(jLabel4);
        jLabel4.setBounds(20, 219, 148, 17);

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Canciones más populares");
        add(jLabel8);
        jLabel8.setBounds(20, 408, 169, 17);

        panCarruselRecomendados.setBackground(new java.awt.Color(0, 0, 0));
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

        panGrid.setBackground(new java.awt.Color(0, 0, 0));
        panGrid.setLayout(new java.awt.GridLayout(1, 0));
        panCarruselRecomendados.add(panGrid, java.awt.BorderLayout.CENTER);

        add(panCarruselRecomendados);
        panCarruselRecomendados.setBounds(20, 248, 974, 140);

        panCarruselPopulares.setBackground(new java.awt.Color(0, 0, 0));
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

        panGridPopulares.setBackground(new java.awt.Color(0, 0, 0));
        panGridPopulares.setLayout(new java.awt.GridLayout(1, 0));
        panCarruselPopulares.add(panGridPopulares, java.awt.BorderLayout.CENTER);

        add(panCarruselPopulares);
        panCarruselPopulares.setBounds(20, 437, 974, 140);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSigRecomendadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSigRecomendadosActionPerformed
        if (nodoRecomendadas != null) {
            // CORREGIDO: Siguiente llama a Siguiente
            nodoRecomendadas = nodoRecomendadas.getSgte(); 
            actualizarRecomendadas();
        }
    }//GEN-LAST:event_btnSigRecomendadosActionPerformed

    private void btnAntRecomendadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAntRecomendadosActionPerformed
        if (nodoRecomendadas != null) {
            // CORREGIDO: Anterior llama a Anterior
            nodoRecomendadas = nodoRecomendadas.getAnt();
            actualizarRecomendadas();
        }
    }//GEN-LAST:event_btnAntRecomendadosActionPerformed

    private void btnAntPopularesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAntPopularesActionPerformed
        if (nodoPopulares != null) {
            // CORREGIDO: Anterior llama a Anterior
            nodoPopulares = nodoPopulares.getAnt();
            actualizarPopulares();
        }
    }//GEN-LAST:event_btnAntPopularesActionPerformed

    private void btnSigPopularesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSigPopularesActionPerformed
        if (nodoPopulares != null) {
            // CORREGIDO: Siguiente llama a Siguiente
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCanciones24h;
    private javax.swing.JLabel lblGeneroFavorito;
    private javax.swing.JLabel lblNombreUsuario;
    private javax.swing.JLabel lblReproducciones;
    private javax.swing.JPanel panCarruselPopulares;
    private javax.swing.JPanel panCarruselRecomendados;
    private javax.swing.JPanel panGrid;
    private javax.swing.JPanel panGridPopulares;
    // End of variables declaration//GEN-END:variables
}
