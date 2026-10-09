package quindioexpress.estructuras;

import java.util.Iterator;
import java.util.Objects;

/** lista simplemente enlazada propia; no utiliza colecciones del jcf para almacenar elementos. */
public class ListaSimple<T> implements Coleccion<T> {
    private Nodo<T> primero;
    private int tamano;

    /** agrega un elemento al final de la lista. */
    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);

        if (primero == null) {
            primero = nuevo;
        } else {
            Nodo<T> actual = primero;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }

        tamano++;
    }

    /** elimina la primera aparición del elemento y devuelve si lo encontró. */
    @Override
    public boolean eliminar(T elemento) {
        if (primero == null) {
            return false;
        }

        if (Objects.equals(primero.valor, elemento)) {
            primero = primero.siguiente;
            tamano--;
            return true;
        }

        Nodo<T> anterior = primero;
        Nodo<T> actual = primero.siguiente;

        while (actual != null) {
            if (Objects.equals(actual.valor, elemento)) {
                anterior.siguiente = actual.siguiente;
                tamano--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }

        return false;
    }

    /** devuelve el elemento ubicado en el índice indicado, empezando desde cero. */
    @Override
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("índice fuera de los límites de la lista: " + indice);
        }

        Nodo<T> actual = primero;
        for (int i = 0; i < indice; i++) {
            actual = actual.siguiente;
        }
        return actual.valor;
    }

    /** devuelve la cantidad de elementos almacenados. */
    @Override
    public int tamano() {
        return tamano;
    }

    /** indica si la lista no contiene elementos. */
    @Override
    public boolean estaVacia() {
        return tamano == 0;
    }

    /** devuelve un iterador que recorre la lista desde el primer elemento. */
    @Override
    public Iterator<T> iterator() {
        return new IteradorListaSimple<>(primero);
    }
}
