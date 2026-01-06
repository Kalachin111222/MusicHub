/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion;

import Datos.DALUsuario;
import entidades.Usuario;
import estructuras.Pila;
import java.time.LocalDate;
import javax.swing.JPanel;
import listasDinamicas.CLLUsuario;
import logica.BLLUsuario;
import static logica.BLLUsuario.mostrarError;
import static logica.BLLUsuario.mostrarExito;

/**
 *
 * @author ArcosArce
 */
public class FrmRegistro extends javax.swing.JFrame {

    /**
     * Creates new form Registro
     */
    private Pila<JPanel> historialPaneles;
    private JPanel panelActual;
    
    private panRegistro panRegistro;
    private panPaso1 panPaso1;
    private panPaso2 panPaso2;
    private DatosRegistro datosRegistro;

    public FrmRegistro() {
        initComponents();
        setLocationRelativeTo(null);
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        historialPaneles = new Pila<>();
        datosRegistro = new DatosRegistro(); // AGREGAR ESTA LÍNEA

        // Pasar la referencia del Frame a cada panel
        panRegistro = new panRegistro(this);
        panPaso1 = new panPaso1(this);
        panPaso2 = new panPaso2(this);

        mostrarPanel(panRegistro);
    }
    
    public void mostrarPanel(JPanel panel) {
        panContenido.removeAll();
        
        panel.setSize(panContenido.getSize());
        panel.setLocation(0, 0);
        
        panContenido.add(panel);
        panContenido.revalidate();
        panContenido.repaint();
        
        if (panelActual != null) {
            historialPaneles.push(panelActual);
        }
        panelActual = panel;
    }
    
    public void avanzarPaso(String pasoActual) {
        switch (pasoActual) {
            case "inicial":
                mostrarPanel(panPaso1);
                break;
            case "paso1":
                mostrarPanel(panPaso2);
                break;
            case "paso2":
                finalizarRegistro();
                break;
        }
    }
    
    public void retrocederPaso() {
        if (!historialPaneles.isEmpty()) {
            panelActual = historialPaneles.pop();
            
            panContenido.removeAll();
            panelActual.setSize(panContenido.getSize());
            panelActual.setLocation(0, 0);
            panContenido.add(panelActual);
            panContenido.revalidate();
            panContenido.repaint();
        } else {
            volverAlLogin();
        }
    }
    
    private void finalizarRegistro() {
        // Procesar el registro usando BLL
        boolean exito = procesarRegistro(
            datosRegistro.getNombre(),
            datosRegistro.getEmail(),
            datosRegistro.getPassword(),
            datosRegistro.getConfirmarPassword(),
            datosRegistro.getFechaNacimiento(),
            datosRegistro.getGenero()
        );

        if (exito) {
            // Redirigir a ventana principal
            FrmPrincipal frmPrincipal = new FrmPrincipal();
            frmPrincipal.setVisible(true);
            this.dispose();
        }
        // Si falla, BLL ya muestra el error
    }
    
    private boolean procesarRegistro(String nombre, String email, String password,String confirmarPassword, LocalDate fechaNacimiento,String genero){
        if (DALUsuario.existeEmail(email.trim())) {
            mostrarError("El email ya está registrado.");
            return false;
        }
        Usuario nuevoUsuario = new Usuario(
                nombre.trim(),
                email.trim().toLowerCase(),
                password,
                LocalDate.now(),
                fechaNacimiento,
                genero.trim()
        );
        String mensaje = DALUsuario.insertarUsuario(nuevoUsuario);

        if (mensaje != null) {
            mostrarError("Error al registrar: " + mensaje);
            return false;
        }
        Usuario usuarioRegistrado = DALUsuario.obtenerUsuarioPorEmail(email.trim());
        if (usuarioRegistrado != null) {
            CLLUsuario.getInstancia().setUsuario(usuarioRegistrado);
        }

        mostrarExito("Usuario registrado exitosamente.");
        return true;
    }
    
    public DatosRegistro getDatosRegistro() {
        return datosRegistro;
    }
    
    private void volverAlLogin() {
        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "¿Deseas cancelar el registro?",
            "Confirmar",
            javax.swing.JOptionPane.YES_NO_OPTION
        );
        
        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            FrmLogin frmLogin = new FrmLogin();
            frmLogin.setVisible(true);
            this.dispose();
        }
    }
    
    public void limpiarHistorial() {
        historialPaneles.removeAll();
        panelActual = null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panContenido = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panContenido.setBackground(new java.awt.Color(20, 20, 20));

        javax.swing.GroupLayout panContenidoLayout = new javax.swing.GroupLayout(panContenido);
        panContenido.setLayout(panContenidoLayout);
        panContenidoLayout.setHorizontalGroup(
            panContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1280, Short.MAX_VALUE)
        );
        panContenidoLayout.setVerticalGroup(
            panContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panContenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panContenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(FrmRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmRegistro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panContenido;
    // End of variables declaration//GEN-END:variables
}
