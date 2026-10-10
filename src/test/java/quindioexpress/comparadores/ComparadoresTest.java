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

    /** El de 18.7 kg debe ir antes que el de 4.5 kg, y al revés da positivo. */
    @Test
    void pesoDescPoneAntesElMasPesado() {
        ComparadorPesoDesc comparador = new ComparadorPesoDesc();
        Paquete pesado = paquete("PQ001", 18.7, 3, 10);
        Paquete liviano = paquete("PQ002", 4.5, 3, 10);

        assertTrue(comparador.compare(pesado, liviano) < 0);
        assertTrue(comparador.compare(liviano, pesado) > 0);
    }

    /**
     * Con una resta convertida a int (4.5 - 4.2 = 0.3 → 0) estos dos paquetes
     * parecerían iguales. Double.compare sí los distingue.
     */
    @Test
    void pesoDescDistingueDiferenciasMenoresAUnKilo() {
        ComparadorPesoDesc comparador = new ComparadorPesoDesc();
        Paquete a = paquete("PQ001", 4.5, 3, 10);
        Paquete b = paquete("PQ002", 4.2, 3, 10);

        assertTrue(comparador.compare(a, b) < 0);
    }

    /** Mismo peso = empate (0), aunque cambien prioridad y tiempo: el criterio es puro. */
    @Test
    void pesoDescDevuelveCeroSiPesanIgual() {
        assertEquals(0, new ComparadorPesoDesc().compare(
                paquete("PQ001", 7.4, 1, 10), paquete("PQ002", 7.4, 5, 50)));
    }

    /** Usado en sort, deja la lista de mayor a menor peso. */
    @Test
    void ordenarPorPesoDejaLaListaDeMayorAMenor() {
        List<Paquete> lista = new ArrayList<>(List.of(
                paquete("PQ001", 4.5, 3, 10),
                paquete("PQ002", 12.0, 3, 10),
                paquete("PQ003", 3.2, 3, 10),
                paquete("PQ004", 18.7, 3, 10)));

        lista.sort(new ComparadorPesoDesc());

        assertEquals(List.of("PQ004", "PQ002", "PQ001", "PQ003"), codigos(lista));
    }

    /** El de 20 min debe ir antes que el de 45 min, y al revés da positivo. */
    @Test
    void tiempoAscPoneAntesElDeMenorTiempo() {
        ComparadorTiempoAsc comparador = new ComparadorTiempoAsc();
        Paquete rapido = paquete("PQ001", 1.0, 3, 20);
        Paquete lento = paquete("PQ002", 1.0, 3, 45);

        assertTrue(comparador.compare(rapido, lento) < 0);
        assertTrue(comparador.compare(lento, rapido) > 0);
    }

    /** Mismo tiempo = empate (0), aunque cambien peso y prioridad: el criterio es puro. */
    @Test
    void tiempoAscDevuelveCeroSiTardanIgual() {
        assertEquals(0, new ComparadorTiempoAsc().compare(
                paquete("PQ001", 1.0, 3, 20), paquete("PQ002", 9.0, 1, 20)));
    }

    /** Usado en sort, deja la lista de menor a mayor tiempo. */
    @Test
    void ordenarPorTiempoDejaLaListaDeMenorAMayor() {
        List<Paquete> lista = new ArrayList<>(List.of(
                paquete("PQ001", 1.0, 3, 45),
                paquete("PQ002", 1.0, 3, 20),
                paquete("PQ003", 1.0, 3, 30)));

        lista.sort(new ComparadorTiempoAsc());

        assertEquals(List.of("PQ002", "PQ003", "PQ001"), codigos(lista));
    }
}