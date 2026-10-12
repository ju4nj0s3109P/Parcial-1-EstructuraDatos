package quindioexpress.comparadores;

import quindioexpress.modelo.Paquete;

import java.util.Comparator;

public class ComparadorDespacho implements Comparator<Paquete> {

    /** Primer criterio: prioridad descendente. */
    private final Comparator<Paquete> porPrioridad = new ComparadorPrioridadDesc();

    /** Segundo criterio: tiempo estimado ascendente. */
    private final Comparator<Paquete> porTiempo = new ComparadorTiempoAsc();

    @Override
    public int compare(Paquete primero, Paquete segundo) {
        int resultado = porPrioridad.compare(primero, segundo);
        if (resultado != 0) {
            return resultado;
        }

        resultado = porTiempo.compare(primero, segundo);
        if (resultado != 0) {
            return resultado;
        }

        return primero.compareTo(segundo);
    }
}