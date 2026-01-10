package logica;

import listasDinamicas.CLLAlbum;
import Datos.DALAlbum;
import entidades.Album;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Lógica de negocio para Album - Integra con DAL
 */
public class BLLAlbum {

    public static String obtenerUrlImagenAlbum(int albumId) {
        if (!validarIdAlbum(albumId)) {
            return null;
        }

        String urlImagen = DALAlbum.obtenerUrlImagenAlbum(albumId);

        if (urlImagen == null || urlImagen.trim().isEmpty()) {
            mostrarAdvertencia("No se encontró imagen para este álbum.");
            return null;
        }

        return urlImagen;
    }

    public static int obtenerDuracionTotalAlbum(int albumId) {
        if (!validarIdAlbum(albumId)) {
            return 0;
        }

        int duracionTotal = DALAlbum.obtenerDuracionTotalAlbum(albumId);

        if (duracionTotal <= 0) {
            mostrarAdvertencia("El álbum no tiene canciones o la duración es 0.");
            return 0;
        }

        return duracionTotal;
    }

    public static String obtenerDuracionFormateada(int albumId) {
        int segundosTotales = obtenerDuracionTotalAlbum(albumId);

        if (segundosTotales <= 0) {
            return "00:00";
        }

        int horas = segundosTotales / 3600;
        int minutos = (segundosTotales % 3600) / 60;
        int segundos = segundosTotales % 60;

        if (horas > 0) {
            return String.format("%d:%02d:%02d", horas, minutos, segundos);
        } else {
            return String.format("%d:%02d", minutos, segundos);
        }
    }

    public static List<Album> listarAlbumesPorArtista(int idArtista) {
        if (idArtista <= 0) {
            mostrarAdvertencia("Identificador de artista no válido.");
            return new ArrayList<>();
        }

        List<Album> lista = DALAlbum.listarAlbumesPorArtista(idArtista);

        if (lista.isEmpty()) {
            System.out.println("El artista " + idArtista + " no tiene álbumes registrados.");
        }

        return lista;
    }

    public static void seleccionarAlbum(Album album) {
        if (album == null) {
            mostrarError("No se puede seleccionar un álbum nulo.");
            return;
        }

        CLLAlbum.getInstancia().setAlbumActual(album);
    }

    public static Album obtenerAlbumSeleccionado() {
        return CLLAlbum.getInstancia().getAlbumActual();
    }

    public static boolean hayAlbumSeleccionado() {
        return CLLAlbum.getInstancia().hayAlbumSeleccionado();
    }

    public static void limpiarSeleccion() {
        CLLAlbum.getInstancia().limpiarSeleccion();
    }

    public static void agregarAlbumALista(Album album) {
        if (album == null) {
            mostrarError("No se puede agregar un álbum nulo a la lista.");
            return;
        }

        CLLAlbum.getInstancia().agregarALista(album);
    }

    public static void limpiarListaAlbumes() {
        CLLAlbum.getInstancia().limpiarLista();
    }

    public static Album obtenerAlbumDeLista(int posicion) {
        if (posicion < 0 || posicion >= CLLAlbum.getInstancia().getTamanioLista()) {
            mostrarError("Posición inválida en la lista de álbumes.");
            return null;
        }

        return CLLAlbum.getInstancia().getAlbumDeLista(posicion);
    }

    public static int obtenerTamanioListaAlbumes() {
        return CLLAlbum.getInstancia().getTamanioLista();
    }

    private static boolean validarIdAlbum(int albumId) {
        if (albumId <= 0) {
            mostrarError("El ID del álbum debe ser mayor a 0.");
            return false;
        }

        return true;
    }

    private static void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private static void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Advertencia",
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
