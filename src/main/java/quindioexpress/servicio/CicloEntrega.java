package quindioexpress.servicio;

import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;

/**
 * Coordina una entrega completa: selecciona el siguiente paquete de UNA
 * modalidad, asigna un repartidor compatible, lo retira de pendientes y lo
 * agrega al historial. La llamada simula que la entrega termina correctamente.
 *
 * Crear un ciclo por gestor y mantener la modalidad fija evita entregar el
 * mismo paquete desde las dos colas alternativas de GestorDespacho.
 */
public class CicloEntrega {

    public enum Modalidad {
        FIFO,
        PRIORIDAD
    }

    private final CentroDistribucion centro;
    private final GestorDespacho gestorDespacho;
    private final GestorRepartidores gestorRepartidores;
    private final HistorialEntregas historial;
    private final Modalidad modalidad;

    public CicloEntrega(
            CentroDistribucion centro,
            GestorDespacho gestorDespacho,
            GestorRepartidores gestorRepartidores,
            HistorialEntregas historial,
            Modalidad modalidad) {
        if (centro == null || gestorDespacho == null || gestorRepartidores == null
                || historial == null || modalidad == null) {
            throw new DatoInvalidoException("los componentes del ciclo de entrega no pueden ser nulos");
        }

        this.centro = centro;
        this.gestorDespacho = gestorDespacho;
        this.gestorRepartidores = gestorRepartidores;
        this.historial = historial;
        this.modalidad = modalidad;
    }

    /**
     * Procesa el siguiente paquete de la modalidad seleccionada.
     *
     * @return resultado de la entrega; null si esa cola ya esta vacia
     * @throws quindioexpress.excepciones.AsignacionInvalidaException si no hay
     *         un repartidor disponible para el destino; el paquete sigue pendiente
     */
    public ResultadoEntrega procesarSiguienteEntrega() {
        Paquete siguiente = consultarSiguiente();
        if (siguiente == null) {
            return null;
        }

        if (centro.buscarPorCodigo(siguiente.getCodigo()) == null) {
            throw new IllegalStateException(
                    "el paquete " + siguiente.getCodigo() + " no figura como pendiente en el centro");
        }

        // Si la asignacion falla, aun no se ha retirado el paquete de la cola.
        Repartidor repartidor = gestorRepartidores.asignarRepartidor(siguiente);
        Paquete retirado = retirarSiguiente();

        if (retirado != siguiente) {
            repartidor.setDisponible(true);
            throw new IllegalStateException("la cola cambio durante el procesamiento de la entrega");
        }

        if (!centro.eliminarPaquete(siguiente.getCodigo())) {
            repartidor.setDisponible(true);
            throw new IllegalStateException(
                    "no fue posible retirar el paquete entregado del centro");
        }

        historial.registrarEntrega(siguiente);

        // La simulacion representa una entrega completada; el repartidor vuelve
        // a estar disponible para el siguiente paquete de su misma zona.
        repartidor.setDisponible(true);
        return new ResultadoEntrega(siguiente, repartidor);
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    private Paquete consultarSiguiente() {
        return switch (modalidad) {
            case FIFO -> gestorDespacho.verSiguienteFIFO();
            case PRIORIDAD -> gestorDespacho.verSiguientePrioritario();
        };
    }

    private Paquete retirarSiguiente() {
        return switch (modalidad) {
            case FIFO -> gestorDespacho.despacharFIFO();
            case PRIORIDAD -> gestorDespacho.despacharPrioritario();
        };
    }
}
