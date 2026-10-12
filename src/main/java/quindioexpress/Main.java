package quindioexpress;

import quindioexpress.algoritmos.BusquedaBinaria;
import quindioexpress.algoritmos.DivideYVenceras;
import quindioexpress.algoritmos.Recursividad;
import quindioexpress.comparadores.ComparadorPesoDesc;
import quindioexpress.comparadores.ComparadorPrioridadDesc;
import quindioexpress.comparadores.ComparadorTiempoAsc;
import quindioexpress.datos.DatosPrueba;
import quindioexpress.estructuras.ListaSimple;
import quindioexpress.excepciones.AsignacionInvalidaException;
import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;
import quindioexpress.servicio.CentroDistribucion;
import quindioexpress.servicio.CicloEntrega;
import quindioexpress.servicio.GestorDespacho;
import quindioexpress.servicio.GestorRepartidores;
import quindioexpress.servicio.HistorialEntregas;
import quindioexpress.servicio.ReporteMunicipios;
import quindioexpress.servicio.ResultadoEntrega;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Punto de entrada y demostrador de las funcionalidades del parcial.
 *
 * Cada opción presenta una funcionalidad concreta usando las clases de su
 * paquete correspondiente; el Main coordina las demostraciones, no reimplementa
 * las estructuras ni los algoritmos.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean salir = false;

            while (!salir) {
                mostrarMenu();
                String opcion = scanner.nextLine().trim();

                try {
                    switch (opcion) {
                        case "1" -> demostrarRegistroYConsultas();
                        case "2" -> demostrarReportesMunicipales();
                        case "3" -> demostrarRecursividad();
                        case "4" -> demostrarBusquedaBinaria();
                        case "5" -> demostrarDivideYVenceras();
                        case "6" -> demostrarOrdenamientosYComparadores();
                        case "7" -> demostrarListaSimple();
                        case "8" -> ejecutarDespacho(CicloEntrega.Modalidad.FIFO);
                        case "9" -> ejecutarDespacho(CicloEntrega.Modalidad.PRIORIDAD);
                        case "0" -> salir = true;
                        default -> System.out.println("Opción no válida. Selecciona una opción del menú.");
                    }
                } catch (RuntimeException ex) {
                    System.out.println("No se pudo completar esta demostración: " + ex.getMessage());
                }
            }
        }

        System.out.println("Programa finalizado.");
    }

    private static void mostrarMenu() {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("       QUINDIOEXPRESS - DEMOSTRADOR DEL PARCIAL");
        System.out.println("==================================================");
        System.out.println("1. Registro, orden de llegada y búsqueda por código");
        System.out.println("2. Municipios únicos, ordenados y paquetes agrupados");
        System.out.println("3. Recursividad: peso por municipio y conteo por prioridad");
        System.out.println("4. Búsqueda binaria recursiva");
        System.out.println("5. Divide y vencerás");
        System.out.println("6. Comparable y comparadores");
        System.out.println("7. Lista simplemente enlazada propia e iterador");
        System.out.println("8. Simular despacho FIFO");
        System.out.println("9. Simular despacho por prioridad");
        System.out.println("0. Salir");
        System.out.print("Selecciona una opción: ");
    }

    /** Demuestra el registro, la conservación del orden y las consultas del centro. */
    private static void demostrarRegistroYConsultas() {
        imprimirTitulo("1. REGISTRO Y CONSULTAS");

        CentroDistribucion centro = DatosPrueba.crearCentroDistribucion();
        List<Paquete> paquetes = centro.obtenerPaquetesRegistrados();
        List<Repartidor> repartidores = DatosPrueba.crearRepartidores();

        System.out.println("Paquetes registrados: " + centro.cantidadPaquetes());
        System.out.println("Repartidores de prueba: " + repartidores.size());
        System.out.println("\nPrimeros cinco paquetes en orden de llegada:");
        for (int i = 0; i < Math.min(5, paquetes.size()); i++) {
            imprimirPaquete(paquetes.get(i));
        }

        String codigoBuscado = "PQ731";
        Paquete encontrado = centro.buscarPorCodigo(codigoBuscado);
        System.out.println("\nConsulta por código " + codigoBuscado + ":");
        System.out.println(encontrado == null ? "No existe." : encontrado);

        System.out.println("\nRepartidores en orden de registro (incluye su disponibilidad):");
        for (Repartidor repartidor : repartidores) {
            System.out.printf("%s | %s | zona: %s | disponible: %s%n",
                    repartidor.getIdentificacion(), repartidor.getNombre(),
                    repartidor.getZona(), repartidor.isDisponible() ? "sí" : "no");
        }

        System.out.println("\nQué demuestra:");
        System.out.println("- CentroDistribucion conserva el orden de registro y busca por código.");
        System.out.println("- Los códigos de paquete y las identificaciones de repartidor deben ser únicos.");
        System.out.println("- La asignación debe respetar disponibilidad y coincidencia de zona.");
    }

    /** Demuestra unicidad, orden alfabético y agrupación por destino. */
    private static void demostrarReportesMunicipales() {
        imprimirTitulo("2. REPORTES POR MUNICIPIO");

        CentroDistribucion centro = DatosPrueba.crearCentroDistribucion();
        ReporteMunicipios reporte = new ReporteMunicipios(centro);

        Set<String> municipiosUnicos = reporte.obtenerMunicipiosUnicos();
        Set<String> municipiosOrdenados = reporte.obtenerMunicipiosOrdenados();
        Map<String, List<Paquete>> agrupados = reporte.agruparPaquetesPorMunicipio();

        System.out.println("Municipios únicos (sin exigir orden):");
        System.out.println(municipiosUnicos);

        System.out.println("\nMunicipios únicos en orden alfabético:");
        System.out.println(municipiosOrdenados);

        System.out.println("\nPaquetes agrupados por municipio:");
        for (Map.Entry<String, List<Paquete>> entrada : agrupados.entrySet()) {
            System.out.printf("%s (%d paquetes): ", entrada.getKey(), entrada.getValue().size());
            for (int i = 0; i < entrada.getValue().size(); i++) {
                if (i > 0) {
                    System.out.print(", ");
                }
                System.out.print(entrada.getValue().get(i).getCodigo());
            }
            System.out.println();
        }
    }

    /** Usa los escenarios concretos del enunciado para las dos operaciones recursivas. */
    private static void demostrarRecursividad() {
        imprimirTitulo("3. RECURSIVIDAD");

        List<Paquete> escenarioPeso = DatosPrueba.escenarioPesoPorMunicipio();
        String municipio = "Armenia";
        double pesoTotal = Recursividad.pesoTotalPorMunicipio(escenarioPeso, municipio);

        System.out.println("A) Peso total por municipio");
        System.out.println("Municipio consultado: " + municipio);
        for (Paquete paquete : escenarioPeso) {
            imprimirPaquete(paquete);
        }
        System.out.printf("Resultado recursivo: %.2f kg (esperado: 14.00 kg)%n", pesoTotal);

        List<Paquete> escenarioPrioridad = DatosPrueba.escenarioPrioridades();
        int prioridadMinima = 4;
        int cantidad = Recursividad.contarConPrioridadMinima(escenarioPrioridad, prioridadMinima);

        System.out.println("\nB) Cantidad de paquetes con prioridad mayor o igual a " + prioridadMinima);
        for (Paquete paquete : escenarioPrioridad) {
            System.out.printf("%s -> prioridad %d%n", paquete.getCodigo(), paquete.getPrioridad());
        }
        System.out.println("Resultado recursivo: " + cantidad + " (esperado: 4)");

        System.out.println("\nPuntos para explicar en la sustentación:");
        System.out.println("- Caso base: el índice llega al tamaño de la lista y se devuelve 0.");
        System.out.println("- Caso recursivo: se procesa el paquete actual y se llama con índice + 1.");
        System.out.println("- Cada llamada espera el resultado del resto y luego suma su aporte.");
        System.out.println("- Estos dos métodos de Recursividad no reemplazan la recursión por ciclos.");
    }

    /** Demuestra la búsqueda binaria en una lista preparada y ordenada por código. */
    private static void demostrarBusquedaBinaria() {
        imprimirTitulo("4. BÚSQUEDA BINARIA");

        List<Paquete> ordenados = DatosPrueba.escenarioBusquedaBinaria();
        // El escenario ya viene ordenado; ordenarPorCodigo permite mostrar
        // explícitamente el contrato que exige el algoritmo.
        ordenados = BusquedaBinaria.ordenarPorCodigo(ordenados);

        System.out.println("Lista ordenada por código:");
        for (int i = 0; i < ordenados.size(); i++) {
            System.out.printf("[%d] %s%n", i, ordenados.get(i).getCodigo());
        }

        String codigoExistente = "PQ731";
        int posicion = BusquedaBinaria.buscarPosicion(ordenados, codigoExistente);
        Paquete paquete = BusquedaBinaria.buscar(ordenados, codigoExistente);
        System.out.printf("%nBúsqueda de %s: posición %d; paquete: %s%n",
                codigoExistente, posicion, paquete);

        String codigoInexistente = "PQ999";
        System.out.printf("Búsqueda de %s: posición %d ( -1 significa no encontrado).%n",
                codigoInexistente, BusquedaBinaria.buscarPosicion(ordenados, codigoInexistente));

        System.out.println("\nLa búsqueda binaria requiere que la colección esté ordenada por el mismo criterio.");
        System.out.println("En este caso, el orden natural de Paquete compara el código.");
    }

    /** Demuestra el algoritmo de máximo y la suma por divide y vencerás. */
    private static void demostrarDivideYVenceras() {
        imprimirTitulo("5. DIVIDE Y VENCERÁS");

        List<Paquete> paquetes = DatosPrueba.escenarioDivideYVenceras();
        System.out.println("Conjunto de prueba:");
        for (Paquete paquete : paquetes) {
            imprimirPaquete(paquete);
        }

        Paquete masPesado = DivideYVenceras.paqueteMasPesado(paquetes);
        double pesoTotal = DivideYVenceras.pesoTotal(paquetes);

        System.out.println("\nPaquete de mayor peso:");
        System.out.println(masPesado);
        System.out.printf("Peso acumulado de los seis paquetes: %.2f kg%n", pesoTotal);

        System.out.println("\nCómo explicar el algoritmo:");
        System.out.println("- Caso base: el rango contiene un solo paquete.");
        System.out.println("- División: el rango se parte en mitades.");
        System.out.println("- Conquista: se resuelve recursivamente cada mitad.");
        System.out.println("- Combinación: se comparan los ganadores de ambas mitades.");
    }

    /** Demuestra Comparable y los comparadores sin alterar la lista original. */
    private static void demostrarOrdenamientosYComparadores() {
        imprimirTitulo("6. COMPARABLE Y COMPARADORES");

        List<Paquete> originales = DatosPrueba.escenarioDespacho();
        System.out.println("Lista original de llegada:");
        imprimirPaquetes(originales);

        List<Paquete> porCodigo = new ArrayList<>(originales);
        Collections.sort(porCodigo);
        System.out.println("\nComparable<Paquete>: código ascendente:");
        imprimirPaquetes(porCodigo);

        mostrarOrdenPorComparador(originales, new ComparadorPrioridadDesc(),
                "ComparadorPrioridadDesc: prioridad descendente");
        mostrarOrdenPorComparador(originales, new ComparadorPesoDesc(),
                "ComparadorPesoDesc: peso descendente");
        mostrarOrdenPorComparador(originales, new ComparadorTiempoAsc(),
                "ComparadorTiempoAsc: tiempo estimado ascendente");

        List<Paquete> ordenDespacho = new ArrayList<>(originales);
        ordenDespacho.sort(Comparator
                .comparingInt(Paquete::getPrioridad).reversed()
                .thenComparingInt(Paquete::getTiempoEstimado)
                .thenComparing(Paquete::getCodigo));
        System.out.println("\nRegla de despacho prioritario (prioridad ↓, tiempo ↑, código ↑):");
        imprimirPaquetes(ordenDespacho);

        System.out.println("\nLa cola prioritaria usa ComparadorDespacho, que aplica esos tres criterios.");
        System.out.println("La modalidad FIFO usa Queue respaldada por ArrayDeque; la prioritaria usa PriorityQueue.");
        System.out.println("ArrayDeque también implementa Deque, aunque en el despacho se usa como cola FIFO.");
    }

    /** Demuestra la lista genérica propia, acceso por índice, eliminación e Iterable. */
    private static void demostrarListaSimple() {
        imprimirTitulo("7. LISTA SIMPLEMENTE ENLAZADA PROPIA");

        ListaSimple<String> lista = new ListaSimple<>();
        System.out.println("Lista vacía al crearla: " + lista.estaVacia());

        lista.agregar("Armenia");
        lista.agregar("Salento");
        lista.agregar("Calarcá");

        System.out.println("Tamaño después de agregar tres elementos: " + lista.tamano());
        System.out.println("Elemento en índice 1: " + lista.obtener(1));

        System.out.println("\nRecorrido externo mediante for-each (Iterable):");
        for (String municipio : lista) {
            System.out.println("- " + municipio);
        }

        System.out.println("\nRecorrido explícito mediante Iterator:");
        Iterator<String> iterador = lista.iterator();
        while (iterador.hasNext()) {
            System.out.println("-> " + iterador.next());
        }

        System.out.println("\nEliminar Salento: " + lista.eliminar("Salento"));
        System.out.println("Contenido después de eliminar:");
        for (String municipio : lista) {
            System.out.println("- " + municipio);
        }

        System.out.println("\nLa lista guarda nodos propios y expone sus elementos mediante Iterator,");
        System.out.println("sin exponer los nodos internos al código cliente.");
    }

    /** Simula una sola modalidad usando una instancia de datos limpia. */
    private static void ejecutarDespacho(CicloEntrega.Modalidad modalidad) {
        imprimirTitulo("8. CICLO DE ENTREGA - " + modalidad);

        CentroDistribucion centro = DatosPrueba.crearCentroDistribucion();
        GestorDespacho despacho = DatosPrueba.crearGestorDespacho(centro);
        GestorRepartidores repartidores = DatosPrueba.crearGestorRepartidores();
        HistorialEntregas historial = new HistorialEntregas();

        CicloEntrega ciclo = new CicloEntrega(
                centro, despacho, repartidores, historial, modalidad);

        System.out.println("Paquetes al iniciar: " + centro.cantidadPaquetes());
        System.out.println("Repartidores registrados: " + repartidores.cantidadRepartidores());
        System.out.println("Cada modalidad empieza con datos nuevos para evitar mezclar colas.");

        while (true) {
            try {
                ResultadoEntrega resultado = ciclo.procesarSiguienteEntrega();
                if (resultado == null) {
                    break;
                }

                Paquete paquete = resultado.paquete();
                Repartidor repartidor = resultado.repartidor();
                System.out.printf("ENTREGA | %s | destino: %s | prioridad: %d | "
                                + "tiempo: %d min | repartidor: %s (%s)%n",
                        paquete.getCodigo(), paquete.getDestino(), paquete.getPrioridad(),
                        paquete.getTiempoEstimado(), repartidor.getNombre(),
                        repartidor.getIdentificacion());
            } catch (AsignacionInvalidaException ex) {
                System.out.println("Entrega detenida: " + ex.getMessage());
                System.out.println("El paquete que no pudo asignarse queda pendiente.");
                break;
            }
        }

        System.out.println("\nResumen final:");
        System.out.println("Entregas registradas: " + historial.cantidadEntregas());
        System.out.println("Paquetes pendientes en el centro: " + centro.cantidadPaquetes());
        System.out.println("Pendientes en la cola seleccionada: "
                + (modalidad == CicloEntrega.Modalidad.FIFO
                ? despacho.cantidadPendientesFIFO()
                : despacho.cantidadPendientesPrioritarios()));

        System.out.println("\nHistorial en el orden real de entrega:");
        int consecutivo = 1;
        for (Paquete paquete : historial) {
            System.out.printf("%02d. %s -> %s%n",
                    consecutivo++, paquete.getCodigo(), paquete.getDestino());
        }
    }

    private static void mostrarOrdenPorComparador(
            List<Paquete> originales, Comparator<Paquete> comparador, String titulo) {
        List<Paquete> copia = new ArrayList<>(originales);
        copia.sort(comparador);
        System.out.println("\n" + titulo + ":");
        imprimirPaquetes(copia);
    }

    private static void imprimirPaquetes(List<Paquete> paquetes) {
        for (Paquete paquete : paquetes) {
            imprimirPaquete(paquete);
        }
    }

    private static void imprimirPaquete(Paquete paquete) {
        System.out.printf("%s | destino: %-11s | peso: %5.1f kg | prioridad: %d | tiempo: %d min%n",
                paquete.getCodigo(), paquete.getDestino(), paquete.getPeso(),
                paquete.getPrioridad(), paquete.getTiempoEstimado());
    }

    private static void imprimirTitulo(String titulo) {
        System.out.println();
        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println(titulo);
        System.out.println("--------------------------------------------------");
    }
}
