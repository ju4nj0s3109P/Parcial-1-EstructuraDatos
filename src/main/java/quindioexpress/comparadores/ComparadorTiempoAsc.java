package quindioexpress.comparadores;

import quindioexpress.modelo.Paquete;

import java.util.Comparator;

/**
 * alternativo de ordenamiento: tiempo estimado de MENOR a MAYOR
 */
public class ComparadorTiempoAsc implements Comparator<Paquete> {

    /**
     * Compara dos paquetes según su tiempo estimado, de menor a mayor
     */
    @Override
    public int compare(Paquete a, Paquete b) {
        return Integer.compare(a.getTiempoEstimado(), b.getTiempoEstimado());
    }
}
