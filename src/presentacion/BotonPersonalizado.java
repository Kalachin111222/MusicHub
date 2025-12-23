/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
/**
 *
 * @author ArcosArce
 */

/**
 *
 * @author ArcosArce
 */
public class BotonPersonalizado extends JButton{
    private Color color;
    private Color press;
    private Color hover;
    
    public BotonPersonalizado(){
        press = new Color(200,200,200);
        hover = new Color(235,235,235);
        color = new Color(255,255,255);
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }
    
    public BotonPersonalizado(Color color) {
        this.color = color;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        this.press = color.darker();
        this.hover = color.brighter();
    }
    
    public BotonPersonalizado(Color color, Color press, Color hover) {
        this.color = color;
        this.press = press;
        this.hover = hover;
        setOpaque(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (getModel().isPressed()) {
            g2.setColor(press);
        } else if (getModel().isRollover()) {
            g2.setColor(hover);
        } else {
            g2.setColor(color);
        }
        // Dibuja el botón con bordes redondeados
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g2.dispose();
        
        super.paintComponent(g);
    }
    
    // ========== MÉTODOS NUEVOS ==========
    
    /**
     * Cambia el color normal del botón
     * @param color Color de fondo normal
     */
    public void setColorNormal(Color color) {
        this.color = color;
        repaint();
    }
    
    /**
     * Cambia el color cuando se presiona
     * @param press Color al presionar
     */
    public void setColorPress(Color press) {
        this.press = press;
        repaint();
    }
    
    /**
     * Cambia el color cuando el mouse pasa sobre el botón
     * @param hover Color en hover
     */
    public void setColorHover(Color hover) {
        this.hover = hover;
        repaint();
    }
    
    /**
     * Cambia todos los colores a la vez
     * @param normal Color normal
     * @param press Color al presionar
     * @param hover Color en hover
     */
    public void setColores(Color normal, Color press, Color hover) {
        this.color = normal;
        this.press = press;
        this.hover = hover;
        repaint();
    }
    
    /**
     * Cambia el color y calcula automáticamente press y hover
     * @param color Color base
     */
    public void setColorBase(Color color) {
        this.color = color;
        this.press = color.darker();
        this.hover = color.brighter();
        repaint();
    }
    
    // Getters
    public Color getColorNormal() {
        return color;
    }
    
    public Color getColorPress() {
        return press;
    }
    
    public Color getColorHover() {
        return hover;
    }
}