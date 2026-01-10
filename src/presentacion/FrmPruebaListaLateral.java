/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author ArcosArce
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmPruebaListaLateral extends JFrame {

    public FrmPruebaListaLateral() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Prueba - Barra Lateral de Playlists");
        setSize(400, 600); // Hacemos la ventana angosta para simular el panel lateral
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 1. PANEL FONDO (Simula ser tu panel Izquierdo o Derecho)
        JPanel panLateral = new JPanel(new BorderLayout());
        panLateral.setBackground(new Color(18, 18, 18)); // Negro fondo
        
        // Titulo de la sección
        JLabel lblTitulo = new JLabel("TUS PLAYLISTS");
        lblTitulo.setForeground(new Color(179, 179, 179)); // Gris claro texto
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
        panLateral.add(lblTitulo, BorderLayout.NORTH);

        // 2. EL CONTENEDOR DE LA LISTA
        JPanel panLista = new JPanel();
        panLista.setBackground(new Color(18, 18, 18));
        // AQUI EL CAMBIO: Usamos BoxLayout Y_AXIS para que se apilen verticalmente
        panLista.setLayout(new BoxLayout(panLista, BoxLayout.Y_AXIS)); 
        
        // Simulamos datos de playlists
        String[] misPlaylists = {"Rock Clásico", "Para Programar", "Gym Motivation", "Salsa Dura", "Lofi Beats", "Mis Me Gusta"};
        
        for (String nombre : misPlaylists) {
            JPanel item = crearItemPlaylist(nombre);
            panLista.add(item);
            // Separador invisible pequeño entre items (opcional)
            panLista.add(Box.createRigidArea(new Dimension(0, 2))); 
        }

        // 3. SCROLLPANE (Por si tiene muchas playlists)
        JScrollPane scroll = new JScrollPane(panLista);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        panLateral.add(scroll, BorderLayout.CENTER);
        
        this.add(panLateral);
    }

    // --- DISEÑO DE CADA FILA DE PLAYLIST ---
    private JPanel crearItemPlaylist(String nombre) {
        JPanel panel = new JPanel();
        // Layout horizontal: Icono a la izquierda, texto al centro
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        
        // Tamaño: Ancho máximo, Alto 60px
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); 
        panel.setPreferredSize(new Dimension(300, 60));
        
        panel.setBackground(new Color(18, 18, 18)); // Fondo transparente/negro inicial
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10)); // Márgenes internos
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // 1. ICONO (Cuadradito de color)
        JLabel lblIcono = new JLabel();
        lblIcono.setPreferredSize(new Dimension(40, 40));
        lblIcono.setMaximumSize(new Dimension(40, 40));
        lblIcono.setOpaque(true);
        // Generamos un color aleatorio para que se vea bonito en la prueba
        lblIcono.setBackground(new Color((int)(Math.random()*100)+50, (int)(Math.random()*100)+50, (int)(Math.random()*100)+50));
        lblIcono.setText("♫");
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcono.setForeground(Color.WHITE);
        lblIcono.setFont(new Font("SansSerif", Font.BOLD, 14));

        // 2. TEXTO
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setForeground(new Color(200, 200, 200)); // Blanco hueso
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        // AGREGAR AL PANEL
        panel.add(lblIcono);
        panel.add(Box.createRigidArea(new Dimension(15, 0))); // Espacio entre icono y texto
        panel.add(lblNombre);
        
        // Empuja todo a la izquierda (relleno elástico)
        panel.add(Box.createHorizontalGlue()); 

        // EFECTO HOVER (Al pasar el mouse)
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(40, 40, 40)); // Se ilumina
                lblNombre.setForeground(Color.WHITE);       // Texto se pone blanco brillante
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(new Color(18, 18, 18)); // Vuelve a negro
                lblNombre.setForeground(new Color(200, 200, 200));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Abrir playlist: " + nombre);
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmPruebaListaLateral().setVisible(true);
        });
    }
}