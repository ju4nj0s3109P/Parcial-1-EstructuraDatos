package quindioexpress.servicio;

import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;

/** Resultado de una entrega completada, con el paquete y su repartidor. */
public record ResultadoEntrega(Paquete paquete, Repartidor repartidor) {
}
