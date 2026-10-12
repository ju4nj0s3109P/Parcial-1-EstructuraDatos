package quindioexpress.algoritmos;

import org.junit.jupiter.api.Test;
import quindioexpress.datos.DatosPrueba;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de los métodos recursivos.
 */
class RecursividadTest {

    /** Margen para comparar double (las sumas de decimales no son exactas). */
    private static final double TOLERANCIA = 1e-9;

    /** Ejemplo del enunciado: Armenia = 4.5 + 7.2 + 2.3 = 14.0; Salento = 3.0. */
    @Test
    void pesoTotalReproduceElEjemploDelEnunciado() {
        List<Paquete> paquetes = DatosPrueba.escenarioPesoPorMunicipio();
        assertEquals(14.0, Recursividad.pesoTotalPorMunicipio(paquetes, "Armenia"), TOLERANCIA);
        assertEquals(3.0, Recursividad.pesoTotalPorMunicipio(paquetes, "Salento"), TOLERANCIA);
    }

    /** El municipio se compara sin importar mayúsculas ni espacios alrededor. */
    @Test
    void pesoTotalIgnoraMayusculasYEspacios() {
        List<Paquete> paquetes = DatosPrueba.escenarioPesoPorMunicipio();
        assertEquals(14.0, Recursividad.pesoTotalPorMunicipio(paquetes, "  ARMENIA "), TOLERANCIA);
    }

    /** Un municipio sin paquetes recorre toda la lista y suma 0. */
    @Test
    void pesoTotalDeMunicipioSinPaquetesEsCero() {
        List<Paquete> paquetes = DatosPrueba.escenarioPesoPorMunicipio();
        assertEquals(0.0, Recursividad.pesoTotalPorMunicipio(paquetes, "Filandia"), TOLERANCIA);
    }

    /** Lista vacía: la primera llamada ya cumple el caso base. */
    @Test
    void pesoTotalConListaVaciaEntraDirectoAlCasoBase() {
        assertEquals(0.0, Recursividad.pesoTotalPorMunicipio(new ArrayList<>(), "Armenia"), TOLERANCIA);
    }

    /** Un solo paquete: una llamada recursiva y luego el caso base. */
    @Test
    void pesoTotalConUnSoloPaquete() {
        List<Paquete> uno = List.of(new Paquete("PQ001", "Armenia", 2.5, 3, 10));
        assertEquals(2.5, Recursividad.pesoTotalPorMunicipio(uno, "Armenia"), TOLERANCIA);
        assertEquals(0.0, Recursividad.pesoTotalPorMunicipio(uno, "Salento"), TOLERANCIA);
    }

    /** Sobre los 32 paquetes, el resultado recursivo coincide con un for clásico. */
    @Test
    void pesoTotalCoincideConUnCalculoIterativoEnLaJornadaCompleta() {
        List<Paquete> jornada = DatosPrueba.crearPaquetes();
        String[] municipios = {"Armenia", "Salento", "Calarcá", "Montenegro", "Quimbaya", "Circasia"};

        for (String municipio : municipios) {
            double esperado = 0.0;
            for (Paquete p : jornada) {
                if (p.getDestino().equals(municipio)) {
                    esperado += p.getPeso();
                }
            }
            assertEquals(esperado, Recursividad.pesoTotalPorMunicipio(jornada, municipio), TOLERANCIA,
                    "peso total incorrecto para " + municipio);
        }
    }

    /** La recursión solo lee la lista: no la modifica. */
    @Test
    void pesoTotalNoModificaLaLista() {
        List<Paquete> paquetes = DatosPrueba.escenarioPesoPorMunicipio();
        Paquete primero = paquetes.get(0);
        Recursividad.pesoTotalPorMunicipio(paquetes, "Armenia");
        assertEquals(4, paquetes.size());
        assertSame(primero, paquetes.get(0));
    }

    /** Lista nula, municipio nulo o vacío lanzan DatoInvalidoException. */
    @Test
    void pesoTotalRechazaDatosInvalidos() {
        List<Paquete> paquetes = DatosPrueba.escenarioPesoPorMunicipio();
        assertThrows(DatoInvalidoException.class,
                () -> Recursividad.pesoTotalPorMunicipio(null, "Armenia"));
        assertThrows(DatoInvalidoException.class,
                () -> Recursividad.pesoTotalPorMunicipio(paquetes, null));
        assertThrows(DatoInvalidoException.class,
                () -> Recursividad.pesoTotalPorMunicipio(paquetes, "   "));
    }

    // ------------------------------------------------ conteo por prioridad mínima

    /** Ejemplo del enunciado: prioridades 5, 2, 4, 1, 5, 3, 4 con mínimo 4 → 4. */
    @Test
    void conteoReproduceElEjemploDelEnunciado() {
        assertEquals(4, Recursividad.contarConPrioridadMinima(DatosPrueba.escenarioPrioridades(), 4));
    }

    /** "Igual o superior": con mínimo 5 cuenta los dos paquetes con prioridad 5. */
    @Test
    void conteoIncluyeElValorMinimo() {
        assertEquals(2, Recursividad.contarConPrioridadMinima(DatosPrueba.escenarioPrioridades(), 5));
    }

    /** Con mínimo 1 todos cumplen. */
    @Test
    void conteoConMinimoBajoCuentaTodos() {
        assertEquals(7, Recursividad.contarConPrioridadMinima(DatosPrueba.escenarioPrioridades(), 1));
    }

    /** Con un mínimo mayor que todas las prioridades, nadie cumple. */
    @Test
    void conteoConMinimoMayorQueTodasEsCero() {
        assertEquals(0, Recursividad.contarConPrioridadMinima(DatosPrueba.escenarioPrioridades(), 6));
    }

    /** Lista vacía: caso base directo. */
    @Test
    void conteoConListaVaciaEsCero() {
        assertEquals(0, Recursividad.contarConPrioridadMinima(new ArrayList<>(), 3));
    }

    /** Sobre los 32 paquetes y cada mínimo de 1 a 5, coincide con un for clásico. */
    @Test
    void conteoCoincideConUnCalculoIterativoEnLaJornadaCompleta() {
        List<Paquete> jornada = DatosPrueba.crearPaquetes();
        for (int minimo = 1; minimo <= 5; minimo++) {
            int esperado = 0;
            for (Paquete p : jornada) {
                if (p.getPrioridad() >= minimo) {
                    esperado++;
                }
            }
            assertEquals(esperado, Recursividad.contarConPrioridadMinima(jornada, minimo));
        }
    }

    /** Lista nula lanza DatoInvalidoException. */
    @Test
    void conteoRechazaListaNula() {
        assertThrows(DatoInvalidoException.class,
                () -> Recursividad.contarConPrioridadMinima(null, 3));
    }
}
