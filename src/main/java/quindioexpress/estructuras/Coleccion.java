package quindioexpress.estructuras;
/** Contrato genérico para la colección propia. */
public interface Coleccion<T> extends Iterable<T> { void agregar(T elemento); boolean eliminar(T elemento); T obtener(int indice); int tamano(); boolean estaVacia(); }
