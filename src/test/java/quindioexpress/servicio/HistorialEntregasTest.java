package quindioexpress.servicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistorialEntregasTest {

    private HistorialEntregas historial;

    @BeforeEach
    void setUp() {
        historial = new HistorialEntregas();
    }

    private Paquete paquete(String codigo) {
        return new Paquete(codigo, "Armenia", 2.5, 3, 20);
    }

    @Test
    void debeIniciarVacio() {
        assertTrue(historial.estaVacio());
        assertEquals(0, historial.cantidadEntregas());
        assertFalse(historial.iterator().hasNext());
    }

    @Test
    void debeRechazarPaqueteNuloSinModificarHistorial() {
        assertThrows(DatoInvalidoException.class, () -> historial.registrarEntrega(null));
        assertTrue(historial.estaVacio());
        assertEquals(0, historial.cantidadEntregas());
    }

    @Test
    void debeConservarExactamenteElOrdenRealDeEntrega() {
        Paquete primero = paquete("PQ105");
        Paquete segundo = paquete("PQ942");
        Paquete tercero = paquete("PQ731");

        historial.registrarEntrega(primero);
        historial.registrarEntrega(segundo);
        historial.registrarEntrega(tercero);

        assertSame(primero, historial.obtenerEntrega(0));
        assertSame(segundo, historial.obtenerEntrega(1));
        assertSame(tercero, historial.obtenerEntrega(2));
    }

    @Test
    void debePermitirRegistrarUnSoloPaquete() {
        Paquete paquete = paquete("PQ105");

        historial.registrarEntrega(paquete);

        assertFalse(historial.estaVacio());
        assertEquals(1, historial.cantidadEntregas());
        assertSame(paquete, historial.obtenerEntrega(0));
    }

    @Test
    void debeRecorrerElHistorialMedianteIterableSinExponerNodos() {
        historial.registrarEntrega(paquete("PQ105"));
        historial.registrarEntrega(paquete("PQ942"));
        historial.registrarEntrega(paquete("PQ731"));

        List<String> codigos = new ArrayList<>();
        for (Paquete paquete : historial) {
            codigos.add(paquete.getCodigo());
        }

        assertEquals(List.of("PQ105", "PQ942", "PQ731"), codigos);
    }

    @Test
    void debeEliminarLaPrimeraEntregaYConservarElOrdenRestante() {
        Paquete primero = paquete("PQ105");
        Paquete segundo = paquete("PQ942");
        Paquete tercero = paquete("PQ731");
        historial.registrarEntrega(primero);
        historial.registrarEntrega(segundo);
        historial.registrarEntrega(tercero);

        assertTrue(historial.eliminarEntrega(primero));

        assertEquals(2, historial.cantidadEntregas());
        assertSame(segundo, historial.obtenerEntrega(0));
        assertSame(tercero, historial.obtenerEntrega(1));
    }

    @Test
    void debeEliminarUnaEntregaIntermedia() {
        Paquete primero = paquete("PQ105");
        Paquete segundo = paquete("PQ942");
        Paquete tercero = paquete("PQ731");
        historial.registrarEntrega(primero);
        historial.registrarEntrega(segundo);
        historial.registrarEntrega(tercero);

        assertTrue(historial.eliminarEntrega(segundo));

        assertEquals(List.of("PQ105", "PQ731"), codigosDelHistorial());
    }

    @Test
    void debeEliminarLaUltimaEntrega() {
        Paquete primero = paquete("PQ105");
        Paquete segundo = paquete("PQ942");
        historial.registrarEntrega(primero);
        historial.registrarEntrega(segundo);

        assertTrue(historial.eliminarEntrega(segundo));

        assertEquals(1, historial.cantidadEntregas());
        assertSame(primero, historial.obtenerEntrega(0));
    }

    @Test
    void eliminarUnPaqueteInexistenteNoDebeModificarElHistorial() {
        historial.registrarEntrega(paquete("PQ105"));

        assertFalse(historial.eliminarEntrega(paquete("PQ999")));

        assertEquals(1, historial.cantidadEntregas());
        assertEquals(List.of("PQ105"), codigosDelHistorial());
    }

    @Test
    void eliminarElUnicoPaqueteDebeDejarElHistorialVacio() {
        Paquete paquete = paquete("PQ105");
        historial.registrarEntrega(paquete);

        assertTrue(historial.eliminarEntrega(paquete));

        assertTrue(historial.estaVacio());
        assertEquals(0, historial.cantidadEntregas());
        assertFalse(historial.iterator().hasNext());
    }

    @Test
    void debeRechazarIndicesFueraDeRango() {
        historial.registrarEntrega(paquete("PQ105"));

        assertThrows(IndexOutOfBoundsException.class, () -> historial.obtenerEntrega(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> historial.obtenerEntrega(1));
    }

    private List<String> codigosDelHistorial() {
        List<String> codigos = new ArrayList<>();
        for (Paquete paquete : historial) {
            codigos.add(paquete.getCodigo());
        }
        return codigos;
    }
}
