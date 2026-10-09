package quindioexpress.estructuras;
/** Nodo interno de la lista enlazada. No se expone a clientes externos. */
final class Nodo<T> { T valor; Nodo<T> siguiente; Nodo(T valor) { this.valor=valor; } }
