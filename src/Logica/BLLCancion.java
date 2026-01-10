package logica;

import listasDinamicas.CLLCancion;
import listasDinamicas.CLLUsuario;
import Datos.DALCancion;
import entidades.Cancion;
import entidades.Artista;
import entidades.Album;
import javax.swing.JOptionPane;
import java.util.List;

/**
 * Lógica de negocio para Cancion - Integra con DAL
 */
public class BLLCancion {
    
    
    public static boolean insertarCancion(String titulo, String duracionStr, String genero,
                                         Artista artista, Album album, String urlAudio) {
        if (!validarDatosCancion(titulo, duracionStr, genero, artista)) {
            return false;
        }
        
        if (urlAudio == null || urlAudio.trim().isEmpty()) {
            mostrarError("Debe proporcionar la URL del audio.");
            return false;
        }
        
        int duracion = Integer.parseInt(duracionStr.trim());
        
        Cancion nuevaCancion = new Cancion();
        nuevaCancion.setTitulo(titulo.trim());
        nuevaCancion.setDuracion(duracion);
        nuevaCancion.setGenero(genero.trim());
        nuevaCancion.setArtista(artista);
        nuevaCancion.setAlbum(album);
        nuevaCancion.setUrlAudio(urlAudio.trim());
        nuevaCancion.setNumeroReproducciones(0);
        
        String mensaje = DALCancion.insertarCancion(nuevaCancion);
        
        if (mensaje != null) {
            mostrarError("Error al insertar canción: " + mensaje);
            return false;
        }
        
        mostrarExito("Canción agregada exitosamente.");
        return true;
    }
   
    
    public static Cancion obtenerCancionPorId(int id) {
        if (id <= 0) {
            mostrarError("ID de canción inválido.");
            return null;
        }
        
        Cancion cancion = DALCancion.obtenerCancionPorId(id);
        
        if (cancion == null) {
            mostrarAdvertencia("No se encontró la canción con ID: " + id);
        }
        
        return cancion;
    }
    
    
    public static boolean actualizarCancion(Cancion cancion, String titulo, String duracionStr,
                                           String genero, Artista artista, Album album, String urlAudio) {

        if (!validarDatosCancion(titulo, duracionStr, genero, artista)) {
            return false;
        }
        
        if (urlAudio == null || urlAudio.trim().isEmpty()) {
            mostrarError("Debe proporcionar la URL del audio.");
            return false;
        }
        

        int duracion = Integer.parseInt(duracionStr.trim());
        
        cancion.setTitulo(titulo.trim());
        cancion.setDuracion(duracion);
        cancion.setGenero(genero.trim());
        cancion.setArtista(artista);
        cancion.setAlbum(album);
        cancion.setUrlAudio(urlAudio.trim());
        
        String mensaje = DALCancion.actualizarCancion(cancion);
        
        if (mensaje != null) {
            mostrarError("Error al actualizar canción: " + mensaje);
            return false;
        }
        
        mostrarExito("Canción actualizada exitosamente.");
        return true;
    }
    
    
    public static List<Cancion> buscarCanciones(String criterio, String valor) {
        if (!validarCriterioBusqueda(valor)) {
            return null;
        }
        
        List<Cancion> resultados = DALCancion.buscarCancionesPorTitulo(valor.trim());
        
        if (resultados != null && !resultados.isEmpty()) {
            CLLCancion.getInstancia().setListaCanciones(resultados);
        } else {
            mostrarAdvertencia("No se encontraron canciones con ese criterio.");
        }
        
        return resultados;
    }
    
    public static List<Cancion> obtenerCancionesParaMostrar() {
        List<Cancion> canciones = DALCancion.listarCancionesMasPopulares();
        
        if (canciones != null && !canciones.isEmpty()) {
            CLLCancion.getInstancia().setListaCanciones(canciones);
        }
        
        return canciones;
    }
    
    public static List<Cancion> ordenarCanciones(List<Cancion> canciones, String criterio) {
        if (canciones == null || canciones.isEmpty()) {
            return canciones;
        }
        
        switch (criterio.toLowerCase()) {
            case "titulo":
                canciones.sort((c1, c2) -> c1.getTitulo().compareToIgnoreCase(c2.getTitulo()));
                break;
            case "reproducciones":
                canciones.sort((c1, c2) -> Integer.compare(c2.getNumeroReproducciones(), c1.getNumeroReproducciones()));
                break;
            case "duracion":
                canciones.sort((c1, c2) -> Integer.compare(c1.getDuracion(), c2.getDuracion()));
                break;
            default:
                mostrarAdvertencia("Criterio de ordenamiento no reconocido.");
        }
        
        return canciones;
    }
    
    
    public static List<Cancion> listarCancionesPorArtista(int idArtista) {
        if (idArtista <= 0) {
            mostrarError("ID de artista inválido.");
            return null;
        }
        
        List<Cancion> canciones = DALCancion.listarCancionesPorArtista(idArtista);
        
        if (canciones != null && !canciones.isEmpty()) {
            CLLCancion.getInstancia().setListaCanciones(canciones);
        } else {
            mostrarAdvertencia("El artista no tiene canciones.");
        }
        
        return canciones;
    }
    
    
    public static List<Cancion> listarCancionesPorAlbum(int idAlbum) {
        if (idAlbum <= 0) {
            mostrarError("ID de álbum inválido.");
            return null;
        }
        
        List<Cancion> canciones = DALCancion.listarCancionesPorAlbum(idAlbum);
        
        if (canciones != null && !canciones.isEmpty()) {
            CLLCancion.getInstancia().setListaCanciones(canciones);
        } else {
            mostrarAdvertencia("El álbum no tiene canciones.");
        }
        
        return canciones;
    }
    
    
    public static boolean incrementarReproducciones(int cancionId) {
        if (cancionId <= 0) {
            mostrarError("ID de canción inválido.");
            return false;
        }
        
        String mensaje = DALCancion.incrementarReproducciones(cancionId);
        
        if (mensaje != null) {
            System.out.println("Advertencia al incrementar reproducciones: " + mensaje);
            return false;
        }
        
        return true;
    }
    
    
    public static List<Cancion> listarCancionesMasPopulares() {
        List<Cancion> canciones = DALCancion.listarCancionesMasPopulares();
        
        if (canciones != null && !canciones.isEmpty()) {
            CLLCancion.getInstancia().setListaCanciones(canciones);
        }
        
        return canciones;
    }
    
    
    public static List<Cancion> listarCancionesPorGeneroNoEscuchadas(int usuarioId, String genero) {
  
        if (!CLLUsuario.getInstancia().hayUsuarioLogueado()) {
            mostrarError("Debe iniciar sesión.");
            return null;
        }
        
        if (usuarioId <= 0) {
            mostrarError("ID de usuario inválido.");
            return null;
        }
        
 
        if (genero == null || genero.trim().isEmpty()) {
            mostrarError("El género no puede estar vacío.");
            return null;
        }
        
        List<Cancion> canciones = DALCancion.listarCancionesPorGeneroNoEscuchadas(usuarioId, genero.trim());
        
        if (canciones != null && !canciones.isEmpty()) {
            CLLCancion.getInstancia().setListaCanciones(canciones);
        } else {
            mostrarAdvertencia("No hay canciones nuevas de este género para ti.");
        }
        
        return canciones;
    }
    
    
    public static String obtenerUrlAudioCancion(int cancionId) {
        if (cancionId <= 0) {
            mostrarError("ID de canción inválido.");
            return null;
        }
        
        String urlAudio = DALCancion.obtenerUrlAudioCancion(cancionId);
        
        if (urlAudio == null || urlAudio.trim().isEmpty()) {
            mostrarError("No se encontró la URL de audio para esta canción.");
            return null;
        }
        
        return urlAudio;
    }
    
    
    public static boolean reproducirCancion(Cancion cancion, int idUsuario) {
        if (!validarReproduccion(cancion, idUsuario)) {
            return false;
        }
        
        CLLCancion.getInstancia().setCancionActual(cancion);
        
        incrementarReproducciones(cancion.getId());
        
        return true;
    }
    
    private static boolean validarReproduccion(Cancion cancion, int idUsuario) {
        if (cancion == null) {
            mostrarError("No hay canción seleccionada.");
            return false;
        }
        
        if (!CLLUsuario.getInstancia().hayUsuarioLogueado()) {
            mostrarError("Debe iniciar sesión para reproducir.");
            return false;
        }
        
        return true;
    }
    
    
    public static Cancion siguienteCancion() {
        Cancion siguiente = CLLCancion.getInstancia().siguienteCancion();
        
        if (siguiente == null) {
            mostrarAdvertencia("No hay más canciones en la lista.");
        }
        
        return siguiente;
    }
    
    public static Cancion anteriorCancion() {
        Cancion anterior = CLLCancion.getInstancia().anteriorCancion();
        
        if (anterior == null) {
            mostrarAdvertencia("No hay canciones anteriores en la lista.");
        }
        
        return anterior;
    }
    
    public static void detenerReproduccion() {
        CLLCancion.getInstancia().detenerReproduccion();
    }
    
    public static Cancion getCancionActual() {
        return CLLCancion.getInstancia().getCancionActual();
    }
    
    public static boolean hayCancionReproduciendose() {
        return CLLCancion.getInstancia().hayCancionReproduciendose();
    }
    
    
    public static void agregarALista(Cancion cancion) {
        if (cancion != null) {
            CLLCancion.getInstancia().agregarALista(cancion);
        }
    }
    
    public static void limpiarLista() {
        CLLCancion.getInstancia().limpiarLista();
    }
    
    public static List<Cancion> getListaCanciones() {
        return CLLCancion.getInstancia().getListaCanciones();
    }
    
    public static int getTamanioLista() {
        return CLLCancion.getInstancia().getTamanioLista();
    }
    
    
    private static boolean validarDatosCancion(String titulo, String duracionStr,
                                              String genero, Artista artista) {
        if (!validarTitulo(titulo)) {
            return false;
        }
        
        if (!validarDuracion(duracionStr)) {
            return false;
        }
        
        if (!validarGenero(genero)) {
            return false;
        }
        
        if (artista == null) {
            mostrarError("Debe seleccionar un artista.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            mostrarError("El título no puede estar vacío.");
            return false;
        }
        
        if (titulo.trim().length() > 100) {
            mostrarError("El título no puede exceder 100 caracteres.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarDuracion(String duracionStr) {
        if (duracionStr == null || duracionStr.trim().isEmpty()) {
            mostrarError("La duración no puede estar vacía.");
            return false;
        }
        
        try {
            int duracion = Integer.parseInt(duracionStr.trim());
            
            if (duracion <= 0) {
                mostrarError("La duración debe ser mayor a 0.");
                return false;
            }
            
            if (duracion > 3600) {
                mostrarError("La duración no puede exceder 3600 segundos.");
                return false;
            }
            
        } catch (NumberFormatException e) {
            mostrarError("La duración debe ser un número válido.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            mostrarError("El género no puede estar vacío.");
            return false;
        }
        
        if (genero.trim().length() > 50) {
            mostrarError("El género no puede exceder 50 caracteres.");
            return false;
        }
        
        return true;
    }
    
    private static boolean validarCriterioBusqueda(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            mostrarError("Ingrese un criterio de búsqueda.");
            return false;
        }
        
        if (criterio.trim().length() < 2) {
            mostrarError("El criterio debe tener al menos 2 caracteres.");
            return false;
        }
        
        return true;
    }
    
    // ===== UTILIDADES =====
    
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
    
    private static void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(
            null,
            mensaje,
            "Éxito",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}