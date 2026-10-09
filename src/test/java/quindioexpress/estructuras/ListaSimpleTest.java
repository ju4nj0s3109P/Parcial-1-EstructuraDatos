package quindioexpress.estructuras;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ListaSimpleTest {

    @Test
    void listaNuevaDebeEstarVacia() {
        ListaSimple<String> lista = new ListaSimple<>();

        assertTrue(lista.estaVacia());
        assertEquals(0, lista.tamano());
    }

    @Test
    void agregarDebeConservarElOrdenYActualizarElTamano() {
        ListaSimple<String> lista = new ListaSimple<>();

        lista.agregar("uno");
        lista.agregar("dos");
        lista.agregar("tres");

        assertFalse(lista.estaVacia());
        assertEquals(3, lista.tamano());
        assertEquals("uno", lista.obtener(0));
        assertEquals("dos", lista.obtener(1));
        assertEquals("tres", lista.obtener(2));
    }

    @Test
    void obtenerDebePermitirElPrimerYElUltimoIndice() {
        ListaSimple<Integer> lista = new ListaSimple<>();
        lista.agregar(10);
        lista.agregar(20);
        lista.agregar(30);

        assertEquals(10, lista.obtener(0));
        assertEquals(30, lista.obtener(2));
    }

    @Test
    void obtenerDebeRechazarIndicesFueraDeRango() {
        ListaSimple<String> lista = new ListaSimple<>();
        lista.agregar("dato");

        assertThrows(IndexOutOfBoundsException.class, () -> lista.obtener(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> lista.obtener(1));
        assertThrows(IndexOutOfBoundsException.class, () -> new ListaSimple<>().obtener(0));
    }

    @Test
    void eliminarDebeQuitarElPrimerElemento() {
        ListaSimple<String> lista = new ListaSimple<>();
        lista.agregar("a");
        lista.agregar("b");

        assertTrue(lista.eliminar("a"));
        assertEquals("b", lista.obtener(0));
        assertEquals(1, lista.tamano());
    }

    @Test
    void eliminarDebeQuitarUnElementoDelMedio() {
        ListaSimple<String> lista = new ListaSimple<>();
        lista.agregar("a");
        lista.agregar("b");
        lista.agregar("c");

        assertTrue(lista.eliminar("b"));
        assertEquals(2, lista.tamano());
        assertEquals("a", lista.obtener(0));
        assertEquals("c", lista.obtener(1));
    }

    @Test
    void eliminarDebeQuitarElUltimoElemento() {
        ListaSimple<String> lista = new ListaSimple<>();
        lista.agregar("a");
        lista.agregar("b");

        assertTrue(lista.eliminar("b"));
        assertEquals(1, lista.tamano());
        assertEquals("a", lista.obtener(0));
    }

    @Test
    void eliminarElUnicoElementoDebeDejarLaListaVacia() {
        ListaSimple<String> lista = new ListaSimple<>();
        lista.agregar("único");

        assertTrue(lista.eliminar("único"));
        assertTrue(lista.estaVacia());
        assertEquals(0, lista.tamano());
    }

    @Test
    void eliminarDebeDevolverFalseSiElElementoNoExiste() {
        ListaSimple<String> lista = new ListaSimple<>();
        lista.agregar("a");

        assertFalse(lista.eliminar("no existe"));
        assertEquals(1, lista.tamano());
        assertEquals("a", lista.obtener(0));
        assertFalse(new ListaSimple<String>().eliminar("no existe"));
    }

    @Test
    void eliminarDebeQuitarSoloLaPrimeraAparicion() {
        ListaSimple<String> lista = new ListaSimple<>();
        lista.agregar("repetido");
        lista.agregar("otro");
        lista.agregar("repetido");

        assertTrue(lista.eliminar("repetido"));
        assertEquals(2, lista.tamano());
        assertEquals("otro", lista.obtener(0));
        assertEquals("repetido", lista.obtener(1));
    }

    @Test
    void listaDebePermitirValoresNulos() {
        ListaSimple<String> lista = new ListaSimple<>();
        lista.agregar(null);
        lista.agregar("dato");

        assertNull(lista.obtener(0));
        assertTrue(lista.eliminar(null));
        assertEquals(1, lista.tamano());
        assertEquals("dato", lista.obtener(0));
    }

    @Test
    void iteradorDebeRecorrerLosElementosEnOrden() {
        ListaSimple<String> lista = new ListaSimple<>();
        lista.agregar("a");
        lista.agregar("b");
        lista.agregar("c");

        Iterator<String> iterador = lista.iterator();

        assertTrue(iterador.hasNext());
        assertEquals("a", iterador.next());
        assertEquals("b", iterador.next());
        assertEquals("c", iterador.next());
        assertFalse(iterador.hasNext());
        assertThrows(NoSuchElementException.class, iterador::next);
    }

    @Test
    void iteradorDeListaVaciaNoDebeTenerSiguiente() {
        Iterator<String> iterador = new ListaSimple<String>().iterator();

        assertFalse(iterador.hasNext());
        assertThrows(NoSuchElementException.class, iterador::next);
    }

    @Test
    void listaDebeImplementarElContratoDeColeccion() {
        Coleccion<Integer> coleccion = new ListaSimple<>();
        coleccion.agregar(7);

        assertEquals(1, coleccion.tamano());
        assertEquals(7, coleccion.obtener(0));
        assertFalse(coleccion.estaVacia());
    }
}
