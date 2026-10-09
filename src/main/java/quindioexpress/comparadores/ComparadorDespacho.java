package quindioexpress.comparadores;

import quindioexpress.modelo.Paquete;

import java.util.Comparator;

/**
 * Ordena los paquetes para despacho: prioridad descendente, tiempo estimado
 * ascendente y codigo alfabetico ascendente.
 */
public class ComparadorDespacho implements Comparator<Paquete> {

    @Override
    public int compare(Paquete primero, Paquete segundo) {
        int porPrioridad = Integer.compare(
                segundo.getPrioridad(), primero.getPrioridad());
        if (porPrioridad != 0) {
            return porPrioridad;
        }

        int porTiempo = Integer.compare(
                primero.getTiempoEstimado(), segundo.getTiempoEstimado());
        if (porTiempo != 0) {
            return porTiempo;
        }

        return primero.getCodigo().compareTo(segundo.getCodigo());
    }
}
