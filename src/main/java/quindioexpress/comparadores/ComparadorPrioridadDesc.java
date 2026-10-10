package quindioexpress.comparadores;

import quindioexpress.modelo.Paquete;

import java.util.Comparator;

/**
 * Comparador de paquetes por prioridad, de mayor a menor.
 */
public class ComparadorPrioridadDesc implements Comparator<Paquete> {
    @Override
    public int compare(Paquete a, Paquete b) {
        return Integer.compare(b.getPrioridad(), a.getPrioridad());
    }
}