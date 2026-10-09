package quindioexpress.servicio;

import quindioexpress.estructuras.ListaSimple;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.Iterator;

/**
 * Conserva los paquetes entregados en el orden real de entrega.
 * El almacenamiento se realiza con la lista simplemente enlazada propia.
 */
public class HistorialEntregas implements Iterable<Paquete> {

    private final ListaSimple<Paquete> entregas = new ListaSimple<>();

    /**
     * Registra una entrega al final del historial.
     *
     * @param paquete paquete entregado satisfactoriamente
     * @throws DatoInvalidoException si el paquete es nulo
     */
    public void registrarEntrega(Paquete paquete) {
        if (paquete == null) {
            throw new DatoInvalidoException("el paquete entregado no puede ser nulo");
        }
        entregas.agregar(paquete);
    }

    /**
     * Elimina del historial la primera aparición del paquete indicado.
     *
     * @return true si el paquete estaba registrado; false en caso contrario
     */
    public boolean eliminarEntrega(Paquete paquete) {
        return entregas.eliminar(paquete);
    }

    /**
     * Obtiene una entrega por su posición, empezando desde cero.
     *
     * @throws IndexOutOfBoundsException si el índice no existe
     */
    public Paquete obtenerEntrega(int indice) {
        return entregas.obtener(indice);
    }

    /** Devuelve la cantidad de entregas registradas. */
    public int cantidadEntregas() {
        return entregas.tamano();
    }

    /** Indica si el historial no contiene entregas. */
    public boolean estaVacio() {
        return entregas.estaVacia();
    }

    /**
     * Permite recorrer las entregas en orden sin exponer los nodos internos.
     */
    @Override
    public Iterator<Paquete> iterator() {
        return entregas.iterator();
    }
}
