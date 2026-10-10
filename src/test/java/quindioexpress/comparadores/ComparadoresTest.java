package quindioexpress.comparadores;

import org.junit.jupiter.api.Test;
import quindioexpress.modelo.Paquete;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de los criterios de comparación
 */
class ComparadoresTest {

    /** Atajo para crear paquetes de prueba con destino fijo. */
    private Paquete paquete(String codigo, double peso, int prioridad, int tiempo) {
        return new Paquete(codigo, "Armenia", peso, prioridad, tiempo);
    }

    /** Extrae los códigos de una lista para comparar resultados fácilmente. */
    private List<String> codigos(List<Paquete> paquetes) {
        List<String> codigos = new ArrayList<>();
        for (Paquete p : paquetes) {
            codigos.add(p.getCodigo());
        }
        return codigos;
    }

    /** El de prioridad 5 debe ir antes que el de prioridad 2, y al revés da positivo. */
    @Test
    void prioridadDescPoneAntesElDeMayorPrioridad() {
        ComparadorPrioridadDesc comparador = new ComparadorPrioridadDesc();
        Paquete alta = paquete("PQ001", 1.0, 5, 10);
        Paquete baja = paquete("PQ002", 1.0, 2, 10);

        assertTrue(comparador.compare(alta, baja) < 0);
        assertTrue(comparador.compare(baja, alta) > 0);
    }

    /** Misma prioridad = empate (0), aunque cambien peso y tiempo: el criterio es puro. */
    @Test
    void prioridadDescDevuelveCeroSiEmpatan() {
        ComparadorPrioridadDesc comparador = new ComparadorPrioridadDesc();
        assertEquals(0, comparador.compare(paquete("PQ001", 1.0, 3, 10),
                paquete("PQ002", 9.0, 3, 99)));
    }

    /** Usado en sort, deja la lista de mayor a menor prioridad. */
    @Test
    void ordenarPorPrioridadDejaLaListaDeMayorAMenor() {
        List<Paquete> lista = new ArrayList<>(List.of(
                paquete("PQ001", 1.0, 2, 10),
                paquete("PQ002", 1.0, 5, 10),
                paquete("PQ003", 1.0, 1, 10),
                paquete("PQ004", 1.0, 4, 10)));

        lista.sort(new ComparadorPrioridadDesc());

        assertEquals(List.of("PQ002", "PQ004", "PQ001", "PQ003"), codigos(lista));
    }
}