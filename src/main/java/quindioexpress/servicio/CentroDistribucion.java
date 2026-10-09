package quindioexpress.servicio;

import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.excepciones.PaqueteDuplicadoException;
import quindioexpress.modelo.Paquete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * coordina el registro, la consulta y la eliminacion de paquetes.
 * utiliza un mapa para consultar por codigo y una lista para conservar
 * el orden en que se registraron los paquetes.
 */
public class CentroDistribucion {

    private final Map<String, Paquete> paquetesPorCodigo = new HashMap<>();
    private final List<Paquete> ordenRegistro = new ArrayList<>();

    /**
     * registra un paquete y conserva su posicion de llegada.
     *
     * @param paquete paquete que se desea registrar
     * @throws DatoInvalidoException si el paquete es nulo
     * @throws PaqueteDuplicadoException si ya existe un paquete con ese codigo
     */
    public void registrarPaquete(Paquete paquete) {
        if (paquete == null) {
            throw new DatoInvalidoException("el paquete no puede ser nulo");
        }

        String codigo = paquete.getCodigo();
        if (paquetesPorCodigo.containsKey(codigo)) {
            throw new PaqueteDuplicadoException(
                    "ya existe un paquete registrado con el codigo: " + codigo);
        }

        paquetesPorCodigo.put(codigo, paquete);
        ordenRegistro.add(paquete);
    }

    /**
     * busca un paquete por su codigo.
     *
     * @param codigo codigo del paquete
     * @return el paquete encontrado o null si no existe
     * @throws DatoInvalidoException si el codigo es nulo o esta vacio
     */
    public Paquete buscarPorCodigo(String codigo) {
        validarCodigo(codigo);
        return paquetesPorCodigo.get(codigo.trim());
    }

    /**
     * elimina el paquete con el codigo indicado.
     *
     * @param codigo codigo del paquete que se desea eliminar
     * @return true si se elimino un paquete; false si no existia
     * @throws DatoInvalidoException si el codigo es nulo o esta vacio
     */
    public boolean eliminarPaquete(String codigo) {
        validarCodigo(codigo);
        Paquete eliminado = paquetesPorCodigo.remove(codigo.trim());

        if (eliminado == null) {
            return false;
        }

        ordenRegistro.remove(eliminado);
        return true;
    }

    /**
     * devuelve una vista no modificable de los paquetes en orden de registro.
     */
    public List<Paquete> obtenerPaquetesRegistrados() {
        return Collections.unmodifiableList(new ArrayList<>(ordenRegistro));
    }

    /** devuelve la cantidad de paquetes registrados. */
    public int cantidadPaquetes() {
        return ordenRegistro.size();
    }

    /** indica si no hay paquetes registrados. */
    public boolean estaVacio() {
        return ordenRegistro.isEmpty();
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DatoInvalidoException("el codigo no puede estar vacio");
        }
    }
}
