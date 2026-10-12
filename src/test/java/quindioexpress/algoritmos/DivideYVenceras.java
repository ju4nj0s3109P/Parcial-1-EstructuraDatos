package quindioexpress.algoritmos;

import org.junit.jupiter.api.Test;
import quindioexpress.datos.DatosPrueba;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de divide y vencerás
 */
class DivideYVencerasTest {

    /** Margen para comparar double (las sumas de decimales no son exactas). */
    private static final double TOLERANCIA = 1e-9;

    /** Crea una lista de paquetes a partir de pesos (códigos PQ001, PQ002...). */
    private List<Paquete> conPesos(double... pesos) {
        List<Paquete> paquetes = new ArrayList<>();
        for (int i = 0; i < pesos.length; i++) {
            paquetes.add(new Paquete(String.format("PQ%03d", i + 1), "Armenia", pesos[i], 3, 10));
        }
        return paquetes;
    }

    // ------------------------------------------------------- paquete más pesado

    /** Ejemplo del enunciado: pesos 4.5, 12.0, 3.2, 18.7, 7.4, 5.1 → 18.7 (PQ318). */
    @Test
    void reproduceElEjemploDelEnunciado() {
        Paquete resultado = DivideYVenceras.paqueteMasPesado(DatosPrueba.escenarioDivideYVenceras());
        assertEquals(18.7, resultado.getPeso(), TOLERANCIA);
        assertEquals("PQ318", resultado.getCodigo());
    }

    /** Un solo paquete: la primera llamada ya es el caso base. */
    @Test
    void conUnSoloPaqueteDevuelveEseMismo() {
        List<Paquete> uno = conPesos(3.3);
        assertSame(uno.get(0), DivideYVenceras.paqueteMasPesado(uno));
    }

    /** Dos paquetes: una división en dos casos base y una combinación. */
    @Test
    void conDosPaquetes() {
        assertEquals(9.0, DivideYVenceras.paqueteMasPesado(conPesos(2.0, 9.0)).getPeso(), TOLERANCIA);
        assertEquals(9.0, DivideYVenceras.paqueteMasPesado(conPesos(9.0, 2.0)).getPeso(), TOLERANCIA);
    }

    /** Máximo al inicio, al final, en la mitad izquierda y en la derecha. */
    @Test
    void encuentraElMaximoEnCualquierPosicion() {
        assertEquals("PQ001", DivideYVenceras.paqueteMasPesado(conPesos(50, 1, 2, 3, 4)).getCodigo());
        assertEquals("PQ005", DivideYVenceras.paqueteMasPesado(conPesos(1, 2, 3, 4, 50)).getCodigo());
        assertEquals("PQ002", DivideYVenceras.paqueteMasPesado(conPesos(1, 50, 2, 3, 4, 5)).getCodigo());
        assertEquals("PQ005", DivideYVenceras.paqueteMasPesado(conPesos(1, 2, 3, 4, 50, 5)).getCodigo());
    }

    /** Con tamaño par las mitades quedan iguales; con impar, la izquierda tiene uno más. */
    @Test
    void funcionaConTamanoParEImpar() {
        assertEquals(7.0, DivideYVenceras.paqueteMasPesado(conPesos(1, 7, 3, 5)).getPeso(), TOLERANCIA);
        assertEquals(7.0, DivideYVenceras.paqueteMasPesado(conPesos(1, 7, 3, 5, 2)).getPeso(), TOLERANCIA);
    }

    /** Si dos paquetes tienen el peso máximo, la combinación favorece al izquierdo. */
    @Test
    void enEmpateDevuelveElQueAparecePrimero() {
        assertEquals("PQ002", DivideYVenceras.paqueteMasPesado(conPesos(1, 8, 3, 8)).getCodigo());
    }

    /** Lista vacía: no hay paquete más pesado. */
    @Test
    void listaVaciaDevuelveNull() {
        assertNull(DivideYVenceras.paqueteMasPesado(new ArrayList<>()));
    }

    /** Lista nula lanza DatoInvalidoException. */
    @Test
    void listaNulaLanzaExcepcion() {
        assertThrows(DatoInvalidoException.class, () -> DivideYVenceras.paqueteMasPesado(null));
    }

    /** Sobre los 32 paquetes coincide con un recorrido lineal clásico (PQ318, 18.7 kg). */
    @Test
    void encuentraElMasPesadoDeLaJornadaCompleta() {
        List<Paquete> jornada = DatosPrueba.crearPaquetes();

        Paquete esperado = jornada.get(0);
        for (Paquete p : jornada) {
            if (p.getPeso() > esperado.getPeso()) {
                esperado = p;
            }
        }

        assertSame(esperado, DivideYVenceras.paqueteMasPesado(jornada));
        assertEquals("PQ318", esperado.getCodigo());
    }

    /** El algoritmo trabaja con índices: no reordena ni modifica la lista. */
    @Test
    void noModificaLaLista() {
        List<Paquete> paquetes = DatosPrueba.escenarioDivideYVenceras();
        DivideYVenceras.paqueteMasPesado(paquetes);
        assertEquals("PQ105", paquetes.get(0).getCodigo());
        assertEquals(6, paquetes.size());
    }

    /** 4.5 + 12.0 + 3.2 + 18.7 + 7.4 + 5.1 = 50.9 */
    @Test
    void pesoTotalDelEjemplo() {
        assertEquals(50.9, DivideYVenceras.pesoTotal(DatosPrueba.escenarioDivideYVenceras()), TOLERANCIA);
    }

    /** Lista vacía → 0.0; un paquete → su peso; lista nula → excepción. */
    @Test
    void pesoTotalCasosLimite() {
        assertEquals(0.0, DivideYVenceras.pesoTotal(new ArrayList<>()), TOLERANCIA);
        assertEquals(3.3, DivideYVenceras.pesoTotal(conPesos(3.3)), TOLERANCIA);
        assertThrows(DatoInvalidoException.class, () -> DivideYVenceras.pesoTotal(null));
    }

    /**
     * Relaciona las dos estrategias: la suma por divide y vencerás debe ser
     * igual a la suma de los pesos de cada municipio calculados con la
     * recursión lineal.
     */
    @Test
    void pesoTotalCoincideConLaSumaDeLosMunicipios() {
        List<Paquete> jornada = DatosPrueba.crearPaquetes();
        String[] municipios = {"Armenia", "Salento", "Calarcá", "Montenegro", "Quimbaya", "Circasia"};

        double sumaPorMunicipios = 0.0;
        for (String m : municipios) {
            sumaPorMunicipios += Recursividad.pesoTotalPorMunicipio(jornada, m);
        }

        assertEquals(sumaPorMunicipios, DivideYVenceras.pesoTotal(jornada), TOLERANCIA);
    }
}
