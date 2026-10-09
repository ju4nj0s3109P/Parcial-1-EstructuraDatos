package quindioexpress.servicio;

import org.junit.jupiter.api.Test;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.excepciones.PaqueteDuplicadoException;
import quindioexpress.modelo.Paquete;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CentroDistribucionTest {

    private Paquete crearPaquete(String codigo, String destino) {
        return new Paquete(codigo, destino, 2.5, 3, 20);
    }

    @Test
    void iniciaSinPaquetesRegistrados() {
        CentroDistribucion centro = new CentroDistribucion();

        assertTrue(centro.estaVacio());
        assertEquals(0, centro.cantidadPaquetes());
        assertTrue(centro.obtenerPaquetesRegistrados().isEmpty());
    }

    @Test
    void registraPaqueteYActualizaLaCantidad() {
        CentroDistribucion centro = new CentroDistribucion();
        Paquete paquete = crearPaquete("PQ-01", "Armenia");

        centro.registrarPaquete(paquete);

        assertFalse(centro.estaVacio());
        assertEquals(1, centro.cantidadPaquetes());
        assertSame(paquete, centro.buscarPorCodigo("PQ-01"));
    }

    @Test
    void conservaElOrdenEnQueSeRegistranLosPaquetes() {
        CentroDistribucion centro = new CentroDistribucion();
        Paquete primero = crearPaquete("PQ-02", "Armenia");
        Paquete segundo = crearPaquete("PQ-01", "Calarca");

        centro.registrarPaquete(primero);
        centro.registrarPaquete(segundo);

        assertEquals(List.of(primero, segundo), centro.obtenerPaquetesRegistrados());
    }

    @Test
    void noPermiteRegistrarDosPaquetesConElMismoCodigo() {
        CentroDistribucion centro = new CentroDistribucion();
        centro.registrarPaquete(crearPaquete("PQ-01", "Armenia"));

        assertThrows(PaqueteDuplicadoException.class,
                () -> centro.registrarPaquete(crearPaquete("PQ-01", "Calarca")));
        assertEquals(1, centro.cantidadPaquetes());
    }

    @Test
    void noPermiteRegistrarUnPaqueteNulo() {
        CentroDistribucion centro = new CentroDistribucion();

        assertThrows(DatoInvalidoException.class, () -> centro.registrarPaquete(null));
        assertTrue(centro.estaVacio());
    }

    @Test
    void buscaPorCodigoIgnorandoEspaciosAlrededor() {
        CentroDistribucion centro = new CentroDistribucion();
        Paquete paquete = crearPaquete("PQ-01", "Armenia");
        centro.registrarPaquete(paquete);

        assertSame(paquete, centro.buscarPorCodigo(" PQ-01 "));
    }

    @Test
    void devuelveNullCuandoElCodigoNoEstaRegistrado() {
        CentroDistribucion centro = new CentroDistribucion();

        assertNull(centro.buscarPorCodigo("PQ-99"));
    }

    @Test
    void rechazaCodigosNulosOVaciosEnLasConsultas() {
        CentroDistribucion centro = new CentroDistribucion();

        assertThrows(DatoInvalidoException.class, () -> centro.buscarPorCodigo(null));
        assertThrows(DatoInvalidoException.class, () -> centro.buscarPorCodigo("  "));
        assertThrows(DatoInvalidoException.class, () -> centro.eliminarPaquete(null));
        assertThrows(DatoInvalidoException.class, () -> centro.eliminarPaquete(""));
    }

    @Test
    void eliminaElPaqueteDelMapaYDelOrdenDeRegistro() {
        CentroDistribucion centro = new CentroDistribucion();
        Paquete primero = crearPaquete("PQ-01", "Armenia");
        Paquete segundo = crearPaquete("PQ-02", "Calarca");
        centro.registrarPaquete(primero);
        centro.registrarPaquete(segundo);

        assertTrue(centro.eliminarPaquete("PQ-01"));
        assertNull(centro.buscarPorCodigo("PQ-01"));
        assertEquals(List.of(segundo), centro.obtenerPaquetesRegistrados());
        assertEquals(1, centro.cantidadPaquetes());
    }

    @Test
    void eliminarUnCodigoInexistenteDevuelveFalse() {
        CentroDistribucion centro = new CentroDistribucion();

        assertFalse(centro.eliminarPaquete("PQ-99"));
        assertTrue(centro.estaVacio());
    }

    @Test
    void permiteRegistrarNuevamenteUnCodigoDespuesDeEliminarlo() {
        CentroDistribucion centro = new CentroDistribucion();
        centro.registrarPaquete(crearPaquete("PQ-01", "Armenia"));

        assertTrue(centro.eliminarPaquete("PQ-01"));

        Paquete nuevo = crearPaquete("PQ-01", "Calarca");
        centro.registrarPaquete(nuevo);

        assertSame(nuevo, centro.buscarPorCodigo("PQ-01"));
        assertEquals(1, centro.cantidadPaquetes());
    }

    @Test
    void noPermiteModificarLaListaDevuelta() {
        CentroDistribucion centro = new CentroDistribucion();
        centro.registrarPaquete(crearPaquete("PQ-01", "Armenia"));

        List<Paquete> paquetes = centro.obtenerPaquetesRegistrados();

        assertThrows(UnsupportedOperationException.class,
                () -> paquetes.add(crearPaquete("PQ-02", "Calarca")));
        assertEquals(1, centro.cantidadPaquetes());
    }
}
