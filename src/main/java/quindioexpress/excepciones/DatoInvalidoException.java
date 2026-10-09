package quindioexpress.excepciones;
/** Dato que incumple las reglas del dominio. */
public class DatoInvalidoException extends IllegalArgumentException { public DatoInvalidoException(String mensaje){super(mensaje);} }
