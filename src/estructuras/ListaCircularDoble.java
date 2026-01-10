/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

/**
 *
 * @author ArcosArce
 */

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.swing.table.DefaultTableModel;

public class ListaCircularDoble<T> {
    private NodoCircularDoble<T> L;
    
    public ListaCircularDoble() {
        L = null;
    }
    
    public boolean esVacia() {
        return L == null;
    }
    
    public NodoCircularDoble<T> getL() {
        return L;
    }
    
    public int contar() {
        if(esVacia()) return 0;
        if(L == L.getSgte()) return 1;

        int c = 1;
        NodoCircularDoble<T> p = L.getSgte();
        while(p != L) {
            c++;
            p = p.getSgte();
        }
        return c;
    }
    
    public int encontrarPos(NodoCircularDoble<T> pos) {
        if(esVacia()) return -1;
        
        int c = 0;
        NodoCircularDoble<T> p = L.getSgte();
        do {
            if(p == pos) {
                return c;
            }
            p = p.getSgte();
            c++;
        } while(p != L.getSgte());
        
        return -1;
    }
    
    public T obtenerPorIndice(int indice) {
        if(esVacia()) return null;
        
        int total = contar();
        if(indice < 0 || indice >= total) return null;
        
        NodoCircularDoble<T> p = L.getSgte();
        int contador = 0;
        
        do {
            if(contador == indice) {
                return p.getInfo();
            }
            p = p.getSgte();
            contador++;
        } while(p != L.getSgte());
        
        return null;
    }
    
    public void recorrer(Consumer<T> accion) {
        if(esVacia()) return;
        
        NodoCircularDoble<T> p = L.getSgte();
        do {
            accion.accept(p.getInfo());
            p = p.getSgte();
        } while(p != L.getSgte());
    }
    
    public void insertar(T x) {
        NodoCircularDoble<T> nuevo = new NodoCircularDoble<>(x);
        
        if(esVacia()) {
            L = nuevo;
            L.setSgte(L);
            L.setAnt(L);
        } else {
            NodoCircularDoble<T> primero = L.getSgte();
            
            nuevo.setSgte(primero);
            nuevo.setAnt(L);
            
            L.setSgte(nuevo);
            primero.setAnt(nuevo);
            
            L = nuevo;
        }
    }
    
    public void insertarAlInicio(T x) {
        NodoCircularDoble<T> nuevo = new NodoCircularDoble<>(x);
        
        if(esVacia()) {
            L = nuevo;
            L.setSgte(L);
            L.setAnt(L);
        } else {
            NodoCircularDoble<T> primero = L.getSgte();
            
            nuevo.setSgte(primero);
            nuevo.setAnt(L);
            
            primero.setAnt(nuevo);
            L.setSgte(nuevo);
        }
    }
    
    public NodoCircularDoble<T> buscar(T dato) {
        if(esVacia()) return null;
        
        NodoCircularDoble<T> p = L.getSgte();
        do {
            if(p.getInfo().equals(dato)) {
                return p;
            }
            p = p.getSgte();
        } while(p != L.getSgte());
        
        return null;
    }
    
    public NodoCircularDoble<T> buscar(T dato, Comparator<T> comparator) {
        if(esVacia()) return null;
        
        NodoCircularDoble<T> p = L.getSgte();
        do {
            if(comparator.compare(p.getInfo(), dato) == 0) {
                return p;
            }
            p = p.getSgte();
        } while(p != L.getSgte());
        
        return null;
    }
    
    public T buscarPorCriterio(Predicate<T> criterio) {
        if(esVacia()) return null;
        
        NodoCircularDoble<T> p = L.getSgte();
        do {
            if(criterio.test(p.getInfo())) {
                return p.getInfo();
            }
            p = p.getSgte();
        } while(p != L.getSgte());
        
        return null;
    }
    
    public boolean modificar(T valorAntiguo, T valorNuevo) {
        NodoCircularDoble<T> nodo = buscar(valorAntiguo);
        if(nodo != null) {
            nodo.setInfo(valorNuevo);
            return true;
        }
        return false;
    }

    public boolean modificar(T valorAntiguo, T valorNuevo, Comparator<T> comparator) {
        NodoCircularDoble<T> nodo = buscar(valorAntiguo, comparator);
        if(nodo != null) {
            nodo.setInfo(valorNuevo);
            return true;
        }
        return false;
    }
    
    public boolean eliminar(T dato) {
        if(esVacia()) return false;
        
        NodoCircularDoble<T> p = L.getSgte();
        
        do {
            if(p.getInfo().equals(dato)) {
                if(p == p.getSgte()) {
                    L = null;
                    return true;
                }
                
                NodoCircularDoble<T> anterior = p.getAnt();
                NodoCircularDoble<T> siguiente = p.getSgte();
                
                anterior.setSgte(siguiente);
                siguiente.setAnt(anterior);
                
                if(p == L) {
                    L = anterior;
                }
                
                return true;
            }
            p = p.getSgte();
        } while(p != L.getSgte());
        
        return false;
    }
    
    public void ordenar(Comparator<T> comparator) {
        if(esVacia() || L == L.getSgte()) {
            return;
        }
        
        NodoCircularDoble<T> p = L.getSgte();
        do {
            NodoCircularDoble<T> q = p.getSgte();
            do {
                if(comparator.compare(p.getInfo(), q.getInfo()) > 0) {
                    T aux = p.getInfo();
                    p.setInfo(q.getInfo());
                    q.setInfo(aux);
                }
                q = q.getSgte();
            } while(q != L.getSgte());
            p = p.getSgte();
        } while(p != L);
    }
    
    @SuppressWarnings("unchecked")
    public void ordenar() {
        if(esVacia() || L.getSgte() == null) {
            return;
        }
        
        try {
            ordenar((o1, o2) -> ((Comparable<T>) o1).compareTo(o2));
        } catch(ClassCastException e) {
            throw new UnsupportedOperationException(
                "El tipo " + L.getInfo().getClass().getSimpleName() + 
                " no implementa Comparable. Use ordenar(Comparator<T> comparator)");
        }
    }
    
    public void mostrarEnTabla(DefaultTableModel modelo, String[] titulos, Function<T, Object>... extractores) {
        if(esVacia()) {
            modelo.setDataVector(new Object[0][extractores.length], titulos);
            return;
        }

        int totalElementos = contar();
        Object datos[][] = new Object[totalElementos][extractores.length];

        NodoCircularDoble<T> p = L.getSgte();
        int fila = 0;
        do {
            for(int columna = 0; columna < extractores.length; columna++) {
                datos[fila][columna] = extractores[columna].apply(p.getInfo());
            }
            p = p.getSgte();
            fila++;
        } while(p != L.getSgte());

        modelo.setDataVector(datos, titulos);
    }

    public void mostrarEnTablaSi(DefaultTableModel modelo, String[] titulos, Predicate<T> criterio, Function<T, Object>... extractores) {
        if(esVacia()) {
            modelo.setDataVector(new Object[0][extractores.length], titulos);
            return;
        }
        
        int elementosQueCumplen = 0;
        NodoCircularDoble<T> p = L.getSgte();
        do {
            if(criterio.test(p.getInfo())) {
                elementosQueCumplen++;
            }
            p = p.getSgte();
        } while(p != L.getSgte());

        Object datos[][] = new Object[elementosQueCumplen][extractores.length];
        p = L.getSgte();
        int fila = 0;
        do {
            if(criterio.test(p.getInfo())) {
                for(int columna = 0; columna < extractores.length; columna++) {
                    datos[fila][columna] = extractores[columna].apply(p.getInfo());
                }
                fila++;
            }
            p = p.getSgte();
        } while(p != L.getSgte());

        modelo.setDataVector(datos, titulos);
    }
    
    public int contarSi(Predicate<T> criterio) {
        if(esVacia()) return 0;
        
        int contador = 0;
        NodoCircularDoble<T> p = L.getSgte();
        do {
            if(criterio.test(p.getInfo())) {
                contador++;
            }
            p = p.getSgte();
        } while(p != L.getSgte());
        
        return contador;
    }
    
    public T obtenerPrimero() {
        if(esVacia()) return null;
        return L.getSgte().getInfo();
    }

    public T obtenerUltimo() {
        if(esVacia()) return null;
        return L.getInfo();
    }

    public void recorrerHaciaAdelanteSi(Predicate<T> filtro, Consumer<T> accion) {
        if(esVacia()) return;

        NodoCircularDoble<T> p = L.getSgte();
        do {
            if(filtro.test(p.getInfo())) {
                accion.accept(p.getInfo());
            }
            p = p.getSgte();
        } while(p != L.getSgte());
    }

    public void recorrerHaciaAtrasSi(Predicate<T> filtro, Consumer<T> accion) {
        if(esVacia()) return;

        NodoCircularDoble<T> p = L;
        do {
            if(filtro.test(p.getInfo())) {
                accion.accept(p.getInfo());
            }
            p = p.getAnt();
        } while(p != L);
    }
    
}