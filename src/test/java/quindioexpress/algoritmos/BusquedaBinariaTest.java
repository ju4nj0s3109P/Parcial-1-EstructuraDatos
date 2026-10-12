package quindioexpress.algoritmos;

import org.junit.jupiter.api.Test;
import quindioexpress.datos.DatosPrueba;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de la búsqueda binaria
 */
class BusquedaBinariaTest {

    /** Ejemplo del enunciado: PQ731 está en la posición 5 de la lista de 8. */
    @Test
    void encuentraElEjemploDelEnunciado() {
        List<Paquete> ordenados = DatosPrueba.escenarioBusquedaBinaria();
        assertEquals(5, BusquedaBinaria.buscarPosicion(ordenados, "PQ731"));
        assertEquals("PQ731", BusquedaBinaria.buscar(ordenados, "PQ731").getCodigo());
    }

    /** Los extremos son los casos donde más fácil se equivoca el cálculo de rangos. */
    @Test
    void encuentraElPrimeroYElUltimo() {
        List<Paquete> ordenados = DatosPrueba.escenarioBusquedaBinaria();
        assertEquals(0, BusquedaBinaria.buscarPosicion(ordenados, "PQ105"));
        assertEquals(7, BusquedaBinaria.buscarPosicion(ordenados, "PQ942"));
    }

    /** Cada uno de los 8 paquetes se encuentra en su posición exacta. */
    @Test
    void encuentraCadaElementoEnSuPosicion() {
        List<Paquete> ordenados = DatosPrueba.escenarioBusquedaBinaria();
        for (int i = 0; i < ordenados.size(); i++) {
            assertEquals(i, BusquedaBinaria.buscarPosicion(ordenados, ordenados.get(i).getCodigo()));
        }
    }

    /** Códigos ausentes: antes del primero, después del último y entre dos existentes. */
    @Test
    void devuelveMenosUnoSiNoExiste() {
        List<Paquete> ordenados = DatosPrueba.escenarioBusquedaBinaria();
        assertEquals(-1, BusquedaBinaria.buscarPosicion(ordenados, "PQ001")); // antes del primero
        assertEquals(-1, BusquedaBinaria.buscarPosicion(ordenados, "PQ999")); // después del último
        assertEquals(-1, BusquedaBinaria.buscarPosicion(ordenados, "PQ500")); // en medio
        assertNull(BusquedaBinaria.buscar(ordenados, "PQ500"));
    }

    /** Lista vacía: rango [0, -1], la primera llamada ya cumple inicio > fin. */
    @Test
    void listaVaciaDevuelveMenosUno() {
        assertEquals(-1, BusquedaBinaria.buscarPosicion(new ArrayList<>(), "PQ731"));
        assertNull(BusquedaBinaria.buscar(new ArrayList<>(), "PQ731"));
    }

    /** Un solo elemento: lo encuentra, y si no está, sale por izquierda o por derecha. */
    @Test
    void listaDeUnSoloElemento() {
        List<Paquete> uno = List.of(new Paquete("PQ731", "Armenia", 1.0, 3, 10));
        assertEquals(0, BusquedaBinaria.buscarPosicion(uno, "PQ731"));
        assertEquals(-1, BusquedaBinaria.buscarPosicion(uno, "PQ105"));
        assertEquals(-1, BusquedaBinaria.buscarPosicion(uno, "PQ942"));
    }

    /** El código se limpia de espacios antes de buscar. */
    @Test
    void ignoraEspaciosAlrededorDelCodigo() {
        assertEquals(5, BusquedaBinaria.buscarPosicion(DatosPrueba.escenarioBusquedaBinaria(), "  PQ731 "));
    }

    /** Lista nula, código nulo o vacío lanzan DatoInvalidoException. */
    @Test
    void rechazaDatosInvalidos() {
        List<Paquete> ordenados = DatosPrueba.escenarioBusquedaBinaria();
        assertThrows(DatoInvalidoException.class, () -> BusquedaBinaria.buscarPosicion(null, "PQ731"));
        assertThrows(DatoInvalidoException.class, () -> BusquedaBinaria.buscarPosicion(ordenados, null));
        assertThrows(DatoInvalidoException.class, () -> BusquedaBinaria.buscarPosicion(ordenados, " "));
        assertThrows(DatoInvalidoException.class, () -> BusquedaBinaria.ordenarPorCodigo(null));
    }

    /** ordenarPorCodigo devuelve una copia ordenada y no altera el orden de llegada. */
    @Test
    void ordenarPorCodigoDevuelveCopiaOrdenadaSinTocarLaOriginal() {
        List<Paquete> llegada = DatosPrueba.crearPaquetes();
        String primeroLlegada = llegada.get(0).getCodigo();

        List<Paquete> ordenados = BusquedaBinaria.ordenarPorCodigo(llegada);

        assertNotSame(llegada, ordenados);
        assertEquals(llegada.size(), ordenados.size());
        assertEquals(primeroLlegada, llegada.get(0).getCodigo()); // la original no cambió
        for (int i = 1; i < ordenados.size(); i++) {
            assertTrue(ordenados.get(i - 1).compareTo(ordenados.get(i)) < 0);
        }
    }

    /** Tras ordenar, encuentra los 32 paquetes de la jornada (y son los mismos objetos). */
    @Test
    void encuentraTodosLosPaquetesDeLaJornadaTrasOrdenar() {
        List<Paquete> llegada = DatosPrueba.crearPaquetes();
        List<Paquete> ordenados = BusquedaBinaria.ordenarPorCodigo(llegada);

        for (Paquete p : llegada) {
            Paquete encontrado = BusquedaBinaria.buscar(ordenados, p.getCodigo());
            assertSame(p, encontrado);
        }
    }

    /**
     * Documenta la precondición: sobre la lista en ORDEN DE LLEGADA la
     * búsqueda binaria no encuentra PQ829 aunque sí está en la lista.
     */
    @Test
    void sinOrdenarLaPrecondicionSeIncumpleYPuedeFallar() {
        List<Paquete> desordenados = DatosPrueba.crearPaquetes();
        assertEquals(-1, BusquedaBinaria.buscarPosicion(desordenados, "PQ829"));

        List<Paquete> ordenados = BusquedaBinaria.ordenarPorCodigo(desordenados);
        assertNotEquals(-1, BusquedaBinaria.buscarPosicion(ordenados, "PQ829"));
    }
}