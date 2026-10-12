package quindioexpress;

import quindioexpress.datos.DatosPrueba;
import quindioexpress.excepciones.AsignacionInvalidaException;
import quindioexpress.modelo.Paquete;
import quindioexpress.servicio.CentroDistribucion;
import quindioexpress.servicio.CicloEntrega;
import quindioexpress.servicio.GestorDespacho;
import quindioexpress.servicio.GestorRepartidores;
import quindioexpress.servicio.HistorialEntregas;
import quindioexpress.servicio.ResultadoEntrega;

import java.util.Scanner;

/** Punto de entrada para demostrar el ciclo de entrega de QuindioExpress. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean salir = false;

            while (!salir) {
                mostrarMenu();
                String entrada = scanner.nextLine().trim();

                switch (entrada) {
                    case "1" -> ejecutarDespacho(CicloEntrega.Modalidad.FIFO);
                    case "2" -> ejecutarDespacho(CicloEntrega.Modalidad.PRIORIDAD);
                    case "0" -> salir = true;
                    default -> System.out.println("Opcion no valida. Selecciona 1, 2 o 0.");
                }
            }
        }

        System.out.println("Programa finalizado.");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("========== QUINDIOEXPRESS ==========");
        System.out.println("Selecciona UNA modalidad para esta simulacion:");
        System.out.println("1. Entrega FIFO (orden de llegada)");
        System.out.println("2. Entrega por prioridad (prioridad, tiempo y codigo)");
        System.out.println("0. Salir");
        System.out.print("Opcion: ");
    }

    /**
     * Cada ejecucion crea un escenario limpio y utiliza una sola modalidad
     * durante todo el ciclo para evitar entregar un paquete dos veces desde
     * las dos colas alternativas del gestor.
     */
    private static void ejecutarDespacho(CicloEntrega.Modalidad modalidad) {
        CentroDistribucion centro = DatosPrueba.crearCentroDistribucion();
        GestorDespacho despacho = DatosPrueba.crearGestorDespacho(centro);
        GestorRepartidores repartidores = DatosPrueba.crearGestorRepartidores();
        HistorialEntregas historial = new HistorialEntregas();

        CicloEntrega ciclo = new CicloEntrega(
                centro, despacho, repartidores, historial, modalidad);

        System.out.println();
        System.out.println("Modalidad seleccionada: " + modalidad);
        System.out.println("Paquetes al iniciar: " + centro.cantidadPaquetes());
        System.out.println("------------------------------------");

        while (true) {
            try {
                ResultadoEntrega resultado = ciclo.procesarSiguienteEntrega();
                if (resultado == null) {
                    break;
                }

                Paquete paquete = resultado.paquete();
                System.out.printf(
                        "Entregado %s -> %s | destino: %s | prioridad: %d | repartidor: %s%n",
                        paquete.getCodigo(),
                        resultado.repartidor().getNombre(),
                        paquete.getDestino(),
                        paquete.getPrioridad(),
                        resultado.repartidor().getIdentificacion());
            } catch (AsignacionInvalidaException ex) {
                System.out.println("No se puede continuar: " + ex.getMessage());
                System.out.println("El paquete sigue pendiente y no se agrega al historial.");
                break;
            }
        }

        System.out.println("------------------------------------");
        System.out.println("Entregas registradas: " + historial.cantidadEntregas());
        System.out.println("Paquetes que siguen en el centro: " + centro.cantidadPaquetes());
        System.out.println("Pendientes en la modalidad elegida: "
                + (modalidad == CicloEntrega.Modalidad.FIFO
                ? despacho.cantidadPendientesFIFO()
                : despacho.cantidadPendientesPrioritarios()));
        System.out.println("Historial en orden real de entrega:");

        for (Paquete paquete : historial) {
            System.out.println(" - " + paquete.getCodigo() + " | " + paquete.getDestino());
        }

        System.out.println();
        System.out.println("La siguiente modalidad, si se elige, comienza con datos nuevos.");
    }
}
