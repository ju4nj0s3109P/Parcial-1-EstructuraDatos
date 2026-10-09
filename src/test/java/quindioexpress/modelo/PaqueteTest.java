package quindioexpress.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaqueteTest {

    @Test
    void guardaLosDatosDelPaquete() {
        Paquete paquete = new Paquete(" PQ-01 ", " Armenia ", 2.5, 3, 40);

        assertEquals("PQ-01", paquete.getCodigo());
        assertEquals("Armenia", paquete.getDestino());
        assertEquals(2.5, paquete.getPeso());
        assertEquals(3, paquete.getPrioridad());
        assertEquals(40, paquete.getTiempoEstimado());
    }

    @Test
    void usaPrioridadCincoCuandoNoSeIndica() {
        Paquete paquete = new Paquete("PQ-02", "Pereira", 1.2, 20);

        assertEquals(5, paquete.getPrioridad());
    }

    @Test
    void noAceptaCodigoVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paquete("  ", "Armenia", 1, 2, 10));
    }

    @Test
    void noAceptaCodigoNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paquete(null, "Armenia", 1, 2, 10));
    }

    @Test
    void noAceptaDestinoVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paquete("PQ-01", " ", 1, 2, 10));
    }

    @Test
    void noAceptaPesoCeroONegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paquete("PQ-01", "Armenia", 0, 2, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new Paquete("PQ-01", "Armenia", -1, 2, 10));
    }

    @Test
    void noAceptaPesoInfinitoONoNumerico() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paquete("PQ-01", "Armenia", Double.POSITIVE_INFINITY, 2, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new Paquete("PQ-01", "Armenia", Double.NaN, 2, 10));
    }

    @Test
    void noAceptaPrioridadFueraDelRango() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paquete("PQ-01", "Armenia", 1, 0, 10));
        assertThrows(IllegalArgumentException.class,
                () -> new Paquete("PQ-01", "Armenia", 1, 6, 10));
    }

    @Test
    void noAceptaTiempoCeroONegativo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Paquete("PQ-01", "Armenia", 1, 2, 0));
        assertThrows(IllegalArgumentException.class,
                () -> new Paquete("PQ-01", "Armenia", 1, 2, -5));
    }

    @Test
    void losCambiosTambienSeValidan() {
        Paquete paquete = new Paquete("PQ-01", "Armenia", 1, 2, 10);

        paquete.setDestino("Calarca");
        paquete.setPeso(3.5);
        paquete.setPrioridad(4);
        paquete.setTiempoEstimado(25);

        assertEquals("Calarca", paquete.getDestino());
        assertEquals(3.5, paquete.getPeso());
        assertEquals(4, paquete.getPrioridad());
        assertEquals(25, paquete.getTiempoEstimado());

        assertThrows(IllegalArgumentException.class, () -> paquete.setDestino(""));
        assertThrows(IllegalArgumentException.class, () -> paquete.setPeso(0));
        assertThrows(IllegalArgumentException.class, () -> paquete.setPrioridad(7));
        assertThrows(IllegalArgumentException.class, () -> paquete.setTiempoEstimado(0));
    }

    @Test
    void ordenaPorCodigoDeMenorAMayor() {
        Paquete primero = new Paquete("PQ-01", "Armenia", 1, 2, 10);
        Paquete segundo = new Paquete("PQ-02", "Pereira", 1, 2, 10);

        assertTrue(primero.compareTo(segundo) < 0);
        assertTrue(segundo.compareTo(primero) > 0);
        assertEquals(0, primero.compareTo(
                new Paquete("PQ-01", "Calarca", 4, 1, 20)));
    }

    @Test
    void muestraLosDatosEnElTexto() {
        Paquete paquete = new Paquete("PQ-01", "Armenia", 2, 3, 15);

        assertTrue(paquete.toString().contains("PQ-01"));
        assertTrue(paquete.toString().contains("Armenia"));
    }
}
