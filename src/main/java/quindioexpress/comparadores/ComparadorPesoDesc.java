package quindioexpress.comparadores;

import quindioexpress.modelo.Paquete;

import java.util.Comparator;
public class ComparadorPesoDesc implements Comparator<Paquete> {

    /**
     * Compara dos paquetes según su peso, de mayor a menor.
     */
    @Override
    public int compare(Paquete a, Paquete b) {
        return Double.compare(b.getPeso(), a.getPeso());
    }
}
