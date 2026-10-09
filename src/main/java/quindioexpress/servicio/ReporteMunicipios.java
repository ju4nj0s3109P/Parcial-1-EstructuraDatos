package quindioexpress.servicio;

import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Genera reportes de municipios con paquetes registrados y agrupa los paquetes
 * por destino. Los datos se consultan desde el CentroDistribucion asociado.
 */
public class ReporteMunicipios {

    private final CentroDistribucion centroDistribucion;

    /**
     * Crea el generador de reportes para un centro de distribución.
     *
     * @param centroDistribucion centro que contiene los paquetes registrados
     * @throws DatoInvalidoException si el centro es nulo
     */
    public ReporteMunicipios(CentroDistribucion centroDistribucion) {
        if (centroDistribucion == null) {
            throw new DatoInvalidoException("el centro de distribucion no puede ser nulo");
        }
        this.centroDistribucion = centroDistribucion;
    }

    /**
     * Obtiene los municipios con al menos un paquete, sin repetirlos.
     * Se utiliza HashSet para garantizar la unicidad.
     */
    public Set<String> obtenerMunicipiosUnicos() {
        Set<String> municipios = new HashSet<>();
        for (Paquete paquete : centroDistribucion.obtenerPaquetesRegistrados()) {
            municipios.add(paquete.getDestino());
        }
        return Collections.unmodifiableSet(municipios);
    }

    /**
     * Obtiene los municipios sin repetirlos y en orden alfabético.
     * Se utiliza TreeSet para mantener el orden natural de los nombres.
     */
    public Set<String> obtenerMunicipiosOrdenados() {
        Set<String> municipios = new TreeSet<>();
        for (Paquete paquete : centroDistribucion.obtenerPaquetesRegistrados()) {
            municipios.add(paquete.getDestino());
        }
        return Collections.unmodifiableSet(municipios);
    }

    /**
     * Agrupa los paquetes por destino, conservando el orden de registro
     * de los municipios y el de los paquetes dentro de cada grupo.
     */
    public Map<String, List<Paquete>> agruparPaquetesPorMunicipio() {
        Map<String, List<Paquete>> agrupados = new LinkedHashMap<>();

        for (Paquete paquete : centroDistribucion.obtenerPaquetesRegistrados()) {
            agrupados.computeIfAbsent(paquete.getDestino(), destino -> new ArrayList<>())
                    .add(paquete);
        }

        Map<String, List<Paquete>> resultado = new LinkedHashMap<>();
        for (Map.Entry<String, List<Paquete>> entrada : agrupados.entrySet()) {
            resultado.put(entrada.getKey(),
                    Collections.unmodifiableList(new ArrayList<>(entrada.getValue())));
        }
        return Collections.unmodifiableMap(resultado);
    }
}
