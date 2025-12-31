/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentacion;

import entidades.Usuario;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import logica.BLLUsuario;

/**
 *
 * @author ArcosArce
 */
public class FrmLogin extends javax.swing.JFrame {

    /**
     * Creates new form FrmLogin
     */
    private final String PLACEHOLDER_EMAIL = "Correo electrónico";
    private final String PLACEHOLDER_PASSWORD = "Contraseña";
    
    // Colores
    private final Color COLOR_PLACEHOLDER = new Color(180, 180, 180);
    private final Color COLOR_TEXTO = new Color(255, 255, 255);

    public FrmLogin() {
        initComponents();
        setLocationRelativeTo(null);
        configurarPlaceholders();
        configurarEventos();
        btnIniciar.requestFocus();
    }
    
    private void configurarPlaceholders() {
        // Configurar placeholder del correo
        txtCorreo.setText(PLACEHOLDER_EMAIL);
        txtCorreo.setForeground(COLOR_PLACEHOLDER);
        
        txtCorreo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtCorreo.getText().equals(PLACEHOLDER_EMAIL)) {
                    txtCorreo.setText("");
                    txtCorreo.setForeground(COLOR_TEXTO);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtCorreo.getText().trim().isEmpty()) {
                    txtCorreo.setText(PLACEHOLDER_EMAIL);
                    txtCorreo.setForeground(COLOR_PLACEHOLDER);
                }
            }
        });

        // Configurar placeholder de la contraseña
        txtContraseña.setEchoChar((char) 0); // Mostrar texto normal inicialmente
        txtContraseña.setText(PLACEHOLDER_PASSWORD);
        txtContraseña.setForeground(COLOR_PLACEHOLDER);
        
        txtContraseña.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(txtContraseña.getPassword()).equals(PLACEHOLDER_PASSWORD)) {
                    txtContraseña.setText("");
                    txtContraseña.setForeground(COLOR_TEXTO);
                    txtContraseña.setEchoChar('•'); // Activar puntos para contraseña
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(txtContraseña.getPassword()).trim().isEmpty()) {
                    txtContraseña.setEchoChar((char) 0); // Desactivar puntos
                    txtContraseña.setText(PLACEHOLDER_PASSWORD);
                    txtContraseña.setForeground(COLOR_PLACEHOLDER);
                }
            }
        });
    }

    /**
     * Configura los eventos de los labels clickeables
     */
    private void configurarEventos() {
        // Evento para el label de Registro
        lblRegistro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRegistro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                abrirRegistro();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblRegistro.setForeground(new Color(29, 185, 84)); // Color verde hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblRegistro.setForeground(Color.WHITE);
            }
        });

        // Evento para el label de Restablecer contraseña
        lblRestablecer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblRestablecer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                restablecerContraseña();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                lblRestablecer.setForeground(new Color(29, 185, 84)); // Color verde hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblRestablecer.setForeground(Color.WHITE);
            }
        });
    }
    
    private void abrirRegistro() {
        FrmRegistro frmRegistro = new FrmRegistro();
        frmRegistro.setVisible(true);
        this.dispose();
    }

    /**
     * Abre el formulario para restablecer contraseña
     */
    private void restablecerContraseña() {
        // Por ahora mostramos un mensaje simple
        // Puedes crear un FrmRestablecerPassword más adelante
        String email = JOptionPane.showInputDialog(
            this,
            "Ingrese su correo electrónico:",
            "Restablecer Contraseña",
            JOptionPane.QUESTION_MESSAGE
        );

        if (email != null && !email.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Se ha enviado un enlace de recuperación a: " + email,
                "Información",
                JOptionPane.INFORMATION_MESSAGE
            );
            // Aquí irían las llamadas a BLL para enviar email de recuperación
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
        jPanel1 = new PanelPersonalizado();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        txtContraseña = new javax.swing.JPasswordField();
        lblRestablecer = new javax.swing.JLabel();
        btnIniciar = new BotonPersonalizado();
        jLabel4 = new javax.swing.JLabel();
        lblRegistro = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panFondo.setBackground(new java.awt.Color(20, 20, 20));

        jPanel1.setBackground(new java.awt.Color(80, 80, 80));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Inicia sesión para");

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("continuar");

        txtCorreo.setBackground(new java.awt.Color(153, 153, 153));
        txtCorreo.setForeground(new java.awt.Color(255, 255, 255));
        txtCorreo.setText("Correo electrónico o nombre de usuario");

        txtContraseña.setBackground(new java.awt.Color(153, 153, 153));
        txtContraseña.setForeground(new java.awt.Color(255, 255, 255));
        txtContraseña.setText("Contraseña");

        lblRestablecer.setForeground(new java.awt.Color(255, 255, 255));
        lblRestablecer.setText("Restablecer contraseña");

        btnIniciar.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btnIniciar.setText("Iniciar Sesión");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("¿No tienes una cuenta?");

        lblRegistro.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblRegistro.setForeground(new java.awt.Color(255, 255, 255));
        lblRegistro.setText("REGÍSTRATE");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblRestablecer)
                    .addComponent(txtCorreo, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                    .addComponent(txtContraseña)
                    .addComponent(btnIniciar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(lblRegistro)))
                .addGap(45, 45, 45))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblRestablecer)
                .addGap(32, 32, 32)
                .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblRegistro))
                .addGap(57, 57, 57))
        );

        javax.swing.GroupLayout panFondoLayout = new javax.swing.GroupLayout(panFondo);
        panFondo.setLayout(panFondoLayout);
        panFondoLayout.setHorizontalGroup(
            panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFondoLayout.createSequentialGroup()
                .addGap(480, 480, 480)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(480, Short.MAX_VALUE))
        );
        panFondoLayout.setVerticalGroup(
            panFondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFondoLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(145, Short.MAX_VALUE))
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

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        String email = obtenerTextoCorreo();
        String password = obtenerTextoPassword();
        
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor complete todos los campos",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        
        Usuario usuario = BLLUsuario.procesarLogin(email, password);
        
        if (usuario != null) {
            JOptionPane.showMessageDialog(
                this,
                "¡Bienvenido, " + usuario.getNombre() + "!",
                "Login Exitoso",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            abrirPantallaPrincipal();
        }
    }//GEN-LAST:event_btnIniciarActionPerformed

    private String obtenerTextoCorreo() {
        String texto = txtCorreo.getText().trim();
        if (texto.equals(PLACEHOLDER_EMAIL)) {
            return "";
        }
        return texto;
    }
    private String obtenerTextoPassword() {
        String texto = String.valueOf(txtContraseña.getPassword()).trim();
        if (texto.equals(PLACEHOLDER_PASSWORD)) {
            return "";
        }
        return texto;
    }
    
    private void abrirPantallaPrincipal() {
        FrmPrincipal frmPrincipal = new FrmPrincipal();
        frmPrincipal.setVisible(true);
        this.dispose();
    }

    /**
     * Limpia los campos del formulario
     */
    private void limpiarCampos() {
        txtCorreo.setText(PLACEHOLDER_EMAIL);
        txtCorreo.setForeground(COLOR_PLACEHOLDER);
        
        txtContraseña.setEchoChar((char) 0);
        txtContraseña.setText(PLACEHOLDER_PASSWORD);
        txtContraseña.setForeground(COLOR_PLACEHOLDER);
    }
    
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
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblRegistro;
    private javax.swing.JLabel lblRestablecer;
    private javax.swing.JPanel panFondo;
    private javax.swing.JPasswordField txtContraseña;
    private javax.swing.JTextField txtCorreo;
    // End of variables declaration//GEN-END:variables
}
