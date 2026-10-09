package quindioexpress.estructuras;
import java.util.Iterator;
/** Lista simplemente enlazada propia; no utiliza colecciones del JCF para almacenar elementos. */
public class ListaSimple<T> implements Coleccion<T> {
 private Nodo<T> primero; private int tamano;
 public void agregar(T elemento) { throw new UnsupportedOperationException("Pendiente de implementación"); }
 public boolean eliminar(T elemento) { throw new UnsupportedOperationException("Pendiente de implementación"); }
 public T obtener(int indice) { throw new UnsupportedOperationException("Pendiente de implementación"); }
 public int tamano() { return tamano; } public boolean estaVacia() { return tamano==0; }
 public Iterator<T> iterator() { return new IteradorListaSimple<>(primero); }
}
