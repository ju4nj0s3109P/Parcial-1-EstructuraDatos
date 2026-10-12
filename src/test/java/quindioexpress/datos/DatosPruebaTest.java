package quindioexpress.datos;

import org.junit.jupiter.api.Test;
import quindioexpress.modelo.Paquete;
import quindioexpress.modelo.Repartidor;
import quindioexpress.servicio.CentroDistribucion;
import quindioexpress.servicio.GestorDespacho;
import quindioexpress.servicio.GestorRepartidores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifica que los datos de prueba cumplen los mínimos del enunciado y las
 * reglas del dominio. Si alguien modifica DatosPrueba y rompe un mínimo,
 * estas pruebas lo detectan.
 */
class DatosPruebaTest {

    /** Mínimo del enunciado: 30 paquetes. */
    @Test
    void hayAlMenosTreintaPaquetes() {
        assertTrue(DatosPrueba.crearPaquetes().size() >= 30);
    }

    /** Regla: no pueden existir dos paquetes con el mismo código. */
    @Test
    void losCodigosDePaqueteSonUnicos() {
        Set<String> codigos = new HashSet<>();
        for (Paquete p : DatosPrueba.crearPaquetes()) {
            assertTrue(codigos.add(p.getCodigo()), "código repetido: " + p.getCodigo());
        }
    }

    /** Formato fijo PQ + 3 dígitos, para que el orden alfabético sea igual al numérico. */
    @Test
    void losCodigosTienenFormatoFijoPQTresDigitos() {
        for (Paquete p : DatosPrueba.crearPaquetes()) {
            assertTrue(p.getCodigo().matches("PQ\\d{3}"), "formato inválido: " + p.getCodigo());
        }
    }

    /** Mínimo del enunciado: códigos no consecutivos y sin relación con el orden de llegada. */
    @Test
    void losCodigosNoSonConsecutivosNiSiguenElOrdenDeLlegada() {
        List<Paquete> llegada = DatosPrueba.crearPaquetes();
        List<Paquete> porCodigo = new ArrayList<>(llegada);
        Collections.sort(porCodigo);

        // El orden de llegada no coincide con el orden por código.
        assertNotEquals(llegada, porCodigo);

        // Los códigos no forman una secuencia (PQ001, PQ002, ...). Solo hay un
        // par vecino consecutivo (PQ420 y PQ421), ambos tomados del enunciado.
        int paresConsecutivos = 0;
        for (int i = 1; i < porCodigo.size(); i++) {
            int anterior = Integer.parseInt(porCodigo.get(i - 1).getCodigo().substring(2));
            int actual = Integer.parseInt(porCodigo.get(i).getCodigo().substring(2));
            if (actual == anterior + 1) {
                paresConsecutivos++;
            }
        }
        assertTrue(paresConsecutivos <= 1);
    }

    /** Mínimo del enunciado: al menos 5 destinos diferentes. */
    @Test
    void hayAlMenosCincoDestinosDiferentes() {
        Set<String> destinos = new HashSet<>();
        for (Paquete p : DatosPrueba.crearPaquetes()) {
            destinos.add(p.getDestino());
        }
        assertTrue(destinos.size() >= 5);
    }

    /** Mínimo del enunciado: varios paquetes destinados al mismo municipio. */
    @Test
    void variosPaquetesVanAlMismoMunicipio() {
        Map<String, Integer> conteo = new HashMap<>();
        for (Paquete p : DatosPrueba.crearPaquetes()) {
            conteo.merge(p.getDestino(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> entrada : conteo.entrySet()) {
            assertTrue(entrada.getValue() >= 2, "pocos paquetes para " + entrada.getKey());
        }
    }

    /** Mínimo del enunciado: prioridades y pesos variados; reglas de rango. */
    @Test
    void hayPrioridadesYPesosVariadosYDentroDelRango() {
        Set<Integer> prioridades = new HashSet<>();
        Set<Double> pesos = new HashSet<>();
        for (Paquete p : DatosPrueba.crearPaquetes()) {
            assertTrue(p.getPrioridad() >= 1 && p.getPrioridad() <= 5);
            assertTrue(p.getPeso() > 0);
            assertTrue(p.getTiempoEstimado() > 0);
            prioridades.add(p.getPrioridad());
            pesos.add(p.getPeso());
        }
        assertEquals(5, prioridades.size(), "deben aparecer las 5 prioridades");
        assertTrue(pesos.size() >= 20);
    }

    /** Los 6 primeros paquetes son los del ejemplo del enunciado, en el mismo orden. */
    @Test
    void conservaLosEjemplosDelEnunciadoAlInicio() {
        List<Paquete> paquetes = DatosPrueba.crearPaquetes();
        String[] codigos = {"PQ731", "PQ105", "PQ942", "PQ318", "PQ567", "PQ829"};
        String[] destinos = {"Armenia", "Salento", "Armenia", "Calarcá", "Salento", "Armenia"};
        for (int i = 0; i < codigos.length; i++) {
            assertEquals(codigos[i], paquetes.get(i).getCodigo());
            assertEquals(destinos[i], paquetes.get(i).getDestino());
        }
    }

    /** Mínimo del enunciado: 6 repartidores; regla: identificación única. */
    @Test
    void hayAlMenosSeisRepartidoresConIdentificacionUnica() {
        List<Repartidor> repartidores = DatosPrueba.crearRepartidores();
        assertTrue(repartidores.size() >= 6);

        Set<String> ids = new HashSet<>();
        for (Repartidor r : repartidores) {
            assertTrue(ids.add(r.getIdentificacion()));
        }
    }

    /** Debe haber al menos un repartidor no disponible para demostrar esa regla. */
    @Test
    void hayRepartidoresNoDisponiblesParaProbarLaRegla() {
        int noDisponibles = 0;
        for (Repartidor r : DatosPrueba.crearRepartidores()) {
            if (!r.isDisponible()) {
                noDisponibles++;
            }
        }
        assertTrue(noDisponibles >= 1);
    }

    /** Todo paquete se puede asignar: su destino tiene un repartidor disponible de esa zona. */
    @Test
    void cadaDestinoTieneUnRepartidorDisponibleDeEsaZona() {
        Set<String> zonasDisponibles = new HashSet<>();
        for (Repartidor r : DatosPrueba.crearRepartidores()) {
            if (r.isDisponible()) {
                zonasDisponibles.add(r.getZona());
            }
        }
        for (Paquete p : DatosPrueba.crearPaquetes()) {
            assertTrue(zonasDisponibles.contains(p.getDestino()),
                    "sin repartidor disponible para " + p.getDestino());
        }
    }

    /** Cada llamada crea objetos nuevos: modificar una lista no afecta la siguiente. */
    @Test
    void cadaLlamadaDevuelveObjetosNuevos() {
        List<Paquete> primera = DatosPrueba.crearPaquetes();
        List<Paquete> segunda = DatosPrueba.crearPaquetes();
        assertNotSame(primera, segunda);
        assertNotSame(primera.get(0), segunda.get(0));

        primera.clear();
        assertEquals(32, DatosPrueba.crearPaquetes().size());
    }

    /** El centro queda con los 32 paquetes, en el mismo orden de llegada. */
    @Test
    void elCentroSeCargaCompletoYEnOrdenDeLlegada() {
        CentroDistribucion centro = DatosPrueba.crearCentroDistribucion();
        List<Paquete> esperados = DatosPrueba.crearPaquetes();

        assertEquals(esperados.size(), centro.cantidadPaquetes());
        List<Paquete> registrados = centro.obtenerPaquetesRegistrados();
        for (int i = 0; i < esperados.size(); i++) {
            assertEquals(esperados.get(i).getCodigo(), registrados.get(i).getCodigo());
        }
        assertNotNull(centro.buscarPorCodigo("PQ731"));
    }

    /** El gestor queda con los 8 repartidores y respeta la disponibilidad inicial. */
    @Test
    void elGestorDeRepartidoresSeCargaCompleto() {
        GestorRepartidores gestor = DatosPrueba.crearGestorRepartidores();
        assertEquals(8, gestor.cantidadRepartidores());
        assertFalse(gestor.buscarPorIdentificacion("R002").isDisponible());
    }

    /** El despacho usa los mismos objetos del centro y aplica FIFO y prioridad. */
    @Test
    void elGestorDeDespachoUsaLosMismosObjetosDelCentro() {
        CentroDistribucion centro = DatosPrueba.crearCentroDistribucion();
        GestorDespacho despacho = DatosPrueba.crearGestorDespacho(centro);

        assertEquals(32, despacho.cantidadPendientesFIFO());
        assertEquals(32, despacho.cantidadPendientesPrioritarios());

        // FIFO: el primero en salir es el primero que llegó, y es el mismo objeto.
        assertSame(centro.buscarPorCodigo("PQ731"), despacho.despacharFIFO());

        // Prioridad: primero PQ148 (5, 14 min). Luego PQ105 y PQ271 empatan
        // en (5, 25) y gana PQ105 por código.
        assertEquals("PQ148", despacho.despacharPrioritario().getCodigo());
        assertEquals("PQ105", despacho.despacharPrioritario().getCodigo());
        assertEquals("PQ271", despacho.despacharPrioritario().getCodigo());
    }

    /** Los escenarios pequeños tienen el tamaño esperado y el de búsqueda está ordenado. */
    @Test
    void losEscenariosDelEnunciadoTienenLosValoresEsperados() {
        assertEquals(4, DatosPrueba.escenarioPesoPorMunicipio().size());
        assertEquals(7, DatosPrueba.escenarioPrioridades().size());
        assertEquals(6, DatosPrueba.escenarioDivideYVenceras().size());
        assertEquals(4, DatosPrueba.escenarioDespacho().size());

        List<Paquete> ordenados = DatosPrueba.escenarioBusquedaBinaria();
        assertEquals(8, ordenados.size());
        for (int i = 1; i < ordenados.size(); i++) {
            assertTrue(ordenados.get(i - 1).compareTo(ordenados.get(i)) < 0,
                    "el escenario de búsqueda binaria debe estar ordenado");
        }
    }
}
