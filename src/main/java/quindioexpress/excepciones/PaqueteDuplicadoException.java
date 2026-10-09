
package quindioexpress.excepciones;

/**
 * indica que el codigo de un paquete ya se encuentra registrado.
 */
public class PaqueteDuplicadoException extends RuntimeException {

    public PaqueteDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
