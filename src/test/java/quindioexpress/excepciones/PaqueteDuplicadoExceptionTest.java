
package quindioexpress.excepciones;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class PaqueteDuplicadoExceptionTest {

    @Test
    void conservaElMensajeRecibido() {
        PaqueteDuplicadoException excepcion =
                new PaqueteDuplicadoException(
                        "el paquete con codigo PQ-01 ya existe"
                );

        assertEquals(
                "el paquete con codigo PQ-01 ya existe",
                excepcion.getMessage()
        );
    }

    @Test
    void esUnaExcepcionDeEjecucion() {
        PaqueteDuplicadoException excepcion =
                new PaqueteDuplicadoException("paquete duplicado");

        assertInstanceOf(RuntimeException.class, excepcion);
    }
}
