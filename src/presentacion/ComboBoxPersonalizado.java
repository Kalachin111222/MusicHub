/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author ArcosArce
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

/**
 *
 * @author ArcosArce
 */
public class ComboBoxPersonalizado<E> extends JComboBox<E> {
    private Color colorFondo;
    private Color colorTexto;
    private Color colorFlecha;
    private Color colorBorde;
    
    public ComboBoxPersonalizado() {
        this(new Color(60, 60, 60), Color.WHITE, Color.WHITE, new Color(80, 80, 80));
    }
    
    public ComboBoxPersonalizado(Color colorFondo) {
        this(colorFondo, Color.WHITE, Color.WHITE, colorFondo.brighter());
    }
    
    public ComboBoxPersonalizado(Color colorFondo, Color colorTexto, Color colorFlecha, Color colorBorde) {
        this.colorFondo = colorFondo;
        this.colorTexto = colorTexto;
        this.colorFlecha = colorFlecha;
        this.colorBorde = colorBorde;
        
        configurarEstilo();
    }
    
    private void configurarEstilo() {
        setFont(new Font("Roboto", Font.PLAIN, 12));
        setForeground(colorTexto);
        setBackground(colorFondo);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Aplicar UI personalizada
        setUI(new CustomComboBoxUI());
    }
    
    // Métodos para cambiar colores dinámicamente
    public void setColorFondo(Color color) {
        this.colorFondo = color;
        setBackground(color);
        repaint();
    }
    
    public void setColorTexto(Color color) {
        this.colorTexto = color;
        setForeground(color);
        repaint();
    }
    
    public void setColorFlecha(Color color) {
        this.colorFlecha = color;
        repaint();
    }
    
    public void setColorBorde(Color color) {
        this.colorBorde = color;
        repaint();
    }
    
    public void setColores(Color fondo, Color texto, Color flecha, Color borde) {
        this.colorFondo = fondo;
        this.colorTexto = texto;
        this.colorFlecha = flecha;
        this.colorBorde = borde;
        setBackground(fondo);
        setForeground(texto);
        repaint();
    }
    
    // UI personalizada
    private class CustomComboBoxUI extends BasicComboBoxUI {
        
        @Override
        protected JButton createArrowButton() {
            return new ArrowButton();
        }
        
        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(colorFondo);
            g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);
            
            g2.dispose();
        }
        
        @Override
        protected ComboPopup createPopup() {
            return new CustomComboPopup(comboBox);
        }
        
        // Botón de flecha personalizado
        private class ArrowButton extends JButton {
            public ArrowButton() {
                setPreferredSize(new Dimension(20, 20));
                setBackground(colorFondo);
                setBorder(BorderFactory.createEmptyBorder());
                setFocusPainted(false);
                setContentAreaFilled(false);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo
                g2.setColor(colorFondo);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Flecha
                int w = getWidth();
                int h = getHeight();
                int size = 6;
                
                int[] xPoints = {
                    w/2 - size, 
                    w/2 + size, 
                    w/2
                };
                int[] yPoints = {
                    h/2 - size/2, 
                    h/2 - size/2, 
                    h/2 + size/2
                };
                
                g2.setColor(colorFlecha);
                g2.fillPolygon(xPoints, yPoints, 3);
                
                g2.dispose();
            }
        }
    }
    
    // Popup personalizado
    private class CustomComboPopup extends BasicComboPopup {
        
        public CustomComboPopup(JComboBox combo) {
            super(combo);
        }
        
        @Override
        protected void configurePopup() {
            super.configurePopup();
            setBorder(BorderFactory.createLineBorder(colorBorde, 1));
            setBackground(colorFondo);
        }
        
        @Override
        protected void configureList() {
            super.configureList();
            list.setBackground(colorFondo);
            list.setForeground(colorTexto);
            list.setSelectionBackground(colorBorde);
            list.setSelectionForeground(colorTexto);
            list.setFont(new Font("Roboto", Font.PLAIN, 12));
            list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
        
        @Override
        protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
            // Hacer el popup del mismo ancho que el combobox
            return super.computePopupBounds(px, py, comboBox.getWidth(), ph);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fondo redondeado
        g2.setColor(colorFondo);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        
        // Borde sutil (opcional)
        g2.setColor(colorBorde);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        
        g2.dispose();
        
        super.paintComponent(g);
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        // No pintar borde por defecto
    }
}