package quindioexpress.servicio;

import quindioexpress.comparadores.ComparadorDespacho;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Administra dos modalidades independientes de atencion:
 * FIFO por orden de llegada y despacho por prioridad.
 *
 * Cada paquete registrado en este gestor se incorpora a ambas estructuras.
 * Las estructuras son vistas independientes para poder comparar las dos
 * modalidades de despacho sobre el mismo conjunto inicial de paquetes.
 */
public class GestorDespacho {

    private final Queue<Paquete> colaFIFO = new ArrayDeque<>();
    private final PriorityQueue<Paquete> colaPrioritaria =
            new PriorityQueue<>(new ComparadorDespacho());

    /**
     * Agrega un paquete al final de la cola FIFO y a la cola prioritaria.
     *
     * @throws DatoInvalidoException si el paquete es nulo
     */
    public void agregarPaquete(Paquete paquete) {
        if (paquete == null) {
            throw new DatoInvalidoException("el paquete no puede ser nulo");
        }

        colaFIFO.offer(paquete);
        colaPrioritaria.offer(paquete);
    }

    /**
     * Retira y devuelve el paquete que lleva mas tiempo esperando.
     * Devuelve null si la cola FIFO esta vacia.
     */
    public Paquete despacharFIFO() {
        return colaFIFO.poll();
    }

    /**
     * Retira y devuelve el paquete con mayor precedencia segun las reglas
     * de despacho prioritario. Devuelve null si no hay paquetes pendientes
     * en esta modalidad.
     */
    public Paquete despacharPrioritario() {
        return colaPrioritaria.poll();
    }

    /**
     * Consulta el siguiente paquete prioritario sin retirarlo de la cola.
     * Devuelve null si la cola prioritaria esta vacia.
     */
    public Paquete verSiguientePrioritario() {
        return colaPrioritaria.peek();
    }

    /** Cantidad de paquetes que quedan en la cola FIFO. */
    public int cantidadPendientesFIFO() {
        return colaFIFO.size();
    }

    /** Cantidad de paquetes que quedan en la cola prioritaria. */
    public int cantidadPendientesPrioritarios() {
        return colaPrioritaria.size();
    }

    /** Indica si la cola FIFO esta vacia. */
    public boolean estaVaciaFIFO() {
        return colaFIFO.isEmpty();
    }

    /** Indica si la cola prioritaria esta vacia. */
    public boolean estaVaciaPrioritaria() {
        return colaPrioritaria.isEmpty();
    }
}
