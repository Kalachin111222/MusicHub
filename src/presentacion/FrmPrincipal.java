/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion;

import entidades.Artista;
import estructuras.Pila;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import entidades.Cancion;
import estructuras.ListaCircularDoble;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.imageio.ImageIO;
import listasDinamicas.CLLReproductor;

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
    private java.util.Map<Integer, javax.swing.Icon> cacheColaLateral = new java.util.HashMap<>();
    private double volumenAnterior = 0.5;
    private boolean modoRepetirUna = false; 


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
        panPerfil = new panPerfil(instanciaGlobal);

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
        
        lblArtista.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        lblArtista.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                lblArtista.setForeground(new java.awt.Color(29, 185, 84)); 
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                lblArtista.setForeground(java.awt.Color.WHITE); 
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Cancion cancionActual = listasDinamicas.CLLReproductor.getInstancia().getActual();

                if (cancionActual != null && cancionActual.getArtista() != null) {
                    Artista artistaSeleccionado = cancionActual.getArtista();
                    System.out.println("Navegando al perfil de: " + artistaSeleccionado.getNombre());

                    panArtista panelArtista = new panArtista(FrmPrincipal.this, artistaSeleccionado);
                    mostrarPanel(panelArtista); 

                } else {
                    String nombreEnLabel = lblArtista.getText();
                    if (!nombreEnLabel.equals("Artista Desconocido") && !nombreEnLabel.equals("...")) {
                          Artista ar = Logica.BLLArtista.obtenerDatosArtista(nombreEnLabel);
                          if (ar != null) mostrarPanel(new panArtista(instanciaGlobal, ar));
                    }
                }
            }
        });
        
        pgbVolumen.setMinimum(0);
        pgbVolumen.setMaximum(100);
        pgbVolumen.setValue(50);

        MouseAdapter controlVolumen = new MouseAdapter() {
            private void cambiarVolumen(MouseEvent e) {
                int mouseX = e.getX();
                int anchoBarra = pgbVolumen.getWidth();

                if (anchoBarra > 0) {
                    int nuevoValor = (int) ((double) mouseX / anchoBarra * 100);
                    nuevoValor = Math.max(0, Math.min(100, nuevoValor)); // Limitar entre 0-100

                    pgbVolumen.setValue(nuevoValor);

                    // Aplicar volumen al reproductor
                    if (gestorAudio != null) {
                        gestorAudio.setVolumen(nuevoValor / 100.0);
                    }

                    // Cambiar icono según el valor
                    if (nuevoValor == 0) {
                        btnVolumen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/muteado-chiquito.png")));
                    } else {
                        btnVolumen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/volumen-chiquito.png")));
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) { cambiarVolumen(e); }

            @Override
            public void mouseDragged(MouseEvent e) { cambiarVolumen(e); }
        };

        pgbVolumen.addMouseListener(controlVolumen);
        pgbVolumen.addMouseMotionListener(controlVolumen);
    }
    
    private void cargarImagenPortada(String url, javax.swing.JLabel label) {
        if (url == null || url.isEmpty()) return;

        new Thread(() -> {
            try {
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
        entidades.Cancion c = listasDinamicas.CLLReproductor.getInstancia().getActual();

        if (c == null) return; 

        lblTitulo.setText(c.getTitulo());
        lblArtista.setText((c.getNombreArtista() != null) ? c.getNombreArtista() : "Artista Desconocido");

        lblImagenCancion.setIcon(null); 
        lblImagenCancion.setText("..."); 

        if (c.getAlbum() != null) {
            int idAlbum = c.getAlbum().getId();
            if (cacheColaLateral != null && cacheColaLateral.containsKey(idAlbum)) {
                lblImagenCancion.setText("");
                lblImagenCancion.setIcon(cacheColaLateral.get(idAlbum));
            } else {
                new Thread(() -> {
                    try {
                        String urlBD = logica.BLLAlbum.obtenerUrlImagenAlbum(idAlbum);
                        if (urlBD != null && urlBD.contains("drive.google.com")) {
                            java.util.regex.Matcher m = java.util.regex.Pattern.compile("/d/([a-zA-Z0-9_-]+)").matcher(urlBD);
                            if (m.find()) urlBD = "https://drive.google.com/uc?export=download&id=" + m.group(1);
                        }

                        if (urlBD != null && !urlBD.isEmpty()) {
                            java.net.URL url = new java.net.URL(urlBD);
                            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(url);
                            if (img != null) {
                                java.awt.Image dimg = img.getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH);
                                javax.swing.ImageIcon icono = new javax.swing.ImageIcon(dimg);
                                javax.swing.SwingUtilities.invokeLater(() -> {
                                    lblImagenCancion.setText(""); 
                                    lblImagenCancion.setIcon(icono); 
                                });
                            }
                        }
                    } catch (Exception e) { 
                        System.err.println("Error carátula: " + e.getMessage());
                    }
                }).start();
            }
        }

        if (gestorAudio != null) {
            if (logica.BLLUsuario.hayUsuarioLogueado()) {
                int idUsuario = logica.BLLUsuario.getUsuarioActual().getId();
                int idCancion = c.getId();
                new Thread(() -> {
                    logica.BLLHistorialReproduccion.registrarReproduccion(idUsuario, idCancion);
                    actualizarEstadisticasHome();
                }).start();
            }

            gestorAudio.reproducir(c.getUrlAudio());
            btnPlayPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pausa.png"))); 
            if (timerProgreso != null) timerProgreso.start();
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            cargarColaLateral(true); 
        });
    }
    
    
    
    
//    public void reproducirDesdePanel(java.util.List<entidades.Cancion> lista, int indice) {
//        listasDinamicas.CLLReproductor gestor = listasDinamicas.CLLReproductor.getInstancia();
//
//        if (lista.size() == 1) {
//            gestor.setNuevaCola(lista, 0);
//            System.out.println("Reproduciendo canción individual seleccionada.");
//        } 
//        else if (gestor.getActual() != null) {
//            for (int i = indice; i < lista.size(); i++) {
//                entidades.Cancion nueva = lista.get(i);
//                // Evitamos duplicar la que ya está sonando en este instante
//                if (nueva.getId() != gestor.getActual().getId()) {
//                    gestor.insertar(nueva);
//                }
//            }
//            System.out.println("Lista agregada a la cola de reproducción.");
//        } 
//        else {
//            gestor.setNuevaCola(lista, indice);
//            System.out.println("Nueva lista de reproducción iniciada.");
//        }
//
//        reproducirCancionActual(); 
//
//        javax.swing.SwingUtilities.invokeLater(() -> {
//            cargarColaLateral(true);
//        });
//    }
    public void reproducirDesdePanel(java.util.List<entidades.Cancion> lista, int indice) {
        listasDinamicas.CLLReproductor gestor = listasDinamicas.CLLReproductor.getInstancia();

        // Validación
        if (lista == null || indice < 0 || indice >= lista.size()) {
            System.err.println("Error: Índice fuera de rango o lista nula.");
            return;
        }

        entidades.Cancion seleccionada = lista.get(indice);
        
        entidades.Cancion actual = gestor.getActual();

        if (actual == null) {
            // CASO 1: No hay nada sonando → Crear nueva cola
            java.util.List<entidades.Cancion> listaUnica = new java.util.ArrayList<>();
            listaUnica.add(seleccionada);
            gestor.setNuevaCola(listaUnica, 0);
            System.out.println("Nueva cola creada: " + seleccionada.getTitulo());

        } else {
            gestor.insertar(seleccionada);
            System.out.println("Agregada a la cola: " + seleccionada.getTitulo());

            // Actualizar solo la vista de la cola lateral
            javax.swing.SwingUtilities.invokeLater(() -> {
                cargarColaLateral(false); // false = no subir scroll
            });
            return; // IMPORTANTE: No reproducir inmediatamente
        }

        // Solo llegamos aquí si creamos una nueva cola
        reproducirCancionActual(); 
        javax.swing.SwingUtilities.invokeLater(() -> {
            cargarColaLateral(true); 
        });
    }
    
    public void reproducirListaCompleta(java.util.List<entidades.Cancion> lista, int indiceInicio) {
        listasDinamicas.CLLReproductor gestor = listasDinamicas.CLLReproductor.getInstancia();

        // Validación
        if (lista == null || lista.isEmpty()) {
            System.err.println("Error: Lista vacía.");
            return;
        }

        if (indiceInicio < 0 || indiceInicio >= lista.size()) {
            indiceInicio = 0;
        }

        entidades.Cancion actual = gestor.getActual();

        if (actual == null) {
            gestor.setNuevaCola(lista, indiceInicio);
            System.out.println("✓ Nueva cola creada con " + lista.size() + " canciones");

            reproducirCancionActual();

            javax.swing.SwingUtilities.invokeLater(() -> {
                cargarColaLateral(true);
            });
            return;
        }

        int agregadas = 0;

        for (entidades.Cancion c : lista) {
            gestor.insertar(c);
            agregadas++;
        }

        System.out.println("✓ Agregadas " + agregadas + " canciones a la cola");

        javax.swing.SwingUtilities.invokeLater(() -> {
            cargarColaLateral(false);
        });
    }
    
    
    
    
    
//    private void actualizarBarraGUI() {
//        if (gestorAudio != null && gestorAudio.getDuracionTotal() > 0) {
//            double actual = gestorAudio.getTiempoActual();
//            double total = gestorAudio.getDuracionTotal();
//
//            pgbProgreso.setMaximum((int) total);
//            pgbProgreso.setValue((int) actual);
//
//            lblTmpActual.setText(obtenerTiempoFormateado(actual));
//            lblDuracion.setText(obtenerTiempoFormateado(total));
//        }
//    }
    
    private void actualizarBarraGUI() {
        if (gestorAudio != null && gestorAudio.getDuracionTotal() > 0) {
            double actual = gestorAudio.getTiempoActual();
            double total = gestorAudio.getDuracionTotal();

            pgbProgreso.setMaximum((int) total);
            pgbProgreso.setValue((int) actual);
            lblTmpActual.setText(obtenerTiempoFormateado(actual));
            lblDuracion.setText(obtenerTiempoFormateado(total));

            // VALIDACIÓN DE FIN DE CANCIÓN
            if (actual >= total - 0.5 && actual > 0) {
                System.out.println("Fin de canción detectado.");

                if (modoRepetirUna) {
                    // MODO BUCLE: Reiniciar la misma canción
                    System.out.println("🔁 Repitiendo canción...");
                    gestorAudio.saltarA(0); // Volver al inicio
                } else {
                    // MODO NORMAL: Avanzar a la siguiente
                    btnSiguienteActionPerformed(null);
                }
            }
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
        panListaPlaylists.removeAll();
        panListaPlaylists.setLayout(new BoxLayout(panListaPlaylists, BoxLayout.Y_AXIS));
        panListaPlaylists.setBackground(new Color(18, 18, 18));

        if (logica.BLLUsuario.hayUsuarioLogueado()) {
            int idUsuario = logica.BLLUsuario.getUsuarioActual().getId();

            java.util.List<entidades.Playlist> misPlaylists = logica.BLLPlaylist.obtenerPlaylistsUsuario(idUsuario);

            if (misPlaylists != null) {
                for (entidades.Playlist pl : misPlaylists) {
                    JPanel item = crearItemPlaylist(pl.getNombre(), false);
                    panListaPlaylists.add(item);
                    panListaPlaylists.add(Box.createRigidArea(new Dimension(0, 2)));
                }
            }

            JPanel btnCrear = crearItemPlaylist("Crear nueva playlist", true);
            panListaPlaylists.add(btnCrear);
        }

        panListaPlaylists.revalidate();
        panListaPlaylists.repaint();
    }

    private JPanel crearItemPlaylist(String nombre, boolean esParaCrear) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        // Configuración de estilo
        panel.setMaximumSize(new Dimension(220, 45));
        panel.setPreferredSize(new Dimension(190, 45));
        panel.setBackground(new Color(18, 18, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblIcono = new JLabel(esParaCrear ? "+" : "♫");
        lblIcono.setFont(new java.awt.Font("Segoe UI", 1, esParaCrear ? 18 : 14));
        lblIcono.setForeground(esParaCrear ? new Color(29, 185, 84) : new Color(179, 179, 179));

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setForeground(esParaCrear ? new Color(29, 185, 84) : new Color(200, 200, 200));
        lblNombre.setFont(new java.awt.Font("Segoe UI", esParaCrear ? java.awt.Font.BOLD : java.awt.Font.PLAIN, 13));

        panel.add(lblIcono);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(lblNombre);
        panel.add(Box.createHorizontalGlue());

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(40, 40, 40));
                if (!esParaCrear) {
                    lblNombre.setForeground(Color.WHITE);
                    lblIcono.setForeground(Color.WHITE);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(18, 18, 18));
                if (!esParaCrear) {
                    lblNombre.setForeground(new Color(200, 200, 200));
                    lblIcono.setForeground(new Color(179, 179, 179));
                } else {
                    lblNombre.setForeground(new Color(29, 185, 84));
                    lblIcono.setForeground(new Color(29, 185, 84));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (esParaCrear) {
                    String nuevoNombre = JOptionPane.showInputDialog(FrmPrincipal.this, 
                            "Nombre de la nueva playlist:", "Crear Playlist", JOptionPane.QUESTION_MESSAGE);

                    if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                        int idUsuario = logica.BLLUsuario.getUsuarioActual().getId();
                        boolean creado = logica.BLLPlaylist.crearNuevaPlaylist(nuevoNombre, idUsuario);

                        if (creado) {
                            configurarUsuarioActual(); 
                            cargarPlaylistsLateral();
                        }
                    }
                } else {
                    entidades.Playlist seleccionada = logica.BLLPlaylist.buscarPlaylistPorNombre(nombre);
                    if (seleccionada != null) {
                        mostrarPanel(new panPlaylist(FrmPrincipal.this, seleccionada));
                    }
                }
            }
        });

        return panel;
    }
    
    public void cargarColaLateral(boolean subirScroll) {
        panContenido2.removeAll();
        panContenido2.setLayout(new javax.swing.BoxLayout(panContenido2, javax.swing.BoxLayout.Y_AXIS));
        panContenido2.setBackground(new java.awt.Color(18, 18, 18));

        listasDinamicas.CLLReproductor gestor = listasDinamicas.CLLReproductor.getInstancia();
        entidades.Cancion cancionActual = gestor.getActual();
        java.util.List<entidades.Cancion> colaFutura = gestor.getColaFutura();

        if (cancionActual != null) {
            javax.swing.JLabel lblNow = new javax.swing.JLabel("  Sonando ahora");
            lblNow.setForeground(new java.awt.Color(180, 180, 180));
            lblNow.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
            lblNow.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 5, 5));
            panContenido2.add(lblNow);
            panContenido2.add(crearItemCola(cancionActual, true));
            panContenido2.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 15)));
        }

        if (colaFutura != null && !colaFutura.isEmpty()) {
            javax.swing.JLabel lblNext = new javax.swing.JLabel("  Siguiente en la cola");
            lblNext.setForeground(new java.awt.Color(180, 180, 180));
            lblNext.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 12));
            lblNext.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panContenido2.add(lblNext);

            for (entidades.Cancion c : colaFutura) {
                panContenido2.add(crearItemCola(c, false));
            }
        }
        if (cancionActual == null && (colaFutura == null || colaFutura.isEmpty())) {
            javax.swing.JLabel lblVacio = new javax.swing.JLabel("Cola vacía");
            lblVacio.setForeground(java.awt.Color.GRAY);
            lblVacio.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            panContenido2.add(javax.swing.Box.createVerticalGlue());
            panContenido2.add(lblVacio);
            panContenido2.add(javax.swing.Box.createVerticalGlue());
        }
        panContenido2.revalidate();
        panContenido2.repaint();

        if (subirScroll) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (scpColaHistorial != null) {
                    scpColaHistorial.getVerticalScrollBar().setValue(0);
                }
            });
        }
    }

    public void cargarColaLateral() {
        cargarColaLateral(true);
    }
    
    
    private JPanel crearItemCola(entidades.Cancion c, boolean esActual) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        
        panel.setMaximumSize(new Dimension(220, 60));
        panel.setPreferredSize(new Dimension(200, 60));
        
        panel.setBackground(new Color(18, 18, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(50, 50));
        lblImagen.setMaximumSize(new Dimension(50, 50));
        lblImagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        
        lblImagen.setText(esActual ? "▶" : "♫");
        lblImagen.setForeground(esActual ? new Color(29, 185, 84) : new Color(80, 80, 80));
        lblImagen.setFont(new java.awt.Font("Segoe UI", 0, 18));
        
        if (c.getAlbum() != null) {
            int idAlbum = c.getAlbum().getId();
            
            if (cacheColaLateral.containsKey(idAlbum)) {
                lblImagen.setText("");
                lblImagen.setIcon(cacheColaLateral.get(idAlbum));
            } else {
                new Thread(() -> {
                    try {
                        String urlBD = logica.BLLAlbum.obtenerUrlImagenAlbum(idAlbum);
                        if (urlBD != null && !urlBD.isEmpty()) {
                            if (urlBD.contains("drive.google.com") && urlBD.contains("/d/")) {
                                java.util.regex.Matcher m = java.util.regex.Pattern.compile("/d/([a-zA-Z0-9_-]+)").matcher(urlBD);
                                if (m.find()) urlBD = "https://drive.google.com/uc?export=download&id=" + m.group(1);
                            }
                            
                            java.net.URL url = new java.net.URL(urlBD);
                            java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(url);
                            
                            if (img != null) {
                                java.awt.Image dimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
                                javax.swing.ImageIcon iconoFinal = new javax.swing.ImageIcon(dimg);
                                
                                cacheColaLateral.put(idAlbum, iconoFinal);
                                
                                javax.swing.SwingUtilities.invokeLater(() -> {
                                    lblImagen.setText("");
                                    lblImagen.setIcon(iconoFinal);
                                });
                            }
                        }
                    } catch (Exception e) {
                    }
                }).start();
            }
        }

        JPanel panTextos = new JPanel();
        panTextos.setLayout(new BoxLayout(panTextos, BoxLayout.Y_AXIS));
        panTextos.setOpaque(false);
        panTextos.setAlignmentY(java.awt.Component.CENTER_ALIGNMENT); 
        
        Color colorTitulo = esActual ? new Color(29, 185, 84) : Color.WHITE;

        JLabel lblTitulo = new JLabel(c.getTitulo());
        lblTitulo.setForeground(colorTitulo);
        lblTitulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

        String nombreArt = (c.getNombreArtista() != null) ? c.getNombreArtista() : "Desconocido";
        JLabel lblArtista = new JLabel(nombreArt);
        lblArtista.setForeground(new Color(150, 150, 150)); 
        lblArtista.setFont(new java.awt.Font("Segoe UI", 0, 11)); 

        panTextos.add(lblTitulo);
        panTextos.add(lblArtista);

        String duracionTexto = obtenerTiempoFormateado(c.getDuracion()); 
        JLabel lblDuracion = new JLabel(duracionTexto);
        lblDuracion.setForeground(new Color(100, 100, 100));
        lblDuracion.setFont(new java.awt.Font("Segoe UI", 0, 11));

        panel.add(lblImagen);
        panel.add(Box.createRigidArea(new Dimension(10, 0)));
        panel.add(panTextos);
        panel.add(Box.createHorizontalGlue());
        panel.add(lblDuracion);
        panel.add(Box.createRigidArea(new Dimension(5, 0))); 

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!esActual) {
                    saltarACancionDeLaCola(c);
                }
            }
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
    
    private void saltarACancionDeLaCola(entidades.Cancion destino) {
        listasDinamicas.CLLReproductor gestor = listasDinamicas.CLLReproductor.getInstancia();

        if (gestor.getActual() != null && gestor.getActual().getId() == destino.getId()) {
            return;
        }
        boolean encontrada = false;

        while (gestor.obtenerSiguiente() != null) {
            if (gestor.getActual().getId() == destino.getId()) {
                encontrada = true;
                break;
            }
        }
        if (encontrada) {
            reproducirCancionActual();

            javax.swing.SwingUtilities.invokeLater(() -> {
                cargarColaLateral();
            });
        } else {
            System.out.println("No se encontró la canción en la cola de reproducción.");
        }
    }
    
    public void cargarHistorialLateral() {
        panContenido2.removeAll();
        panContenido2.setLayout(new javax.swing.BoxLayout(panContenido2, javax.swing.BoxLayout.Y_AXIS));
        panContenido2.setBackground(new java.awt.Color(18, 18, 18));

        if (logica.BLLUsuario.hayUsuarioLogueado()) {
            int idUsuario = logica.BLLUsuario.getUsuarioActual().getId();

            java.util.List<entidades.HistorialReproduccion> historialBD = 
                    logica.BLLHistorialReproduccion.listarUltimasReproducciones(idUsuario, 20);

            if (historialBD != null && !historialBD.isEmpty()) {
                for (entidades.HistorialReproduccion registro : historialBD) {
                    entidades.Cancion c = registro.getCancion();
                    if (c != null) {
                        panContenido2.add(crearItemCola(c, false));
                        panContenido2.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 1)));
                    }
                }
            } else {
                javax.swing.JLabel lblVacio = new javax.swing.JLabel("  Sin reproducciones recientes");
                lblVacio.setForeground(java.awt.Color.GRAY);
                panContenido2.add(lblVacio);
            }
        } else {
            javax.swing.JLabel lblInvitado = new javax.swing.JLabel("  Inicia sesión para ver tu historial");
            lblInvitado.setForeground(java.awt.Color.GRAY);
            panContenido2.add(lblInvitado);
        }

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
        
        entidades.Artista artistaEncontrado = null;

        try {
            artistaEncontrado = Logica.BLLArtista.obtenerDatosArtista(album.getArtista().getNombre());
            
        } catch (Exception e) {
            System.out.println("Error recuperando artista para el detalle: " + e.getMessage());
        }

        panDetalleAlbum panelDetalle = new panDetalleAlbum(this, album, artistaEncontrado);
        
        mostrarPanel(panelDetalle);
    }
    
    /**
    * Actualiza las estadísticas de panHome si está visible
    */
    private void actualizarEstadisticasHome() {
        // Solo actualizar si panHome está activo
        if (panelActual instanceof panHome) {
            ((panHome) panelActual).cargarDatosUsuario();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        btnAgregarPlaylist = new BotonPersonalizado(new java.awt.Color(0,0,0), new java.awt.Color(50,50,50), new java.awt.Color(40,40,40));
        btnPerfil = new BotonPersonalizado(new java.awt.Color(0,0,0), new java.awt.Color(50,50,50), new java.awt.Color(40,40,40));
        pgbVolumen = new ProgressBarPersonalizada();
        btnVolumen = new BotonPersonalizado(new java.awt.Color(0,0,0), new java.awt.Color(50,50,50), new java.awt.Color(40,40,40));
        btnBucle = new BotonPersonalizado(new java.awt.Color(0,0,0), new java.awt.Color(50,50,50), new java.awt.Color(40,40,40));

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

        btnAgregarPlaylist.setBackground(new java.awt.Color(0, 0, 0));
        btnAgregarPlaylist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/añadir-chiquito.png"))); // NOI18N
        btnAgregarPlaylist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarPlaylistActionPerformed(evt);
            }
        });

        btnPerfil.setBackground(new java.awt.Color(0, 0, 0));
        btnPerfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/perfil3.png"))); // NOI18N
        btnPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerfilActionPerformed(evt);
            }
        });

        pgbVolumen.setBackground(new java.awt.Color(204, 204, 204));

        btnVolumen.setBackground(new java.awt.Color(0, 0, 0));
        btnVolumen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/volumen-chiquito.png"))); // NOI18N
        btnVolumen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolumenActionPerformed(evt);
            }
        });

        btnBucle.setBackground(new java.awt.Color(0, 0, 0));
        btnBucle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/bucle2-chiquito.png"))); // NOI18N
        btnBucle.setPreferredSize(new java.awt.Dimension(30, 30));
        btnBucle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBucleActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPerfil))
                    .addGroup(panFondoLayout.createSequentialGroup()
                        .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panFondoLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(lblImagenCancion, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblArtista, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                                        .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(btnAgregarPlaylist))
                                .addGap(50, 50, 50)
                                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panFondoLayout.createSequentialGroup()
                                        .addGap(212, 212, 212)
                                        .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnPlayPausar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnBucle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panFondoLayout.createSequentialGroup()
                                .addComponent(btnVolumen)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pgbVolumen, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)))))
                .addGap(30, 30, 30))
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
                    .addComponent(btnVolver)
                    .addComponent(btnPerfil))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panFondoLayout.createSequentialGroup()
                        .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 711, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panFondoLayout.createSequentialGroup()
                                .addComponent(lblImagenCancion, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(panFondoLayout.createSequentialGroup()
                                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnPlayPausar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panFondoLayout.createSequentialGroup()
                                        .addComponent(lblTitulo)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblArtista))
                                    .addComponent(btnBucle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panFondoLayout.createSequentialGroup()
                                        .addComponent(btnAgregarPlaylist)
                                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(panFondoLayout.createSequentialGroup()
                                        .addComponent(pgbProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(lblTmpActual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblDuracion))
                                        .addGap(49, 49, 49))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panFondoLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnVolumen)
                                .addGap(49, 49, 49))))
                    .addGroup(panFondoLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pgbVolumen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58))))
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
        entidades.Cancion c = listasDinamicas.CLLReproductor.getInstancia().obtenerAnterior();
    
        if (c != null) {
            reproducirCancionActual();
        }
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnColaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColaActionPerformed
        javax.swing.SwingUtilities.invokeLater(() -> cargarColaLateral());
    }//GEN-LAST:event_btnColaActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        javax.swing.SwingUtilities.invokeLater(() -> cargarHistorialLateral());
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void btnPlayPausarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayPausarActionPerformed
        if (gestorAudio == null) return;
    
        if (listasDinamicas.CLLReproductor.getInstancia().getActual() == null) return;

        if (gestorAudio.estaReproduciendo()) {
            gestorAudio.pausar();
            btnPlayPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/play.png"))); 
        } else {
            gestorAudio.continuar();
            btnPlayPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pausa.png"))); 
        }
        btnPlayPausar.repaint();
    }//GEN-LAST:event_btnPlayPausarActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        entidades.Cancion c = listasDinamicas.CLLReproductor.getInstancia().obtenerSiguiente();

        if (c != null) {
            reproducirCancionActual();
        } else {
            // No hay más canciones en la cola
            if (gestorAudio != null) gestorAudio.pausar();
            if (timerProgreso != null) timerProgreso.stop(); // ← DETENER EL TIMER

            // Resetear la barra de progreso
            pgbProgreso.setValue(0);
            lblTmpActual.setText("00:00");
            lblDuracion.setText("00:00");

            // Cambiar ícono a play
            btnPlayPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/play.png")));

            // Actualizar cola lateral para mostrar "Cola vacía"
            javax.swing.SwingUtilities.invokeLater(() -> {
                cargarColaLateral(true);
            });
        }
    }//GEN-LAST:event_btnSiguienteActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        volverAtras();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        mostrarPanel(panHome);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnAgregarPlaylistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarPlaylistActionPerformed
        entidades.Cancion actual = listasDinamicas.CLLReproductor.getInstancia().getActual();

        if (actual == null) {
            JOptionPane.showMessageDialog(this, "Primero reproduce una canción", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        dlgSeleccionarPlaylist dialog = new dlgSeleccionarPlaylist(this, true, actual);
        dialog.setVisible(true);

        if (panelActual instanceof panPlaylist) {
            ((panPlaylist) panelActual).cargarDatosPlaylist();
        }
    }//GEN-LAST:event_btnAgregarPlaylistActionPerformed

    private void btnPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerfilActionPerformed
        mostrarPanel(panPerfil);
    }//GEN-LAST:event_btnPerfilActionPerformed

    private void btnVolumenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolumenActionPerformed
        int valorActual = pgbVolumen.getValue();
    
        if (valorActual > 0) {
            // MUTEAR: Guardar volumen actual y poner en 0
            volumenAnterior = valorActual / 100.0;
            pgbVolumen.setValue(0);

            if (gestorAudio != null) {
                gestorAudio.setVolumen(0.0);
            }

            btnVolumen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/muteado-chiquito.png")));
        } else {
            // DESMUTEAR: Restaurar volumen anterior
            int valorRestaurado = (int) (volumenAnterior * 100);
            pgbVolumen.setValue(valorRestaurado);

            if (gestorAudio != null) {
                gestorAudio.setVolumen(volumenAnterior);
            }

            btnVolumen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/volumen-chiquito.png")));
        }
    }//GEN-LAST:event_btnVolumenActionPerformed

    private void btnBucleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBucleActionPerformed
        modoRepetirUna = !modoRepetirUna; // Alternar estado
    
        if (modoRepetirUna) {
            // Activar bucle - Ícono verde
            btnBucle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/bucle-verde-chiquito.png")));
            System.out.println("🔁 Modo repetir UNA activado");
        } else {
            // Desactivar bucle - Ícono normal
            btnBucle.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/bucle2-chiquito.png")));
            System.out.println("➡️ Modo normal activado");
        }
    }//GEN-LAST:event_btnBucleActionPerformed

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
    private javax.swing.JButton btnAgregarPlaylist;
    private javax.swing.JButton btnAnterior;
    private javax.swing.JButton btnBucle;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCola;
    private javax.swing.JButton btnHistorial;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnPerfil;
    private javax.swing.JButton btnPlayPausar;
    private javax.swing.JButton btnSiguiente;
    private javax.swing.JButton btnVolumen;
    private javax.swing.JButton btnVolver;
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
    private javax.swing.JProgressBar pgbVolumen;
    private javax.swing.JScrollPane scpColaHistorial;
    private javax.swing.JScrollPane scpPlaylists;
    // End of variables declaration//GEN-END:variables
}
