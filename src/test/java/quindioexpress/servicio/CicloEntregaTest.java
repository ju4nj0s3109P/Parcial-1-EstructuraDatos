package quindioexpress.servicio;

import org.junit.jupiter.api.Test;
import quindioexpress.excepciones.AsignacionInvalidaException;
import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;

import static org.junit.jupiter.api.Assertions.*;

class CicloEntregaTest {

    @Test
    void entregaEnFifoRetiraPendientesYRegistraElHistorialEnOrdenReal() {
        CentroDistribucion centro = new CentroDistribucion();
        GestorDespacho despacho = new GestorDespacho();
        GestorRepartidores repartidores = new GestorRepartidores();
        HistorialEntregas historial = new HistorialEntregas();

        Paquete primero = new Paquete("PQ731", "Armenia", 3.0, 3, 20);
        Paquete segundo = new Paquete("PQ105", "Armenia", 4.0, 5, 10);
        centro.registrarPaquete(primero);
        centro.registrarPaquete(segundo);
        despacho.agregarPaquete(primero);
        despacho.agregarPaquete(segundo);
        Repartidor repartidor = new Repartidor("R001", "Ana Perez", "Armenia");
        repartidores.registrarRepartidor(repartidor);

        CicloEntrega ciclo = new CicloEntrega(
                centro, despacho, repartidores, historial, CicloEntrega.Modalidad.FIFO);

        ResultadoEntrega resultado = ciclo.procesarSiguienteEntrega();

        assertSame(primero, resultado.paquete());
        assertSame(repartidor, resultado.repartidor());
        assertNull(centro.buscarPorCodigo("PQ731"));
        assertSame(segundo, centro.buscarPorCodigo("PQ105"));
        assertEquals(1, despacho.cantidadPendientesFIFO());
        // La cola prioritaria es una vista alternativa y no se consume en este ciclo.
        assertEquals(2, despacho.cantidadPendientesPrioritarios());
        assertEquals(1, historial.cantidadEntregas());
        assertSame(primero, historial.obtenerEntrega(0));
        assertTrue(repartidor.isDisponible());
    }

    @Test
    void modalidadPrioridadEntregaPrimeroLaMayorPrioridad() {
        CentroDistribucion centro = new CentroDistribucion();
        GestorDespacho despacho = new GestorDespacho();
        GestorRepartidores repartidores = new GestorRepartidores();
        HistorialEntregas historial = new HistorialEntregas();

        Paquete baja = new Paquete("PQ001", "Armenia", 3.0, 1, 5);
        Paquete alta = new Paquete("PQ002", "Armenia", 4.0, 5, 60);
        centro.registrarPaquete(baja);
        centro.registrarPaquete(alta);
        despacho.agregarPaquete(baja);
        despacho.agregarPaquete(alta);
        repartidores.registrarRepartidor(new Repartidor("R001", "Ana Perez", "Armenia"));

        CicloEntrega ciclo = new CicloEntrega(
                centro, despacho, repartidores, historial, CicloEntrega.Modalidad.PRIORIDAD);

        assertSame(alta, ciclo.procesarSiguienteEntrega().paquete());
        assertSame(alta, historial.obtenerEntrega(0));
        assertNotNull(centro.buscarPorCodigo("PQ001"));
        assertEquals(1, despacho.cantidadPendientesPrioritarios());
        assertEquals(2, despacho.cantidadPendientesFIFO());
    }

    @Test
    void sinRepartidorCompatibleElPaquetePermanecePendiente() {
        CentroDistribucion centro = new CentroDistribucion();
        GestorDespacho despacho = new GestorDespacho();
        GestorRepartidores repartidores = new GestorRepartidores();
        HistorialEntregas historial = new HistorialEntregas();
        Paquete paquete = new Paquete("PQ731", "Armenia", 3.0, 3, 20);

        centro.registrarPaquete(paquete);
        despacho.agregarPaquete(paquete);
        repartidores.registrarRepartidor(new Repartidor("R001", "Ana Perez", "Salento"));

        CicloEntrega ciclo = new CicloEntrega(
                centro, despacho, repartidores, historial, CicloEntrega.Modalidad.FIFO);

        assertThrows(AsignacionInvalidaException.class, ciclo::procesarSiguienteEntrega);
        assertSame(paquete, despacho.verSiguienteFIFO());
        assertSame(paquete, centro.buscarPorCodigo("PQ731"));
        assertTrue(historial.estaVacio());
    }

    @Test
    void colaVaciaNoCreaEntrega() {
        CicloEntrega ciclo = new CicloEntrega(
                new CentroDistribucion(),
                new GestorDespacho(),
                new GestorRepartidores(),
                new HistorialEntregas(),
                CicloEntrega.Modalidad.FIFO);

        assertNull(ciclo.procesarSiguienteEntrega());
    }
}
