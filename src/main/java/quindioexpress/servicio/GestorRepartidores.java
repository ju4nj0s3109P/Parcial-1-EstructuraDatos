package quindioexpress.servicio;

import quindioexpress.excepciones.AsignacionInvalidaException;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.excepciones.RepartidorDuplicadoException;
import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registra y consulta repartidores, y asigna paquetes respetando la
 * disponibilidad y la zona de cada repartidor.
 */
public class GestorRepartidores {

    /*
     * LinkedHashMap permite buscar por identificacion y conservar el orden
     * de registro, que tambien determina cual repartidor compatible se elige
     * primero.
     */
    private final Map<String, Repartidor> repartidoresPorIdentificacion =
            new LinkedHashMap<>();

    /**
     * Registra un repartidor si su identificacion aun no existe.
     *
     * @param repartidor repartidor que se desea registrar
     * @throws DatoInvalidoException si el repartidor es nulo
     * @throws RepartidorDuplicadoException si su identificacion ya existe
     */
    public void registrarRepartidor(Repartidor repartidor) {
        if (repartidor == null) {
            throw new DatoInvalidoException("el repartidor no puede ser nulo");
        }

        String identificacion = repartidor.getIdentificacion();
        if (repartidoresPorIdentificacion.containsKey(identificacion)) {
            throw new RepartidorDuplicadoException(
                    "ya existe un repartidor con la identificacion: " + identificacion);
        }

        repartidoresPorIdentificacion.put(identificacion, repartidor);
    }

    /**
     * Busca un repartidor por identificacion.
     *
     * @param identificacion identificacion que se desea consultar
     * @return el repartidor encontrado o null si no existe
     * @throws DatoInvalidoException si la identificacion es nula o vacia
     */
    public Repartidor buscarPorIdentificacion(String identificacion) {
        validarIdentificacion(identificacion);
        return repartidoresPorIdentificacion.get(identificacion.trim());
    }

    /**
     * Asigna el paquete al primer repartidor registrado que este disponible
     * y cuya zona coincida con el destino. La asignacion cambia su estado a
     * no disponible para impedir que reciba otra asignacion simultanea.
     *
     * @param paquete paquete que se desea asignar
     * @return repartidor asignado
     * @throws DatoInvalidoException si el paquete es nulo
     * @throws AsignacionInvalidaException si no hay un repartidor compatible
     */
    public Repartidor asignarRepartidor(Paquete paquete) {
        if (paquete == null) {
            throw new DatoInvalidoException("el paquete no puede ser nulo");
        }

        String destino = paquete.getDestino();
        for (Repartidor repartidor : repartidoresPorIdentificacion.values()) {
            if (repartidor.isDisponible()
                    && coincideZona(repartidor.getZona(), destino)) {
                repartidor.setDisponible(false);
                return repartidor;
            }
        }

        throw new AsignacionInvalidaException(
                "no hay repartidores disponibles para el destino: " + destino);
    }

    /**
     * Actualiza la disponibilidad de un repartidor registrado.
     *
     * @param identificacion identificacion del repartidor
     * @param disponible nuevo estado de disponibilidad
     * @return true si se encontro y actualizo el repartidor; false si no existe
     * @throws DatoInvalidoException si la identificacion es nula o vacia
     */
    public boolean actualizarDisponibilidad(String identificacion, boolean disponible) {
        validarIdentificacion(identificacion);
        Repartidor repartidor =
                repartidoresPorIdentificacion.get(identificacion.trim());

        if (repartidor == null) {
            return false;
        }

        repartidor.setDisponible(disponible);
        return true;
    }

    /**
     * Devuelve los repartidores en orden de registro sin permitir modificar
     * la lista interna del gestor.
     */
    public List<Repartidor> obtenerRepartidoresRegistrados() {
        return Collections.unmodifiableList(
                new ArrayList<>(repartidoresPorIdentificacion.values()));
    }

    /** Devuelve la cantidad de repartidores registrados. */
    public int cantidadRepartidores() {
        return repartidoresPorIdentificacion.size();
    }

    /** Indica si no hay repartidores registrados. */
    public boolean estaVacio() {
        return repartidoresPorIdentificacion.isEmpty();
    }

    private void validarIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new DatoInvalidoException(
                    "la identificacion no puede estar vacia");
        }
    }

    private boolean coincideZona(String zona, String destino) {
        return zona.trim().equalsIgnoreCase(destino.trim());
    }
}
