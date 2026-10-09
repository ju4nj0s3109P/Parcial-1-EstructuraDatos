package quindioexpress.excepciones;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class DatoInvalidoExceptionTest {

    @Test
    void conservaElMensajeRecibido() {
        DatoInvalidoException excepcion =
                new DatoInvalidoException("el peso debe ser mayor que cero");

        assertEquals(
                "el peso debe ser mayor que cero",
                excepcion.getMessage()
        );
    }

    @Test
    void esUnaExcepcionDeArgumentoIlegal() {
        DatoInvalidoException excepcion =
                new DatoInvalidoException("dato invalido");

        assertInstanceOf(IllegalArgumentException.class, excepcion);
    }
}
