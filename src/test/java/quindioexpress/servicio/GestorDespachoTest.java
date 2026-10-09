package quindioexpress.servicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestorDespachoTest {

    private GestorDespacho gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestorDespacho();
    }

    private Paquete paquete(String codigo, int prioridad, int tiempo) {
        return new Paquete(codigo, "Armenia", 2.5, prioridad, tiempo);
    }

    @Test
    void debeIniciarConAmbasColasVacias() {
        assertTrue(gestor.estaVaciaFIFO());
        assertTrue(gestor.estaVaciaPrioritaria());
        assertEquals(0, gestor.cantidadPendientesFIFO());
        assertEquals(0, gestor.cantidadPendientesPrioritarios());
    }

    @Test
    void debeRechazarPaqueteNulo() {
        assertThrows(DatoInvalidoException.class, () -> gestor.agregarPaquete(null));
        assertTrue(gestor.estaVaciaFIFO());
        assertTrue(gestor.estaVaciaPrioritaria());
    }

    @Test
    void debeDespacharFIFOEnOrdenDeLlegada() {
        Paquete primero = paquete("PQ731", 2, 30);
        Paquete segundo = paquete("PQ105", 5, 10);
        Paquete tercero = paquete("PQ942", 3, 20);

        gestor.agregarPaquete(primero);
        gestor.agregarPaquete(segundo);
        gestor.agregarPaquete(tercero);

        assertSame(primero, gestor.despacharFIFO());
        assertSame(segundo, gestor.despacharFIFO());
        assertSame(tercero, gestor.despacharFIFO());
        assertNull(gestor.despacharFIFO());
    }

    @Test
    void debeDespacharPorPrioridadTiempoYCodigo() {
        gestor.agregarPaquete(paquete("PQ420", 4, 5));
        gestor.agregarPaquete(paquete("PQ942", 5, 25));
        gestor.agregarPaquete(paquete("PQ731", 5, 25));
        gestor.agregarPaquete(paquete("PQ105", 5, 10));

        assertEquals("PQ105", gestor.despacharPrioritario().getCodigo());
        assertEquals("PQ731", gestor.despacharPrioritario().getCodigo());
        assertEquals("PQ942", gestor.despacharPrioritario().getCodigo());
        assertEquals("PQ420", gestor.despacharPrioritario().getCodigo());
        assertNull(gestor.despacharPrioritario());
    }

    @Test
    void debeDarPreferenciaAMayorPrioridadAunqueTengaMayorTiempo() {
        gestor.agregarPaquete(paquete("PQ001", 2, 1));
        gestor.agregarPaquete(paquete("PQ002", 5, 100));

        assertEquals("PQ002", gestor.despacharPrioritario().getCodigo());
        assertEquals("PQ001", gestor.despacharPrioritario().getCodigo());
    }

    @Test
    void debeUsarTiempoAscendenteCuandoLaPrioridadEsIgual() {
        gestor.agregarPaquete(paquete("PQ001", 4, 40));
        gestor.agregarPaquete(paquete("PQ002", 4, 10));

        assertEquals("PQ002", gestor.despacharPrioritario().getCodigo());
        assertEquals("PQ001", gestor.despacharPrioritario().getCodigo());
    }

    @Test
    void debeUsarCodigoAscendenteCuandoPrioridadYTiempoSonIguales() {
        gestor.agregarPaquete(paquete("PQ942", 3, 15));
        gestor.agregarPaquete(paquete("PQ105", 3, 15));
        gestor.agregarPaquete(paquete("PQ731", 3, 15));

        assertEquals(List.of("PQ105", "PQ731", "PQ942"), List.of(
                gestor.despacharPrioritario().getCodigo(),
                gestor.despacharPrioritario().getCodigo(),
                gestor.despacharPrioritario().getCodigo()));
    }

    @Test
    void verSiguientePrioritarioNoDebeRetirarElPaquete() {
        Paquete paquete = paquete("PQ105", 5, 10);
        gestor.agregarPaquete(paquete);

        assertSame(paquete, gestor.verSiguientePrioritario());
        assertSame(paquete, gestor.verSiguientePrioritario());
        assertEquals(1, gestor.cantidadPendientesPrioritarios());
    }

    @Test
    void debeActualizarCantidadYEstadoDeLaColaFIFO() {
        gestor.agregarPaquete(paquete("PQ001", 1, 30));
        gestor.agregarPaquete(paquete("PQ002", 2, 20));

        assertEquals(2, gestor.cantidadPendientesFIFO());
        gestor.despacharFIFO();
        assertEquals(1, gestor.cantidadPendientesFIFO());
        gestor.despacharFIFO();

        assertEquals(0, gestor.cantidadPendientesFIFO());
        assertTrue(gestor.estaVaciaFIFO());
    }

    @Test
    void debeActualizarCantidadYEstadoDeLaColaPrioritaria() {
        gestor.agregarPaquete(paquete("PQ001", 1, 30));
        gestor.agregarPaquete(paquete("PQ002", 2, 20));

        assertEquals(2, gestor.cantidadPendientesPrioritarios());
        gestor.despacharPrioritario();
        assertEquals(1, gestor.cantidadPendientesPrioritarios());
        gestor.despacharPrioritario();

        assertEquals(0, gestor.cantidadPendientesPrioritarios());
        assertTrue(gestor.estaVaciaPrioritaria());
    }

    @Test
    void despacharEnUnaModalidadNoDebeRetirarDeLaOtra() {
        Paquete paquete = paquete("PQ001", 3, 15);
        gestor.agregarPaquete(paquete);

        assertSame(paquete, gestor.despacharFIFO());
        assertEquals(0, gestor.cantidadPendientesFIFO());
        assertEquals(1, gestor.cantidadPendientesPrioritarios());
        assertSame(paquete, gestor.despacharPrioritario());
    }

    @Test
    void verSiguientePrioritarioEnColaVaciaDebeDevolverNull() {
        assertNull(gestor.verSiguientePrioritario());
    }

    @Test
    void despacharEnColasVaciasDebeDevolverNull() {
        assertNull(gestor.despacharFIFO());
        assertNull(gestor.despacharPrioritario());
    }
}
