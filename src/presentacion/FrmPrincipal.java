/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion;

import estructuras.Pila;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import entidades.Cancion;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.imageio.ImageIO;

/**
 *
 * @author ArcosArce
 */
public class FrmPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form FrmMenu
     */
    
    private Pila<JPanel> historialPaneles;
    private JPanel panelActual;
    
    private GestorAudio gestorAudio; 
    private javax.swing.Timer timerProgreso;
    
    private panHome panHome;
    private panPlaylist panPlaylist;
    private panArtista panArtista;
    private panPerfil panPerfil;
    private static FrmPrincipal instanciaGlobal;


    public static FrmPrincipal getInstanciaGlobal() {
        return instanciaGlobal;
    }
    
    public FrmPrincipal() {
        initComponents();
        setLocationRelativeTo(null);
        
        inicializarComponentes();

        cargarPlaylistsLateral(); 

        cargarColaLateral(); 
        new javafx.embed.swing.JFXPanel();
        instanciaGlobal = this;
    }
    
    private void configurarUsuarioActual() {
        if (logica.BLLUsuario.hayUsuarioLogueado()) {
            entidades.Usuario usuario = logica.BLLUsuario.getUsuarioActual();
            
            
            System.out.println("Usuario logueado: " + usuario.getNombre());
        } else {
            System.out.println("Modo Invitado");
        }
    }
    
    private void inicializarComponentes() {
        gestorAudio = new GestorAudio();

        timerProgreso = new javax.swing.Timer(500, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                actualizarBarraGUI();
            }
        });
        
        historialPaneles = new estructuras.Pila<>();
        
        scpPlaylists.setBorder(null);
        scpPlaylists.getVerticalScrollBar().setUnitIncrement(16);
        scpPlaylists.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        scpColaHistorial.setBorder(null);
        scpColaHistorial.getVerticalScrollBar().setUnitIncrement(16);
        scpColaHistorial.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jScrollPane1.setPreferredSize(new Dimension(755, 513));
        jScrollPane1.setMinimumSize(new Dimension(755, 513));
        jScrollPane1.setBorder(null);
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panHome = new panHome(this);
        panPlaylist = new panPlaylist(this);
        panArtista = new panArtista(this);
        panPerfil = new panPerfil(this);

        configurarUsuarioActual();

        if (logica.BLLUsuario.hayUsuarioLogueado()) {
            int idUsuario = logica.BLLUsuario.getUsuarioActual().getId();
            
            String generoFav = logica.BLLHistorialReproduccion.obtenerGeneroFavorito(idUsuario);
            String generoBusqueda = (generoFav != null) ? generoFav : "Pop"; 
            
            java.util.List<entidades.Cancion> listaRecomendadas = 
                    logica.BLLCancion.listarCancionesPorGeneroNoEscuchadas(idUsuario, generoBusqueda);
            
            estructuras.ListaCircularDoble<entidades.Cancion> circularRecomendadas = new estructuras.ListaCircularDoble<>();
            if (listaRecomendadas != null) {
                for (entidades.Cancion c : listaRecomendadas) circularRecomendadas.insertar(c);
            }
            panHome.setListaRecomendadas(circularRecomendadas);

            java.util.List<entidades.Cancion> listaPopulares = logica.BLLCancion.listarCancionesMasPopulares();
            
            estructuras.ListaCircularDoble<entidades.Cancion> circularPopulares = new estructuras.ListaCircularDoble<>();
            if (listaPopulares != null) {
                for (entidades.Cancion c : listaPopulares) circularPopulares.insertar(c);
            }
            panHome.setListaPopulares(circularPopulares);
            
        }

        mostrarPanel(panHome);

        MouseAdapter controlBarra = new MouseAdapter() {
            private void saltarAPosicion(MouseEvent e) {
                if (gestorAudio == null || gestorAudio.getDuracionTotal() <= 0) return;
                
                int mouseX = e.getX();
                int anchoBarra = pgbProgreso.getWidth();
                
                if (anchoBarra > 0) {
                    double porcentaje = (double) mouseX / anchoBarra;
                    double duracionTotal = gestorAudio.getDuracionTotal();
                    double segundoDestino = porcentaje * duracionTotal;
                    
                    gestorAudio.saltarA(segundoDestino);
                    pgbProgreso.setValue((int) segundoDestino);
                    lblTmpActual.setText(obtenerTiempoFormateado(segundoDestino));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) { saltarAPosicion(e); }
            
            @Override
            public void mouseDragged(MouseEvent e) { saltarAPosicion(e); }
        };

        pgbProgreso.addMouseListener(controlBarra);
        pgbProgreso.addMouseMotionListener(controlBarra);
    }
    
    private void cargarImagenPortada(String url, javax.swing.JLabel label) {
        if (url == null || url.isEmpty()) return;

        new Thread(() -> {
            try {
                // 1. Descargar imagen
                java.net.URL linkImagen = new java.net.URL(url);
                java.awt.image.BufferedImage imagenOriginal = ImageIO.read(linkImagen);
                
                if (imagenOriginal != null) {
                    int ancho = label.getWidth() > 0 ? label.getWidth() : 150;
                    int alto = label.getHeight() > 0 ? label.getHeight() : 150;
                    
                    Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                    
                    SwingUtilities.invokeLater(() -> {
                        label.setIcon(new ImageIcon(imagenEscalada));
                    });
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error cargando imagen: " + url);
            }
        }).start();
    }
    
    public void reproducirCancionActual() {
        // 1. Obtener la canción desde la lógica
        Cancion c = listasDinamicas.CLLReproductor.getInstancia().getActual();
        
        if (c == null) return; 

        // 2. Actualizar textos
        lblTitulo.setText(c.getTitulo());
        
        if (c.getAlbum() != null && c.getAlbum().getArtista() != null) {
            lblArtista.setText(c.getAlbum().getArtista().getNombre());
        } else {
            lblArtista.setText("Artista Desconocido");
        }
        
        // 3. ACTUALIZAR CARÁTULA (lblImagenCancion) - LÓGICA CORREGIDA
        // Ponemos un placeholder o limpiamos mientras carga
        lblImagenCancion.setIcon(null); 
        lblImagenCancion.setText("..."); 

        if (c.getAlbum() != null) {
            int idAlbum = c.getAlbum().getId();
            
            // Usamos un HILO para no congelar la app al descargar
            new Thread(() -> {
                try {
                    // A. Pedimos la URL a la BD (Tu método estático arreglado)
                    String urlBD = logica.BLLAlbum.obtenerUrlImagenAlbum(idAlbum);
                    
                    // B. Si es link de Google Drive, lo convertimos (Lógica rápida inline)
                    String urlFinal = urlBD;
                    if (urlBD != null && urlBD.contains("drive.google.com") && urlBD.contains("/d/")) {
                         java.util.regex.Matcher m = java.util.regex.Pattern.compile("/d/([a-zA-Z0-9_-]+)").matcher(urlBD);
                         if (m.find()) {
                             urlFinal = "https://drive.google.com/uc?export=download&id=" + m.group(1);
                         }
                    }

                    if (urlFinal != null && !urlFinal.isEmpty()) {
                        java.net.URL url = new java.net.URL(urlFinal);
                        java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(url);
                        
                        if (img != null) {
                            // Redimensionamos al tamaño de tu label (ej. 60x60)
                            java.awt.Image dimg = img.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
                            javax.swing.ImageIcon icono = new javax.swing.ImageIcon(dimg);
                            
                            // C. Actualizamos la UI
                            javax.swing.SwingUtilities.invokeLater(() -> {
                                lblImagenCancion.setText(""); // Quitamos texto
                                lblImagenCancion.setIcon(icono); // Ponemos foto
                            });
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error cargando mini carátula: " + e.getMessage());
                }
            }).start();
        }

        // 4. Reproducir Audio
        if (gestorAudio != null) {
            gestorAudio.reproducir(c.getUrlAudio());
            btnPlayPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pausa.png"))); 
            
            if (timerProgreso != null) timerProgreso.start();
        }
    }

    private void actualizarBarraGUI() {
        if (gestorAudio != null && gestorAudio.getDuracionTotal() > 0) {
            double actual = gestorAudio.getTiempoActual();
            double total = gestorAudio.getDuracionTotal();

            pgbProgreso.setMaximum((int) total);
            pgbProgreso.setValue((int) actual);

            lblTmpActual.setText(obtenerTiempoFormateado(actual));
            lblDuracion.setText(obtenerTiempoFormateado(total));
        }
    }
    
    public void mostrarPanel(JPanel panelNuevo) {
        if (panelActual != null && panelActual != panelNuevo) {
            historialPaneles.push(panelActual);
        }
        
        cambiarPanelVisualmente(panelNuevo);
    }

    private void cambiarPanelVisualmente(JPanel panel) {
        panContenido.removeAll();
        panContenido.setLayout(new java.awt.BorderLayout());
        
        panContenido.add(panel, java.awt.BorderLayout.CENTER);

        panContenido.revalidate();
        panContenido.repaint();

        SwingUtilities.invokeLater(() -> {
            jScrollPane1.getVerticalScrollBar().setValue(0);
        });

        panelActual = panel;
    }
    
    public void volverAtras() {
        if (!historialPaneles.isEmpty()) {
            JPanel panelAnterior = historialPaneles.pop();
            
            cambiarPanelVisualmente(panelAnterior);
        } else {
            System.out.println("No hay nada atrás en el historial");
        }
    }
    
    public void cargarPlaylistsLateral() {
        // 1. Configuración inicial
        panListaPlaylists.removeAll();
        panListaPlaylists.setLayout(new BoxLayout(panListaPlaylists, BoxLayout.Y_AXIS));
        panListaPlaylists.setBackground(new Color(18, 18, 18));

        if (logica.BLLUsuario.hayUsuarioLogueado()) {
            int idUsuario = logica.BLLUsuario.getUsuarioActual().getId();
            
             java.util.List<entidades.Playlist> misPlaylists = logica.BLLPlaylist.obtenerPlaylistsUsuario(idUsuario);
            
            
            if (misPlaylists.isEmpty()) {
            } else {
                for (entidades.Playlist pl : misPlaylists) {
                   JPanel item = crearItemPlaylist(pl.getNombre());
                   panListaPlaylists.add(item);
                   panListaPlaylists.add(Box.createRigidArea(new Dimension(0, 2)));
                }
            }
        }
        panListaPlaylists.revalidate();
        panListaPlaylists.repaint();
    }

    private JPanel crearItemPlaylist(String nombre) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.setMaximumSize(new Dimension(220, 45));
        
        panel.setPreferredSize(new Dimension(190, 45));

        panel.setBackground(new Color(18,18,18));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcono = new JLabel("♫");
        lblIcono.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lblIcono.setForeground(new Color(179, 179, 179));

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setForeground(new Color(200, 200, 200));
        lblNombre.setFont(new java.awt.Font("Segoe UI", 0, 13));

        panel.add(lblIcono);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(lblNombre);
        
        panel.add(Box.createHorizontalGlue());

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(40, 40, 40));
                lblNombre.setForeground(Color.WHITE);
                lblIcono.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(18, 18, 18));
                lblNombre.setForeground(new Color(200, 200, 200));
                lblIcono.setForeground(new Color(179, 179, 179));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Abrir Playlist: " + nombre);
                // Aquí iría la lógica para abrir la playlist
                // mostrarPanel(new panPlaylist(nombre));
            }
        });

        return panel;
    }
    
    public void cargarColaLateral() {
        panContenido2.removeAll();
        panContenido2.setLayout(new BoxLayout(panContenido2, BoxLayout.Y_AXIS));
        panContenido2.setBackground(new Color(18, 18, 18));

        java.util.List<Cancion> cola = listasDinamicas.CLLReproductor.getInstancia().getColaActual();

        if (cola != null && !cola.isEmpty()) {
            
            for (Cancion c : cola) {
                String nombreArtista = "Desconocido";
                if (c.getAlbum() != null && c.getAlbum().getArtista() != null) {
                    nombreArtista = c.getAlbum().getArtista().getNombre();
                }

                JPanel item = crearItemCola(c.getTitulo(), nombreArtista);
                
                panContenido2.add(item);
                
                panContenido2.add(Box.createRigidArea(new Dimension(0, 1)));
            }
        } else {
             JLabel lblVacio = new JLabel("Cola vacía");
             lblVacio.setForeground(Color.GRAY);
             panContenido2.add(lblVacio);
        }

        // 4. Refrescar visualmente
        panContenido2.revalidate();
        panContenido2.repaint();
    }

    private JPanel crearItemCola(String titulo, String artista) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        panel.setMaximumSize(new Dimension(220, 55));
        
        panel.setPreferredSize(new Dimension(200, 55));

        panel.setBackground(new Color(18, 18, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcon = new JLabel("♫");
        lblIcon.setForeground(new Color(100, 100, 100));
        lblIcon.setFont(new java.awt.Font("Segoe UI", 0, 14));

        JPanel panTextos = new JPanel();
        panTextos.setLayout(new BoxLayout(panTextos, BoxLayout.Y_AXIS));
        panTextos.setOpaque(false);
        panTextos.setAlignmentY(java.awt.Component.CENTER_ALIGNMENT); 

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new java.awt.Font("Segoe UI", 0, 13));

        JLabel lblArtista = new JLabel(artista);
        lblArtista.setForeground(new Color(150, 150, 150)); 
        lblArtista.setFont(new java.awt.Font("Segoe UI", 0, 11)); 

        panTextos.add(lblTitulo);
        panTextos.add(lblArtista);

        JLabel lblDuracion = new JLabel("3:45");
        lblDuracion.setForeground(new Color(100, 100, 100));
        lblDuracion.setFont(new java.awt.Font("Segoe UI", 0, 11));

        panel.add(lblIcon);
        panel.add(Box.createRigidArea(new Dimension(10, 0))); 
        panel.add(panTextos);
        panel.add(Box.createHorizontalGlue()); 
        panel.add(lblDuracion);
        panel.add(Box.createRigidArea(new Dimension(5, 0))); 

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(40, 40, 40));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(18, 18, 18));
            }
        });

        return panel;
    }
    
    public void cargarHistorialLateral() {
        panContenido2.removeAll();
        panContenido2.setLayout(new BoxLayout(panContenido2, BoxLayout.Y_AXIS));
        panContenido2.setBackground(new Color(18, 18, 18));

         java.util.List<Cancion> historial = logica.BLLHistorialReproduccion.obtenerCancionesRecientes();
        
        
        panContenido2.revalidate();
        panContenido2.repaint();
    }
    
    private String obtenerTiempoFormateado(double segundos) {
        int totalSegundos = (int) segundos;
        int minutos = totalSegundos / 60;
        int segs = totalSegundos % 60;
        return String.format("%02d:%02d", minutos, segs);
    }
    
    public void mostrarDetalleAlbum(entidades.Album album) {
        // Creamos el panel de detalle pasándole 'this' (el principal) y el álbum
        panDetalleAlbum panelDetalle = new panDetalleAlbum(this, album);
        mostrarPanel(panelDetalle);
    }
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));
        panFondo = new javax.swing.JPanel();
        pgbProgreso = new ProgressBarPersonalizada();
        lblDuracion = new javax.swing.JLabel();
        lblTmpActual = new javax.swing.JLabel();
        btnPlayPausar = new BotonPersonalizado(new java.awt.Color(0,0,0), new java.awt.Color(50,50,50), new java.awt.Color(40,40,40));
        lblImagenCancion = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        lblArtista = new javax.swing.JLabel();
        btnAnterior = new BotonPersonalizado(new java.awt.Color(0,0,0), new java.awt.Color(50,50,50), new java.awt.Color(40,40,40));
        btnSiguiente = new BotonPersonalizado(new java.awt.Color(0,0,0), new java.awt.Color(50,50,50), new java.awt.Color(40,40,40));
        jPanel1 = new PanelPersonalizado();
        jButton11 = new BotonPersonalizado(new java.awt.Color(25,25,25));
        jTextField2 = new javax.swing.JTextField();
        scpPlaylists = new javax.swing.JScrollPane();
        panListaPlaylists = new javax.swing.JPanel();
        jPanel3 = new PanelPersonalizado();
        btnCola = new BotonPersonalizado(new java.awt.Color(25,25,25));
        btnHistorial = new BotonPersonalizado(new java.awt.Color(25,25,25));
        scpColaHistorial = new javax.swing.JScrollPane();
        panContenido2 = new javax.swing.JPanel();
        btnHome = new BotonPersonalizado(new java.awt.Color(0,0,0));
        jTextField1 = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        panContenido = new javax.swing.JPanel();
        btnVolver = new BotonPersonalizado(new java.awt.Color(0,0,0), new java.awt.Color(50,50,50), new java.awt.Color(40,40,40));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panFondo.setBackground(new java.awt.Color(0, 0, 0));

        pgbProgreso.setBackground(new java.awt.Color(204, 204, 204));

        lblDuracion.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        lblDuracion.setForeground(new java.awt.Color(255, 255, 255));
        lblDuracion.setText("00:00");

        lblTmpActual.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        lblTmpActual.setForeground(new java.awt.Color(255, 255, 255));
        lblTmpActual.setText("00:00");

        btnPlayPausar.setBackground(new java.awt.Color(0, 0, 0));
        btnPlayPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/play.png"))); // NOI18N
        btnPlayPausar.setPreferredSize(new java.awt.Dimension(30, 30));
        btnPlayPausar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayPausarActionPerformed(evt);
            }
        });

        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTitulo.setText("-- --");

        lblArtista.setForeground(new java.awt.Color(255, 255, 255));
        lblArtista.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblArtista.setText("-- --");

        btnAnterior.setBackground(new java.awt.Color(0, 0, 0));
        btnAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/anterior.png"))); // NOI18N
        btnAnterior.setPreferredSize(new java.awt.Dimension(30, 30));
        btnAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnteriorActionPerformed(evt);
            }
        });

        btnSiguiente.setBackground(new java.awt.Color(0, 0, 0));
        btnSiguiente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/siguiente.png"))); // NOI18N
        btnSiguiente.setPreferredSize(new java.awt.Dimension(30, 30));
        btnSiguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSiguienteActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(25, 25, 25));

        jButton11.setBackground(new java.awt.Color(40, 40, 40));
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar (1).png"))); // NOI18N

        jTextField2.setBackground(new java.awt.Color(25, 25, 25));
        jTextField2.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        scpPlaylists.setMaximumSize(new java.awt.Dimension(238, 32767));

        panListaPlaylists.setBackground(new java.awt.Color(25, 25, 25));
        panListaPlaylists.setLayout(new javax.swing.BoxLayout(panListaPlaylists, javax.swing.BoxLayout.LINE_AXIS));
        scpPlaylists.setViewportView(panListaPlaylists);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(scpPlaylists, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpPlaylists, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(25, 25, 25));

        btnCola.setBackground(new java.awt.Color(40, 40, 40));
        btnCola.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnCola.setForeground(new java.awt.Color(255, 255, 255));
        btnCola.setText("Cola");
        btnCola.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColaActionPerformed(evt);
            }
        });

        btnHistorial.setBackground(new java.awt.Color(40, 40, 40));
        btnHistorial.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnHistorial.setForeground(new java.awt.Color(255, 255, 255));
        btnHistorial.setText("Escuchado recientemente");
        btnHistorial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHistorialActionPerformed(evt);
            }
        });

        panContenido2.setBackground(new java.awt.Color(25, 25, 25));
        panContenido2.setLayout(new javax.swing.BoxLayout(panContenido2, javax.swing.BoxLayout.LINE_AXIS));
        scpColaHistorial.setViewportView(panContenido2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scpColaHistorial)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnCola, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(btnHistorial)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCola)
                    .addComponent(btnHistorial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scpColaHistorial, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnHome.setBackground(new java.awt.Color(0, 0, 0));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/hogar(2).png"))); // NOI18N
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        jTextField1.setBackground(new java.awt.Color(40, 40, 40));
        jTextField1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N

        btnBuscar.setBackground(new java.awt.Color(40, 40, 40));
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/buscar (1).png"))); // NOI18N

        jScrollPane1.setBackground(new java.awt.Color(40, 40, 40));
        jScrollPane1.setMaximumSize(new java.awt.Dimension(1016, 32767));

        panContenido.setBackground(new java.awt.Color(25, 25, 25));
        panContenido.setRequestFocusEnabled(false);

        javax.swing.GroupLayout panContenidoLayout = new javax.swing.GroupLayout(panContenido);
        panContenido.setLayout(panContenidoLayout);
        panContenidoLayout.setHorizontalGroup(
            panContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1014, Short.MAX_VALUE)
        );
        panContenidoLayout.setVerticalGroup(
            panContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 709, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panContenido);

        btnVolver.setBackground(new java.awt.Color(0, 0, 0));
        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/volver-arriba-removebg-preview.png"))); // NOI18N
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panFondoLayout = new javax.swing.GroupLayout(panFondo);
        panFondo.setLayout(panFondoLayout);
        panFondoLayout.setHorizontalGroup(
            panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFondoLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panFondoLayout.createSequentialGroup()
                        .addComponent(btnVolver)
                        .addGap(484, 484, 484)
                        .addComponent(btnHome)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscar)
                        .addGap(0, 0, 0)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panFondoLayout.createSequentialGroup()
                        .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panFondoLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblImagenCancion, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblArtista, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                                    .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(50, 50, 50)
                                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panFondoLayout.createSequentialGroup()
                                        .addGap(212, 212, 212)
                                        .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnPlayPausar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(panFondoLayout.createSequentialGroup()
                                            .addComponent(lblTmpActual)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblDuracion))
                                        .addComponent(pgbProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panFondoLayout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );
        panFondoLayout.setVerticalGroup(
            panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panFondoLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHome)
                        .addComponent(btnBuscar))
                    .addComponent(btnVolver))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panFondoLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panFondoLayout.createSequentialGroup()
                        .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panFondoLayout.createSequentialGroup()
                                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panFondoLayout.createSequentialGroup()
                                        .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnPlayPausar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(12, 12, 12)
                                        .addComponent(pgbProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panFondoLayout.createSequentialGroup()
                                        .addComponent(lblTitulo)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblArtista)))
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblTmpActual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblDuracion))
                                .addGap(49, 49, 49))
                            .addGroup(panFondoLayout.createSequentialGroup()
                                .addComponent(lblImagenCancion, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panFondo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnteriorActionPerformed
        listasDinamicas.CLLReproductor.getInstancia().obtenerAnterior();
        reproducirCancionActual();
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnColaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColaActionPerformed
        cargarColaLateral();
    }//GEN-LAST:event_btnColaActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        cargarHistorialLateral();
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void btnPlayPausarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayPausarActionPerformed
        if (gestorAudio == null) return;


        if (gestorAudio.estaReproduciendo()) {
            // --- CASO 1: ESTÁ SONANDO -> QUEREMOS PAUSAR ---
            gestorAudio.pausar();

            // Al pausar, mostramos el botón de "PLAY" (para que pueda volver a arrancar)
            // Asegúrate de que el nombre del archivo sea correcto (ej: play.png, play-chiquito.png, etc.)
            btnPlayPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/play.png"))); 

        } else {
            // --- CASO 2: ESTÁ PAUSADO -> QUEREMOS CONTINUAR ---
            gestorAudio.continuar();

            // Al reproducir, mostramos el botón de "PAUSA" (para que pueda detenerse)
            btnPlayPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pausa.png"))); 
        }
    }//GEN-LAST:event_btnPlayPausarActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        listasDinamicas.CLLReproductor.getInstancia().obtenerSiguiente();
        // 2. Reproducir y actualizar foto
        reproducirCancionActual();
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        volverAtras();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        mostrarPanel(panHome);
    }//GEN-LAST:event_btnHomeActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPrincipal().setVisible(true);
            }  
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCola;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnPlayPausar;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JButton btnVolver;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JButton jButton11;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lblArtista;
    private javax.swing.JLabel lblDuracion;
    private javax.swing.JLabel lblImagenCancion;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTmpActual;
    private javax.swing.JPanel panContenido;
    private javax.swing.JPanel panContenido2;
    private javax.swing.JPanel panFondo;
    private javax.swing.JPanel panListaPlaylists;
    private javax.swing.JProgressBar pgbProgreso;
    private javax.swing.JScrollPane scpColaHistorial;
    private javax.swing.JScrollPane scpPlaylists;
    // End of variables declaration//GEN-END:variables
}
