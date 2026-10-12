package quindioexpress.datos;

import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;
import quindioexpress.servicio.CentroDistribucion;
import quindioexpress.servicio.GestorDespacho;
import quindioexpress.servicio.GestorRepartidores;

import java.util.ArrayList;
import java.util.List;
public final class DatosPrueba {
    private DatosPrueba() {
    }

    /**
     * Crea los 32 paquetes de la jornada en el ORDEN DE LLEGADA al centro
     */
    public static List<Paquete> crearPaquetes() {
        List<Paquete> paquetes = new ArrayList<>();
        //                      código   destino       peso  prio  minutos
        paquetes.add(new Paquete("PQ731", "Armenia",    12.0, 5, 45));
        paquetes.add(new Paquete("PQ105", "Salento",     4.5, 5, 25));
        paquetes.add(new Paquete("PQ942", "Armenia",     3.2, 4, 20));
        paquetes.add(new Paquete("PQ318", "Calarcá",    18.7, 2, 35));
        paquetes.add(new Paquete("PQ567", "Salento",     7.4, 3, 50));
        paquetes.add(new Paquete("PQ829", "Armenia",     5.1, 1, 15));
        paquetes.add(new Paquete("PQ420", "Montenegro",  2.8, 3, 30));
        paquetes.add(new Paquete("PQ214", "Quimbaya",    9.6, 4, 55));
        paquetes.add(new Paquete("PQ421", "Circasia",    1.9, 2, 40));
        paquetes.add(new Paquete("PQ842", "Calarcá",     6.3, 5, 30));
        paquetes.add(new Paquete("PQ156", "Armenia",     2.2, 3, 10));
        paquetes.add(new Paquete("PQ603", "Montenegro", 11.4, 4, 60));
        paquetes.add(new Paquete("PQ377", "Salento",     0.8, 1, 70));
        paquetes.add(new Paquete("PQ918", "Quimbaya",    4.0, 5, 35));
        paquetes.add(new Paquete("PQ249", "Circasia",    3.7, 3, 25));
        paquetes.add(new Paquete("PQ685", "Armenia",    15.3, 2, 20));
        paquetes.add(new Paquete("PQ532", "Calarcá",     2.6, 4, 18));
        paquetes.add(new Paquete("PQ764", "Montenegro",  5.9, 3, 42));
        paquetes.add(new Paquete("PQ193", "Quimbaya",    8.1, 1, 65));
        paquetes.add(new Paquete("PQ456", "Salento",     1.5, 5, 40));
        paquetes.add(new Paquete("PQ871", "Circasia",   10.2, 4, 28));
        paquetes.add(new Paquete("PQ338", "Armenia",     6.6, 3, 12));
        paquetes.add(new Paquete("PQ627", "Calarcá",     3.3, 2, 22));
        paquetes.add(new Paquete("PQ975", "Montenegro",  7.7, 5, 48));
        paquetes.add(new Paquete("PQ284", "Quimbaya",    2.4, 3, 33));
        paquetes.add(new Paquete("PQ509", "Circasia",    4.8, 1, 52));
        paquetes.add(new Paquete("PQ716", "Armenia",     9.0, 4, 16));
        paquetes.add(new Paquete("PQ362", "Salento",    12.5, 2, 58));
        paquetes.add(new Paquete("PQ148", "Calarcá",     1.1, 5, 14));
        paquetes.add(new Paquete("PQ893", "Montenegro",  3.9, 3, 38));
        paquetes.add(new Paquete("PQ271", "Armenia",     5.5, 5, 25));
        paquetes.add(new Paquete("PQ654", "Quimbaya",    6.8, 4, 20));
        return paquetes;
    }

    /**
     * Crea los 8 repartidores de la jornada en orden de registro
     */
    public static List<Repartidor> crearRepartidores() {
        List<Repartidor> repartidores = new ArrayList<>();
        repartidores.add(new Repartidor("R001", "Laura Gómez Ríos",       "Armenia",    true));
        repartidores.add(new Repartidor("R002", "Carlos Restrepo Mejía",  "Armenia",    false));
        repartidores.add(new Repartidor("R003", "Diana Ocampo Salazar",   "Calarcá",    true));
        repartidores.add(new Repartidor("R004", "Andrés Valencia Ruiz",   "Salento",    true));
        repartidores.add(new Repartidor("R005", "Paula Henao Cardona",    "Montenegro", true));
        repartidores.add(new Repartidor("R006", "Jorge Marín López",      "Quimbaya",   true));
        repartidores.add(new Repartidor("R007", "Natalia Arango Pérez",   "Circasia",   false));
        repartidores.add(new Repartidor("R008", "Felipe Castaño Giraldo", "Circasia",   true));
        return repartidores;
    }

    /**
     * Crea un {@link CentroDistribucion} con los 32 paquetes ya registrados
     * en orden de llegada
     */
    public static CentroDistribucion crearCentroDistribucion() {
        CentroDistribucion centro = new CentroDistribucion();
        for (Paquete paquete : crearPaquetes()) {
            centro.registrarPaquete(paquete);
        }
        return centro;
    }

    /**
     * Crea un {@link GestorRepartidores} con los 8 repartidores registrados
     */
    public static GestorRepartidores crearGestorRepartidores() {
        GestorRepartidores gestor = new GestorRepartidores();
        for (Repartidor repartidor : crearRepartidores()) {
            gestor.registrarRepartidor(repartidor);
        }
        return gestor;
    }

    /**
     * Crea un {@link GestorDespacho} con los paquetes de un centro, en el
     * orden en que fueron registrados
     */
    public static GestorDespacho crearGestorDespacho(CentroDistribucion centro) {
        GestorDespacho gestor = new GestorDespacho();
        for (Paquete paquete : centro.obtenerPaquetesRegistrados()) {
            gestor.agregarPaquete(paquete);
        }
        return gestor;
    }

    /**
     * Ejemplo del enunciado para el peso total por municipio:
     * PQ101 Armenia 4.5, PQ203 Salento 3.0, PQ415 Armenia 7.2, PQ730 Armenia 2.3.
     * Peso total de Armenia = 14.0 kg
     */
    public static List<Paquete> escenarioPesoPorMunicipio() {
        List<Paquete> paquetes = new ArrayList<>();
        paquetes.add(new Paquete("PQ101", "Armenia", 4.5, 3, 20));
        paquetes.add(new Paquete("PQ203", "Salento", 3.0, 3, 30));
        paquetes.add(new Paquete("PQ415", "Armenia", 7.2, 3, 25));
        paquetes.add(new Paquete("PQ730", "Armenia", 2.3, 3, 15));
        return paquetes;
    }

    /**
     * Ejemplo del enunciado para el conteo por prioridad mínima:
     * prioridades 5, 2, 4, 1, 5, 3, 4. Con mínimo 4 el resultado es 4
     */
    public static List<Paquete> escenarioPrioridades() {
        List<Paquete> paquetes = new ArrayList<>();
        paquetes.add(new Paquete("PQ111", "Armenia",  1.0, 5, 10));
        paquetes.add(new Paquete("PQ222", "Salento",  1.0, 2, 10));
        paquetes.add(new Paquete("PQ333", "Calarcá",  1.0, 4, 10));
        paquetes.add(new Paquete("PQ444", "Armenia",  1.0, 1, 10));
        paquetes.add(new Paquete("PQ555", "Salento",  1.0, 5, 10));
        paquetes.add(new Paquete("PQ666", "Calarcá",  1.0, 3, 10));
        paquetes.add(new Paquete("PQ777", "Armenia",  1.0, 4, 10));
        return paquetes;
    }

    /**
     * Ejemplo del enunciado para la búsqueda binaria: paquetes YA ordenados
     * por código (PQ105, PQ214, PQ318, PQ421, PQ567, PQ731, PQ842, PQ942).
     * PQ731 está en la posición 5
     */
    public static List<Paquete> escenarioBusquedaBinaria() {
        List<Paquete> paquetes = new ArrayList<>();
        paquetes.add(new Paquete("PQ105", "Salento",    4.5, 5, 25));
        paquetes.add(new Paquete("PQ214", "Quimbaya",   9.6, 4, 55));
        paquetes.add(new Paquete("PQ318", "Calarcá",   18.7, 2, 35));
        paquetes.add(new Paquete("PQ421", "Circasia",   1.9, 2, 40));
        paquetes.add(new Paquete("PQ567", "Salento",    7.4, 3, 50));
        paquetes.add(new Paquete("PQ731", "Armenia",   12.0, 5, 45));
        paquetes.add(new Paquete("PQ842", "Calarcá",    6.3, 5, 30));
        paquetes.add(new Paquete("PQ942", "Armenia",    3.2, 4, 20));
        return paquetes;
    }

    /**
     * Ejemplo del enunciado para divide y vencerás: pesos
     * 4.5, 12.0, 3.2, 18.7, 7.4, 5.1. El más pesado es el de 18.7 kg (PQ318)
     */
    public static List<Paquete> escenarioDivideYVenceras() {
        List<Paquete> paquetes = new ArrayList<>();
        paquetes.add(new Paquete("PQ105", "Salento",  4.5, 5, 25));
        paquetes.add(new Paquete("PQ731", "Armenia", 12.0, 5, 45));
        paquetes.add(new Paquete("PQ942", "Armenia",  3.2, 4, 20));
        paquetes.add(new Paquete("PQ318", "Calarcá", 18.7, 2, 35));
        paquetes.add(new Paquete("PQ567", "Salento",  7.4, 3, 50));
        paquetes.add(new Paquete("PQ829", "Armenia",  5.1, 1, 15));
        return paquetes;
    }

    /**
     * Ejemplo del enunciado para el despacho prioritario:
     * PQ420 (3, 30), PQ731 (5, 45), PQ105 (5, 25), PQ942 (4, 20).
     * Orden esperado: PQ105, PQ731, PQ942, PQ420.
     */
    public static List<Paquete> escenarioDespacho() {
        List<Paquete> paquetes = new ArrayList<>();
        paquetes.add(new Paquete("PQ420", "Montenegro", 2.8, 3, 30));
        paquetes.add(new Paquete("PQ731", "Armenia",   12.0, 5, 45));
        paquetes.add(new Paquete("PQ105", "Salento",    4.5, 5, 25));
        paquetes.add(new Paquete("PQ942", "Armenia",    3.2, 4, 20));
        return paquetes;
    }
}