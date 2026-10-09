package quindioexpress.servicio;

import org.junit.jupiter.api.Test;
import quindioexpress.excepciones.AsignacionInvalidaException;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.excepciones.RepartidorDuplicadoException;
import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestorRepartidoresTest {

    private Repartidor crearRepartidor(
            String identificacion, String nombre, String zona) {
        return new Repartidor(identificacion, nombre, zona);
    }

    private Paquete crearPaquete(String codigo, String destino) {
        return new Paquete(codigo, destino, 2.5, 3, 20);
    }

    @Test
    void iniciaSinRepartidoresRegistrados() {
        GestorRepartidores gestor = new GestorRepartidores();

        assertTrue(gestor.estaVacio());
        assertEquals(0, gestor.cantidadRepartidores());
        assertTrue(gestor.obtenerRepartidoresRegistrados().isEmpty());
    }

    @Test
    void registraRepartidorYActualizaLaCantidad() {
        GestorRepartidores gestor = new GestorRepartidores();
        Repartidor repartidor = crearRepartidor("ID-01", "Ana Perez", "Armenia");

        gestor.registrarRepartidor(repartidor);

        assertFalse(gestor.estaVacio());
        assertEquals(1, gestor.cantidadRepartidores());
        assertSame(repartidor, gestor.buscarPorIdentificacion("ID-01"));
    }

    @Test
    void noPermiteRegistrarUnRepartidorNulo() {
        GestorRepartidores gestor = new GestorRepartidores();

        assertThrows(DatoInvalidoException.class,
                () -> gestor.registrarRepartidor(null));
        assertTrue(gestor.estaVacio());
    }

    @Test
    void noPermiteIdentificacionesDuplicadas() {
        GestorRepartidores gestor = new GestorRepartidores();
        gestor.registrarRepartidor(
                crearRepartidor("ID-01", "Ana Perez", "Armenia"));

        assertThrows(RepartidorDuplicadoException.class,
                () -> gestor.registrarRepartidor(
                        crearRepartidor(" ID-01 ", "Luis Gomez", "Salento")));
        assertEquals(1, gestor.cantidadRepartidores());
    }

    @Test
    void buscaPorIdentificacionIgnorandoEspaciosAlrededor() {
        GestorRepartidores gestor = new GestorRepartidores();
        Repartidor repartidor =
                crearRepartidor("ID-01", "Ana Perez", "Armenia");
        gestor.registrarRepartidor(repartidor);

        assertSame(repartidor, gestor.buscarPorIdentificacion("  ID-01  "));
    }

    @Test
    void devuelveNullCuandoLaIdentificacionNoExiste() {
        GestorRepartidores gestor = new GestorRepartidores();

        assertNull(gestor.buscarPorIdentificacion("ID-404"));
    }

    @Test
    void rechazaIdentificacionesNulasOVaciasEnLasConsultas() {
        GestorRepartidores gestor = new GestorRepartidores();

        assertThrows(DatoInvalidoException.class,
                () -> gestor.buscarPorIdentificacion(null));
        assertThrows(DatoInvalidoException.class,
                () -> gestor.buscarPorIdentificacion("   "));
        assertThrows(DatoInvalidoException.class,
                () -> gestor.actualizarDisponibilidad(null, true));
        assertThrows(DatoInvalidoException.class,
                () -> gestor.actualizarDisponibilidad("", false));
    }

    @Test
    void conservaElOrdenEnQueSeRegistranLosRepartidores() {
        GestorRepartidores gestor = new GestorRepartidores();
        Repartidor primero = crearRepartidor("ID-01", "Ana Perez", "Armenia");
        Repartidor segundo = crearRepartidor("ID-02", "Luis Gomez", "Salento");
        Repartidor tercero = crearRepartidor("ID-03", "Maria Lopez", "Calarca");

        gestor.registrarRepartidor(primero);
        gestor.registrarRepartidor(segundo);
        gestor.registrarRepartidor(tercero);

        assertEquals(List.of(primero, segundo, tercero),
                gestor.obtenerRepartidoresRegistrados());
    }

    @Test
    void noPermiteModificarLaListaDeRepartidoresDevuelta() {
        GestorRepartidores gestor = new GestorRepartidores();
        gestor.registrarRepartidor(
                crearRepartidor("ID-01", "Ana Perez", "Armenia"));

        assertThrows(UnsupportedOperationException.class,
                () -> gestor.obtenerRepartidoresRegistrados().clear());
        assertEquals(1, gestor.cantidadRepartidores());
    }

    @Test
    void asignaElPrimerRepartidorDisponibleDeLaZonaYLoMarcaNoDisponible() {
        GestorRepartidores gestor = new GestorRepartidores();
        Repartidor repartidor =
                crearRepartidor("ID-01", "Ana Perez", "Armenia");
        gestor.registrarRepartidor(repartidor);

        Repartidor asignado = gestor.asignarRepartidor(
                crearPaquete("PQ-01", "Armenia"));

        assertSame(repartidor, asignado);
        assertFalse(repartidor.isDisponible());
    }

    @Test
    void omiteRepartidoresNoDisponiblesYEligeElSiguienteCompatible() {
        GestorRepartidores gestor = new GestorRepartidores();
        Repartidor ocupado =
                new Repartidor("ID-01", "Ana Perez", "Armenia", false);
        Repartidor disponible =
                crearRepartidor("ID-02", "Luis Gomez", "Armenia");
        gestor.registrarRepartidor(ocupado);
        gestor.registrarRepartidor(disponible);

        Repartidor asignado = gestor.asignarRepartidor(
                crearPaquete("PQ-02", "Armenia"));

        assertSame(disponible, asignado);
        assertFalse(ocupado.isDisponible());
        assertFalse(disponible.isDisponible());
    }

    @Test
    void comparaLaZonaConElDestinoSinDistinguirMayusculas() {
        GestorRepartidores gestor = new GestorRepartidores();
        Repartidor repartidor =
                crearRepartidor("ID-01", "Ana Perez", "armenia");
        gestor.registrarRepartidor(repartidor);

        assertSame(repartidor, gestor.asignarRepartidor(
                crearPaquete("PQ-03", "Armenia")));
    }

    @Test
    void noAsignaUnRepartidorDeUnaZonaDiferente() {
        GestorRepartidores gestor = new GestorRepartidores();
        Repartidor repartidor =
                crearRepartidor("ID-01", "Ana Perez", "Salento");
        gestor.registrarRepartidor(repartidor);

        assertThrows(AsignacionInvalidaException.class,
                () -> gestor.asignarRepartidor(
                        crearPaquete("PQ-04", "Armenia")));
        assertTrue(repartidor.isDisponible());
    }

    @Test
    void rechazaLaAsignacionCuandoTodosLosRepartidoresCompatiblesEstanOcupados() {
        GestorRepartidores gestor = new GestorRepartidores();
        gestor.registrarRepartidor(
                new Repartidor("ID-01", "Ana Perez", "Armenia", false));

        assertThrows(AsignacionInvalidaException.class,
                () -> gestor.asignarRepartidor(
                        crearPaquete("PQ-05", "Armenia")));
    }

    @Test
    void noPermiteAsignarUnPaqueteNulo() {
        GestorRepartidores gestor = new GestorRepartidores();

        assertThrows(DatoInvalidoException.class,
                () -> gestor.asignarRepartidor(null));
    }

    @Test
    void permiteActualizarLaDisponibilidadDeUnRepartidor() {
        GestorRepartidores gestor = new GestorRepartidores();
        Repartidor repartidor =
                new Repartidor("ID-01", "Ana Perez", "Armenia", false);
        gestor.registrarRepartidor(repartidor);

        assertTrue(gestor.actualizarDisponibilidad("ID-01", true));
        assertTrue(repartidor.isDisponible());

        assertTrue(gestor.actualizarDisponibilidad(" ID-01 ", false));
        assertFalse(repartidor.isDisponible());
    }

    @Test
    void actualizarDisponibilidadDevuelveFalseSiNoExisteElRepartidor() {
        GestorRepartidores gestor = new GestorRepartidores();

        assertFalse(gestor.actualizarDisponibilidad("ID-404", true));
    }
}
