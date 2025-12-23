package presentacion;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Barra de progreso personalizada con animación fluida y círculo arrastrable
 * @author ArcosArce
 */
public class ProgressBarPersonalizada extends JProgressBar {
    
    private Color colorBarra = new Color(255, 255, 255);
    private Color colorFondo = new Color(80, 80, 80);
    private Color colorBarraHover = new Color(29, 184, 85);
    private Color colorCirculo = Color.WHITE;
    
    private boolean mouseEncima = false;
    private boolean arrastrando = false;
    private int posicionMouse = 0;
    private Timer animacionTimer;
    private float progresoActual = 0f;
    private float progresoObjetivo = 0f;
    
    public ProgressBarPersonalizada() {
        configurarEstilo();
        configurarEventos();
        iniciarAnimacion();
    }
    
    private void configurarEstilo() {
        setOpaque(false);
        setBorderPainted(false);
        setPreferredSize(new Dimension(getWidth(), 20));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void configurarEventos() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseEncima = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (!arrastrando) {
                    mouseEncima = false;
                    repaint();
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                arrastrando = true;
                actualizarPosicion(e.getX());
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (arrastrando) {
                    arrastrando = false;
                    notificarCambio(e.getX());
                    if (!getBounds().contains(e.getPoint())) {
                        mouseEncima = false;
                    }
                    repaint();
                }
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (arrastrando) {
                    actualizarPosicion(e.getX());
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                posicionMouse = e.getX();
                repaint();
            }
        });
    }
    
    private void actualizarPosicion(int x) {
        posicionMouse = Math.max(0, Math.min(x, getWidth()));
        
        int nuevoValor = (int) ((posicionMouse / (float) getWidth()) * getMaximum());
        setValue(nuevoValor);
        
        progresoActual = posicionMouse / (float) getWidth();
        progresoObjetivo = progresoActual;
        
        firePropertyChange("progressUpdating", -1, nuevoValor);
        
        repaint();
    }
    
    private void notificarCambio(int x) {
        firePropertyChange("progressBarClicked", -1, x);
    }
    
    private void iniciarAnimacion() {
        animacionTimer = new Timer(16, e -> { // ~60 FPS
            if (!arrastrando) {
                progresoObjetivo = getValue() / (float) getMaximum();
                
                float diferencia = progresoObjetivo - progresoActual;
                if (Math.abs(diferencia) > 0.001f) {
                    progresoActual += diferencia * 0.3f;
                    repaint();
                } else {
                    progresoActual = progresoObjetivo;
                }
            }
        });
        animacionTimer.start();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int ancho = getWidth();
        int alto = getHeight();
        int altoBarra = mouseEncima ? 8 : 6;
        int yBarra = (alto - altoBarra) / 2;
        
        g2d.setColor(colorFondo);
        g2d.fillRoundRect(0, yBarra, ancho, altoBarra, altoBarra, altoBarra);
        
        int anchoProgreso;
        if (arrastrando) {
            anchoProgreso = posicionMouse;
        } else {
            anchoProgreso = (int) (progresoActual * ancho);
        }
        
        if (mouseEncima || arrastrando) {
            g2d.setColor(colorBarraHover);
        } else {
            g2d.setColor(colorBarra);
        }
        
        if (anchoProgreso > 0) {
            g2d.fillRoundRect(0, yBarra, anchoProgreso, altoBarra, altoBarra, altoBarra);
        }
        
        if (mouseEncima || arrastrando) {
            int diametroCirculo = 12;
            int xCirculo = anchoProgreso - diametroCirculo / 2;
            int yCirculo = alto / 2 - diametroCirculo / 2;
            
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fillOval(xCirculo + 1, yCirculo + 1, diametroCirculo, diametroCirculo);
            
            g2d.setColor(colorCirculo);
            g2d.fillOval(xCirculo, yCirculo, diametroCirculo, diametroCirculo);
            
            if (arrastrando) {
                g2d.setColor(colorBarraHover);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(xCirculo, yCirculo, diametroCirculo, diametroCirculo);
            }
        }
        
        g2d.dispose();
    }
    
    @Override
    public void setValue(int n) {
        super.setValue(n);
        if (!arrastrando) {
            progresoObjetivo = n / (float) getMaximum();
        }
    }
    
    public void setColorBarra(Color color) {
        this.colorBarra = color;
        repaint();
    }
    
    public void setColorFondo(Color color) {
        this.colorFondo = color;
        repaint();
    }
    
    public void setColorBarraHover(Color color) {
        this.colorBarraHover = color;
        repaint();
    }
    
    public void setColorCirculo(Color color) {
        this.colorCirculo = color;
        repaint();
    }
    
    public void setColores(Color barra, Color fondo, Color barraHover, Color circulo) {
        this.colorBarra = barra;
        this.colorFondo = fondo;
        this.colorBarraHover = barraHover;
        this.colorCirculo = circulo;
        repaint();
    }
    
    @Override
    public void removeNotify() {
        if (animacionTimer != null) {
            animacionTimer.stop();
        }
        super.removeNotify();
    }
}