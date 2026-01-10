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

public class FrmPruebaGrid extends JFrame {

    public FrmPruebaGrid() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Prueba Visual - Grid de Artistas/Playlists");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 1. EL CONTENEDOR PRINCIPAL (PANEL DE FONDO)
        JPanel panFondo = new JPanel(new BorderLayout());
        panFondo.setBackground(new Color(18, 18, 18)); // Negro Spotify
        
        // --- TITULO DE LA SECCIÓN ---
        JLabel lblTitulo = new JLabel("Tus Artistas Favoritos");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 0)); // Margen
        panFondo.add(lblTitulo, BorderLayout.NORTH);

        // 2. EL PANEL GRID (DONDE OCURRE LA MAGIA)
        JPanel panGrid = new JPanel();
        panGrid.setBackground(new Color(18, 18, 18));
        
        // AQUÍ ESTÁ EL TRUCO: GridLayout
        // 0 filas (crece hacia abajo), 4 columnas fijas
        // 20px de espacio horizontal, 20px vertical
        panGrid.setLayout(new GridLayout(0, 4, 20, 20)); 
        
        // Margen alrededor de todo el grid para que no pegue con los bordes
        panGrid.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // 3. GENERAR DATOS FALSOS (SIMULANDO TU BLL)
        for (int i = 1; i <= 12; i++) { // Creamos 12 tarjetas de prueba
            JPanel tarjeta = crearTarjeta("Artista " + i);
            panGrid.add(tarjeta);
        }

        // 4. EL SCROLLPANE (NECESARIO PARA NAVEGAR)
        JScrollPane scroll = new JScrollPane(panGrid);
        scroll.setBorder(null); // Quitar borde feo por defecto
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16); // Scroll más suave
        
        panFondo.add(scroll, BorderLayout.CENTER);
        
        this.add(panFondo);
    }

    // --- METODO PARA CREAR CADA "CARTA" O TARJETA ---
    private JPanel crearTarjeta(String nombre) {
        JPanel card = new JPanel();
        // Usamos BoxLayout vertical para apilar Foto encima de Texto
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(40, 40, 40)); // Gris oscuro (tarjeta)
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Relleno interno
        card.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Manita al pasar mouse

        // 1. FOTO FALSA (CUADRADO)
        JLabel lblFoto = new JLabel();
        lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar
        lblFoto.setPreferredSize(new Dimension(140, 140)); // Tamaño foto
        lblFoto.setMaximumSize(new Dimension(140, 140));   // Asegurar tamaño
        lblFoto.setOpaque(true);
        lblFoto.setBackground(new Color(100, 100, 100)); // Color gris simulando foto
        lblFoto.setText("FOTO"); // Texto temporal
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setForeground(Color.WHITE);

        // 2. TEXTO (NOMBRE)
        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        // 3. TEXTO SECUNDARIO
        JLabel lblSub = new JLabel("Artista");
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setForeground(new Color(179, 179, 179)); // Gris claro
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // AGREGAR AL PANEL TARJETA
        card.add(lblFoto);
        card.add(Box.createRigidArea(new Dimension(0, 15))); // Espacio entre foto y texto
        card.add(lblNombre);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(lblSub);

        // EFECTO HOVER (Opcional: cambia de color al pasar el mouse)
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(60, 60, 60)); // Más claro
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(new Color(40, 40, 40)); // Original
            }
        });

        return card;
    }

    public static void main(String[] args) {
        // Ejecutar el Frame
        SwingUtilities.invokeLater(() -> {
            new FrmPruebaGrid().setVisible(true);
        });
    }
}