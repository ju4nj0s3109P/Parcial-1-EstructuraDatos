package quindioexpress.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepartidorTest {

    @Test
    void guardaLosDatosYRecortaLosEspacios() {
        Repartidor repartidor = new Repartidor(" ID-01 ", " Ana Perez ", " Armenia ", false);

        assertEquals("ID-01", repartidor.getIdentificacion());
        assertEquals("Ana Perez", repartidor.getNombre());
        assertEquals("Armenia", repartidor.getZona());
        assertFalse(repartidor.isDisponible());
    }

    @Test
    void quedaDisponibleCuandoNoSeIndicaElEstado() {
        Repartidor repartidor = new Repartidor("ID-02", "Luis Gomez", "Pereira");

        assertTrue(repartidor.isDisponible());
        assertTrue(repartidor.estaDisponible());
    }

    @Test
    void noAceptaIdentificacionVacia() {
        assertThrows(IllegalArgumentException.class,
                () -> new Repartidor("  ", "Ana Perez", "Armenia"));
    }

    @Test
    void noAceptaIdentificacionNula() {
        assertThrows(IllegalArgumentException.class,
                () -> new Repartidor(null, "Ana Perez", "Armenia"));
    }

    @Test
    void noAceptaNombreVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Repartidor("ID-01", " ", "Armenia"));
    }

    @Test
    void noAceptaNombreNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new Repartidor("ID-01", null, "Armenia"));
    }

    @Test
    void noAceptaZonaVacia() {
        assertThrows(IllegalArgumentException.class,
                () -> new Repartidor("ID-01", "Ana Perez", " "));
    }

    @Test
    void noAceptaZonaNula() {
        assertThrows(IllegalArgumentException.class,
                () -> new Repartidor("ID-01", "Ana Perez", null));
    }

    @Test
    void permiteActualizarLosDatosYLaDisponibilidad() {
        Repartidor repartidor = new Repartidor("ID-01", "Ana Perez", "Armenia");

        repartidor.setNombre("Maria Lopez");
        repartidor.setZona("Calarca");
        repartidor.setDisponible(false);

        assertEquals("Maria Lopez", repartidor.getNombre());
        assertEquals("Calarca", repartidor.getZona());
        assertFalse(repartidor.estaDisponible());
    }

    @Test
    void validaLosDatosCuandoSeActualizan() {
        Repartidor repartidor = new Repartidor("ID-01", "Ana Perez", "Armenia");

        assertThrows(IllegalArgumentException.class, () -> repartidor.setIdentificacion(""));
        assertThrows(IllegalArgumentException.class, () -> repartidor.setNombre(" "));
        assertThrows(IllegalArgumentException.class, () -> repartidor.setZona(null));
    }

    @Test
    void muestraLosDatosEnElTexto() {
        Repartidor repartidor = new Repartidor("ID-01", "Ana Perez", "Armenia", true);

        assertTrue(repartidor.toString().contains("ID-01"));
        assertTrue(repartidor.toString().contains("Ana Perez"));
        assertTrue(repartidor.toString().contains("Armenia"));
        assertTrue(repartidor.toString().contains("disponible=true"));
    }
}
