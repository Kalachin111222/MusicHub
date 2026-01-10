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
    
    public FrmPrincipal() {
        initComponents();
        setLocationRelativeTo(null); // Centrar
        
        inicializarComponentes();

        // --- LADO IZQUIERDO --- 
        cargarPlaylistsLateral(); 


        cargarColaLateral(); 
        new javafx.embed.swing.JFXPanel();
    }
    
    private void configurarUsuarioActual() {
        // Usamos tu BLLUsuario para preguntar
        if (logica.BLLUsuario.hayUsuarioLogueado()) {
            entidades.Usuario usuario = logica.BLLUsuario.getUsuarioActual();
            
            // AQUÍ: Actualiza la interfaz con el nombre del usuario
            // Por ejemplo, si tienes un label en el menú lateral o arriba:
            // lblNombreUsuario.setText(usuario.getNombre());
            
            System.out.println("Usuario logueado: " + usuario.getNombre());
        } else {
            // Modo Invitado (No restringimos nada, solo avisamos)
            // lblNombreUsuario.setText("Invitado");
            System.out.println("Modo Invitado");
        }
    }
    
    private void inicializarComponentes() {
        gestorAudio = new GestorAudio();

        // 1. Timer de la barra de progreso
        timerProgreso = new javax.swing.Timer(500, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                actualizarBarraGUI();
            }
        });

        // 2. Configuración visual de Scrolls
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

        // 3. Inicializar Paneles
        panHome = new panHome(this);
        panPlaylist = new panPlaylist(this);
        panArtista = new panArtista(this);
        panPerfil = new panPerfil(this);

        // ---------------------------------------------------------
        // 4. CARGA DE DATOS REALES DESDE LA BASE DE DATOS
        // ---------------------------------------------------------
        
        // A) Cargar Usuario
        configurarUsuarioActual();

        // B) Cargar Canciones desde BLL
        // Obtenemos la lista normal de Java desde tu BLL
        java.util.List<entidades.Cancion> cancionesBD = logica.BLLCancion.obtenerCancionesParaMostrar();
        
        // Creamos tu lista personalizada
        estructuras.ListaCircularDoble<entidades.Cancion> listaParaPaneles = new estructuras.ListaCircularDoble<>();

        if (cancionesBD != null && !cancionesBD.isEmpty()) {
            // Convertimos la List de Java a tu ListaCircularDoble
            for (entidades.Cancion c : cancionesBD) {
                listaParaPaneles.insertar(c);
            }
        } else {
            System.out.println("Advertencia: No se encontraron canciones en la BD o error de conexión.");
            // Opcional: Aquí podrías dejar los datos falsos como respaldo si la BD falla
        }

        // Asignamos la lista real a los paneles
        panHome.setListaRecomendadas(listaParaPaneles);
        panHome.setListaPopulares(listaParaPaneles); 
        // Nota: Idealmente crearías dos listas distintas en el BLL (una para pop y otra recomendados)
        // pero por ahora usamos la misma para probar que carga.

        // 5. Mostrar panel inicial
        mostrarPanel(panHome);

        // 6. Listener para la barra de progreso (Click y Arrastre)
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

        // Usamos un hilo aparte para no congelar la pantalla mientras descarga
        new Thread(() -> {
            try {
                // 1. Descargar imagen
                java.net.URL linkImagen = new java.net.URL(url);
                java.awt.image.BufferedImage imagenOriginal = ImageIO.read(linkImagen);
                
                if (imagenOriginal != null) {
                    // 2. Ajustar tamaño (Redimensionar)
                    // Si el label mide 0 (aún no se ve), asume 100x100
                    int ancho = label.getWidth() > 0 ? label.getWidth() : 150;
                    int alto = label.getHeight() > 0 ? label.getHeight() : 150;
                    
                    Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
                    
                    // 3. Poner en el Label (dentro del hilo de Swing)
                    SwingUtilities.invokeLater(() -> {
                        label.setIcon(new ImageIcon(imagenEscalada));
                    });
                }
            } catch (Exception e) {
                System.out.println("⚠️ Error cargando imagen: " + url);
            }
        }).start();
    }
    
    public void reproducirCancion(entidades.Cancion c) {
        System.out.println("--- Reproduciendo: " + c.getTitulo() + " ---");
        
        // 1. Textos
        lblTitulo.setText(c.getTitulo());
        lblArtista.setText(c.getArtista() != null ? c.getArtista().getNombre() : "Desconocido");
        
        // 2. FOTO (Lo nuevo) 📸
        // Revisa en tu diseño cuál es el JLabel de la foto grande. 
        // Asumiré que es 'jLabel3' (el cuadro gris a la izquierda de los textos).
//        cargarImagenPortada(c.getUrlPortada(), jLabel3);
        
        // 3. Audio
        if (gestorAudio == null) gestorAudio = new GestorAudio();
        gestorAudio.reproducir(c.getUrlAudio());
        
        timerProgreso.start();
        
        btnPlayPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pausa.png")));
    }

    // Este método se ejecuta automáticamente cada 0.5 segundos gracias al Timer
    private void actualizarBarraGUI() {
        // Verificamos que el gestor exista y esté reproduciendo (o pausado con canción cargada)
        if (gestorAudio != null && gestorAudio.getDuracionTotal() > 0) {
            double actual = gestorAudio.getTiempoActual();
            double total = gestorAudio.getDuracionTotal();

            // 1. Actualizar la barra visual
            pgbProgreso.setMaximum((int) total);
            pgbProgreso.setValue((int) actual);

            // 2. Actualizar los Textos (ESTO ES LO QUE FALTABA)
            lblTmpActual.setText(obtenerTiempoFormateado(actual));
            lblDuracion.setText(obtenerTiempoFormateado(total));
        }
    }
    
    public void mostrarPanel(JPanel panel) {
        panContenido.removeAll();
        panContenido.setLayout(new java.awt.BorderLayout());
        panContenido.add(panel, java.awt.BorderLayout.CENTER);

        panContenido.revalidate();
        panContenido.repaint();

        // --- AGREGAR ESTO: Resetear el scroll arriba ---
        SwingUtilities.invokeLater(() -> {
            jScrollPane1.getVerticalScrollBar().setValue(0);
        });

        if (panelActual != null) {
            historialPaneles.push(panelActual);
        }
        panelActual = panel;
    }
    
    public void cargarPlaylistsLateral() {
        // 1. Configuración inicial del panel contenedor
        panListaPlaylists.removeAll();

        // Usamos BoxLayout en eje Y (Vertical) para apilar las playlists
        panListaPlaylists.setLayout(new BoxLayout(panListaPlaylists, BoxLayout.Y_AXIS));

        // Opcional: Color de fondo si no se puso en diseño
        panListaPlaylists.setBackground(new Color(18, 18, 18)); 

        // 2. OBTENER DATOS (Aquí conectaremos con tu BLLPlaylist más adelante)
        // Por ahora simulamos una lista para ver el diseño
        String[] misPlaylists = {"Rock 80s", "Para Estudiar", "Mis Favoritos", "Cumbia Mix"};

        // 3. RECORRIDO Y CREACIÓN
        for (String nombrePl : misPlaylists) {
            // Crear la tarjeta visual
            JPanel item = crearItemPlaylist(nombrePl);

            // Agregar al panel
            panListaPlaylists.add(item);

            // Separador pequeño invisible (2px)
            panListaPlaylists.add(Box.createRigidArea(new Dimension(0, 2)));
        }

        // 4. Refrescar la interfaz para mostrar cambios
        panListaPlaylists.revalidate();
        panListaPlaylists.repaint();
    }

    private JPanel crearItemPlaylist(String nombre) {
        JPanel panel = new JPanel();
        // Layout horizontal para poner: Icono + Texto
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        // --- LA CORRECCIÓN CLAVE ESTÁ AQUÍ ---
        // Antes tenías Integer.MAX_VALUE, lo que hacía que el item quisiera ser infinito.
        // Ahora le ponemos un techo de 220px. Esto evita que ensanche el panel padre.
        panel.setMaximumSize(new Dimension(220, 45));
        
        // Tamaño base
        panel.setPreferredSize(new Dimension(190, 45));

        // Estilos visuales
        panel.setBackground(new Color(18,18,18)); // Fondo oscuro
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Margen interno
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Manita al pasar mouse

        // --- COMPONENTES INTERNOS ---
        
        // 1. Icono Musical
        JLabel lblIcono = new JLabel("♫");
        lblIcono.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lblIcono.setForeground(new Color(179, 179, 179)); // Gris claro

        // 2. Nombre de la Playlist
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setForeground(new Color(200, 200, 200)); // Blanco hueso
        lblNombre.setFont(new java.awt.Font("Segoe UI", 0, 13));

        // --- AGREGAR AL PANEL ---
        panel.add(lblIcono);
        panel.add(Box.createRigidArea(new Dimension(10, 0))); // Espacio de 10px
        panel.add(lblNombre);
        
        // Esto empuja todo el contenido a la izquierda (alineación izquierda)
        panel.add(Box.createHorizontalGlue());

        // --- EVENTOS DEL MOUSE (HOVER) ---
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(40, 40, 40)); // Iluminar
                lblNombre.setForeground(Color.WHITE);
                lblIcono.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(18, 18, 18)); // Oscurecer
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
        // 1. Limpiamos el panel
        panContenido2.removeAll(); 
        panContenido2.setLayout(new BoxLayout(panContenido2, BoxLayout.Y_AXIS));
        panContenido2.setBackground(new Color(18, 18, 18));

        // (ELIMINADO BLOQUE DE TITULO)

        // 2. OBTENER DATOS 
        String[][] cancionesFake = {
            {"Bohemian Rhapsody", "Queen"},
            {"Billie Jean", "Michael Jackson"},
            {"Hotel California", "Eagles"},
            {"Shape of You", "Ed Sheeran"}
        };

        // 3. RECORRIDO
        for (String[] data : cancionesFake) {
            JPanel item = crearItemCola(data[0], data[1]);
            panContenido2.add(item);
            panContenido2.add(Box.createRigidArea(new Dimension(0, 1)));
        }

        panContenido2.revalidate();
        panContenido2.repaint();
    }

    private JPanel crearItemCola(String titulo, String artista) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        // --- CORRECCIÓN AQUÍ ---
        // ANTES: new Dimension(Integer.MAX_VALUE, 55)
        // AHORA: Limitamos el ancho a 220px para que no empuje
        panel.setMaximumSize(new Dimension(220, 55));
        
        panel.setPreferredSize(new Dimension(200, 55));

        panel.setBackground(new Color(18, 18, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ... (El resto del código de etiquetas y eventos sigue igual) ...
        // 1. NÚMERO O ÍCONO (Izquierda)
        JLabel lblIcon = new JLabel("♫");
        lblIcon.setForeground(new Color(100, 100, 100));
        lblIcon.setFont(new java.awt.Font("Segoe UI", 0, 14));

        // 2. PANEL DE TEXTO (Centro - Vertical)
        JPanel panTextos = new JPanel();
        panTextos.setLayout(new BoxLayout(panTextos, BoxLayout.Y_AXIS));
        panTextos.setOpaque(false); // Transparente
        panTextos.setAlignmentY(java.awt.Component.CENTER_ALIGNMENT); 

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new java.awt.Font("Segoe UI", 0, 13));

        JLabel lblArtista = new JLabel(artista);
        lblArtista.setForeground(new Color(150, 150, 150)); 
        lblArtista.setFont(new java.awt.Font("Segoe UI", 0, 11)); 

        panTextos.add(lblTitulo);
        panTextos.add(lblArtista);

        // 3. DURACIÓN (Derecha - Opcional)
        JLabel lblDuracion = new JLabel("3:45");
        lblDuracion.setForeground(new Color(100, 100, 100));
        lblDuracion.setFont(new java.awt.Font("Segoe UI", 0, 11));

        // ARMADO
        panel.add(lblIcon);
        panel.add(Box.createRigidArea(new Dimension(10, 0))); 
        panel.add(panTextos);
        panel.add(Box.createHorizontalGlue()); 
        panel.add(lblDuracion);
        panel.add(Box.createRigidArea(new Dimension(5, 0))); 

        // HOVER
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
        // 1. Limpieza y configuración
        panContenido2.removeAll();
        panContenido2.setLayout(new BoxLayout(panContenido2, BoxLayout.Y_AXIS));
        panContenido2.setBackground(new Color(18, 18, 18));

        // (ELIMINADO BLOQUE DE TITULO)

        // 2. Datos falsos
        String[][] historialFake = {
            {"Yesterday", "The Beatles"},
            {"Thriller", "Michael Jackson"},
            {"Hips Don't Lie", "Shakira"},
            {"Despacito", "Luis Fonsi"}
        };

        // 3. RECORRIDO 
        for (String[] data : historialFake) {
            JPanel item = crearItemCola(data[0], data[1]); // Reutilizamos el item
            panContenido2.add(item);
            panContenido2.add(Box.createRigidArea(new Dimension(0, 1)));
        }

        panContenido2.revalidate();
        panContenido2.repaint();
    }
    
    // Método auxiliar para convertir segundos a formato mm:ss
    private String obtenerTiempoFormateado(double segundos) {
        int totalSegundos = (int) segundos;
        int minutos = totalSegundos / 60;
        int segs = totalSegundos % 60;
        // Formato %02d asegura que ponga un cero a la izquierda si es menor a 10 (ej: 05)
        return String.format("%02d:%02d", minutos, segs);
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
        jLabel3 = new javax.swing.JLabel();
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

        javax.swing.GroupLayout panFondoLayout = new javax.swing.GroupLayout(panFondo);
        panFondo.setLayout(panFondoLayout);
        panFondoLayout.setHorizontalGroup(
            panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panFondoLayout.createSequentialGroup()
                .addGap(589, 589, 589)
                .addComponent(btnHome)
                .addGap(18, 18, 18)
                .addComponent(btnBuscar)
                .addGap(0, 0, 0)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panFondoLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panFondoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(30, 30, 30))
        );
        panFondoLayout.setVerticalGroup(
            panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panFondoLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHome)
                    .addComponent(btnBuscar))
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
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnPlayPausar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(12, 12, 12)
                                        .addComponent(pgbProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panFondoLayout.createSequentialGroup()
                                        .addComponent(lblTitulo)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lblArtista)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblTmpActual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(lblDuracion))
                                .addGap(49, 49, 49))
                            .addGroup(panFondoLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAnteriorActionPerformed

    private void btnColaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColaActionPerformed
        cargarColaLateral();
    }//GEN-LAST:event_btnColaActionPerformed

    private void btnHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHistorialActionPerformed
        cargarHistorialLateral();
    }//GEN-LAST:event_btnHistorialActionPerformed

    private void btnPlayPausarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayPausarActionPerformed
        if (gestorAudio == null) return; // Si no hay gestor, no hacemos nada

        // 1. Ordenamos al gestor que cambie el estado (Play <-> Pause)
        gestorAudio.pausar(); 
        
        // 2. Cambiamos el icono según el nuevo estado
        if (gestorAudio.estaReproduciendo()) {
            // Si está sonando, mostramos el botón de "Pausa"
            // (Asegúrate de tener una imagen 'pausa.png' en tu carpeta)
            btnPlayPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/play.png")));
            
        } else {
            // Si está quieto, mostramos el botón de "Play"
            btnPlayPausar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pausa.png")));
        }
    }//GEN-LAST:event_btnPlayPausarActionPerformed

    private void btnSiguienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSiguienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSiguienteActionPerformed

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
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JButton jButton11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lblArtista;
    private javax.swing.JLabel lblDuracion;
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
