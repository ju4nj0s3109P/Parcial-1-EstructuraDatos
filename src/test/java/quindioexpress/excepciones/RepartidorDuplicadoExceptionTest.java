
package quindioexpress.excepciones;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class RepartidorDuplicadoExceptionTest {

    @Test
    void conservaElMensajeRecibido() {
        RepartidorDuplicadoException excepcion =
                new RepartidorDuplicadoException(
                        "el repartidor con identificacion 123 ya existe"
                );

        assertEquals(
                "el repartidor con identificacion 123 ya existe",
                excepcion.getMessage()
        );
    }

    @Test
    void esUnaExcepcionDeEjecucion() {
        RepartidorDuplicadoException excepcion =
                new RepartidorDuplicadoException("repartidor duplicado");

        assertInstanceOf(RuntimeException.class, excepcion);
    }
}
