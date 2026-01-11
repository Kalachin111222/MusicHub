/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion;

/**
 *
 * @author ArcosArce
 */

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.swing.JOptionPane;

public class PruebaAudio {

    public static void main(String[] args) {
        System.out.println("1. Iniciando prueba...");
        
        try {
            // Esto prueba si JavaFX está instalado
            new JFXPanel(); 
            System.out.println("2. Motor JavaFX encendido CORRECTAMENTE.");

            // Prueba con un link directo (Usaremos uno de ejemplo que siempre funciona)
            String url = "https://drive.google.com/uc?export=download&id=1jAj_xM6LwIMFAZujg7svYbawXwaTwfEP";
            System.out.println("3. Intentando cargar: " + url);

            Media media = new Media(url);
            MediaPlayer player = new MediaPlayer(media);

            player.setOnReady(() -> {
                System.out.println("4. ¡EXITO! Canción cargada. Duración: " + player.getTotalDuration().toSeconds());
                player.play();
                JOptionPane.showMessageDialog(null, "¡Si lees esto y escuchas música, JavaFX funciona perfecto!");
            });

            player.setOnError(() -> {
                System.out.println("Error en el player: " + player.getError().getMessage());
            });
            
            // Mantenemos el programa vivo para escuchar
            Thread.sleep(10000); 

        } catch (Exception e) {
            System.out.println("❌ ERROR GRAVE: " + e.getMessage());
            e.printStackTrace();
        }
    }
}