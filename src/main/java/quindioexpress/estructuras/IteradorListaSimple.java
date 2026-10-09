package quindioexpress.estructuras;
import java.util.Iterator; import java.util.NoSuchElementException;
/** Itera sin exponer nodos. */
final class IteradorListaSimple<T> implements Iterator<T> { private Nodo<T> actual; IteradorListaSimple(Nodo<T> primero){actual=primero;} public boolean hasNext(){return actual!=null;} public T next(){if(!hasNext())throw new NoSuchElementException("No hay más elementos"); T valor=actual.valor; actual=actual.siguiente; return valor;} }
