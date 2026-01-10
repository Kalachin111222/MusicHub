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

public class GestorAudio {
    
    private MediaPlayer player;
    private boolean enPausa = false;

    // CONSTRUCTOR (VITAL PARA EL SONIDO)
    public GestorAudio() {
        new JFXPanel(); // "Enciende" el motor de audio
    }

    // --- LÓGICA DE REPRODUCCIÓN ---
    public void reproducir(String url) {
        detener(); 

        try {
            System.out.println("--- 🟢 INICIANDO AUDIO ---");
            System.out.println("🔗 URL: " + url);
            
            Media media = new Media(url);
            player = new MediaPlayer(media);
            
            // Debug de errores
            player.setOnError(() -> System.out.println("❌ ERROR: " + player.getError().getMessage()));
            
            // Debug de estado
            player.setOnReady(() -> System.out.println("✅ LISTA. Duración: " + player.getTotalDuration().toSeconds()));
            
            player.play();
            enPausa = false;
            
        } catch (Exception e) {
            System.out.println("❌ EXCEPCIÓN: " + e.getMessage());
        }
    }

    public void pausar() {
        if (player != null) {
            if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                player.pause();
                enPausa = true;
            } else if (enPausa) {
                player.play();
                enPausa = false;
            }
        }
    }

    public void detener() {
        if (player != null) {
            player.stop();
            player.dispose();
            player = null;
        }
    }

    // --- MÉTODOS QUE TE FALTABAN (Barra de Progreso) ---
    
    public boolean estaReproduciendo() {
        return player != null && player.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public double getTiempoActual() {
        if (player != null) {
            return player.getCurrentTime().toSeconds();
        }
        return 0;
    }

    public double getDuracionTotal() {
        if (player != null && player.getTotalDuration() != null) {
            return player.getTotalDuration().toSeconds();
        }
        return 0;
    }
    
    // Método extra por si quieres saltar en la canción (Click en la barra)
    public void saltarA(double segundos) {
        if (player != null) {
            player.seek(javafx.util.Duration.seconds(segundos));
        }
    }
}