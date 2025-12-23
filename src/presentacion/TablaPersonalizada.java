/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ArcosArce
 */
package presentacion;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Tabla personalizada con diseño moderno y oscuro
 * @author ArcosArce
 */
public class TablaPersonalizada extends JTable {
    
    private Color colorFondo = new Color(40, 40, 40);
    private Color colorFondoAlternado = new Color(35, 35, 35);
    private Color colorSeleccion = new Color(29, 184, 85);
    private Color colorTexto = Color.WHITE;
    private Color colorHeader = new Color(30, 30, 30);
    private Color colorGrid = new Color(50, 50, 50);
    private Color colorHover = new Color(45, 45, 45);
    
    private int filaHover = -1;
    private int radioBordes = 15;
    
    public TablaPersonalizada() {
        configurarEstilo();
        configurarComportamiento();
        configurarHeader();
        setOpaque(false);
    }
    
    public TablaPersonalizada(TableModel modelo) {
        super(modelo);
        configurarEstilo();
        configurarComportamiento();
        configurarHeader();
        setOpaque(false);
    }
    
    private void configurarEstilo() {
        setBackground(colorFondo);
        setForeground(colorTexto);
        setSelectionBackground(colorSeleccion);
        setSelectionForeground(colorTexto);
        setGridColor(colorGrid);
        
        setFont(new Font("Roboto", Font.PLAIN, 13));
        setRowHeight(35);
        
        setShowGrid(true);
        setIntercellSpacing(new Dimension(0, 1));
        
        setFocusable(true);
        setRowSelectionAllowed(true);
        setColumnSelectionAllowed(false);
    }
    
    private void configurarComportamiento() {
        setDefaultEditor(Object.class, null);
        
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int fila = rowAtPoint(e.getPoint());
                if (fila != filaHover) {
                    filaHover = fila;
                    repaint();
                }
            }
        });
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                filaHover = -1;
                repaint();
            }
        });
    }
    
    private void configurarHeader() {
        JTableHeader header = getTableHeader();
        header.setBackground(colorHeader);
        header.setForeground(colorTexto);
        header.setFont(new Font("Roboto", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        
        header.setDefaultRenderer(new HeaderRenderer());
    }
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            jc.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }
        
        if (!isRowSelected(row)) {
            if (row == filaHover) {
                c.setBackground(colorHover);
            } 
            else if (row % 2 == 0) {
                c.setBackground(colorFondo);
            } else {
                c.setBackground(colorFondoAlternado);
            }
            c.setForeground(colorTexto);
        } else {
            c.setBackground(colorSeleccion);
            c.setForeground(colorTexto);
        }
        
        return c;
    }
    
    private class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(LEFT);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
            
            label.setBackground(colorHeader);
            label.setForeground(colorTexto);
            label.setFont(new Font("Roboto", Font.BOLD, 12));
            label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, colorGrid),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            
            return label;
        }
    }
    
    public void setColorFondo(Color color) {
        this.colorFondo = color;
        repaint();
    }
    
    public void setColorFondoAlternado(Color color) {
        this.colorFondoAlternado = color;
        repaint();
    }
    
    public void setColorSeleccion(Color color) {
        this.colorSeleccion = color;
        setSelectionBackground(color);
        repaint();
    }
    
    public void setColorTexto(Color color) {
        this.colorTexto = color;
        setForeground(color);
        repaint();
    }
    
    public void setColorHeader(Color color) {
        this.colorHeader = color;
        getTableHeader().setBackground(color);
        repaint();
    }
    
    public void setColorGrid(Color color) {
        this.colorGrid = color;
        setGridColor(color);
        repaint();
    }
    
    public void setColorHover(Color color) {
        this.colorHover = color;
        repaint();
    }
    
    public void setColores(Color fondo, Color fondoAlt, Color seleccion, 
                          Color texto, Color header, Color grid, Color hover) {
        this.colorFondo = fondo;
        this.colorFondoAlternado = fondoAlt;
        this.colorSeleccion = seleccion;
        this.colorTexto = texto;
        this.colorHeader = header;
        this.colorGrid = grid;
        this.colorHover = hover;
        
        configurarEstilo();
        configurarHeader();
        repaint();
    }
    
    public void aplicarTemaOscuro() {
        setColores(
            new Color(40, 40, 40),
            new Color(35, 35, 35),
            new Color(29, 184, 85),
            Color.WHITE,
            new Color(30, 30, 30),
            new Color(50, 50, 50),
            new Color(45, 45, 45)
        );
    }
    
    public void aplicarTemaClaro() {
        setColores(
            Color.WHITE,
            new Color(245, 245, 245),
            new Color(29, 184, 85),
            new Color(20, 20, 20),
            new Color(230, 230, 230),
            new Color(200, 200, 200),
            new Color(240, 240, 240)
        );
    }
    
    public void setRadioBordes(int radio) {
        this.radioBordes = radio;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(colorFondo);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), radioBordes, radioBordes);
        
        g2d.dispose();
        super.paintComponent(g);
    }
}