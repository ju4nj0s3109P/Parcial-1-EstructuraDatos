package quindioexpress.algoritmos;

import quindioexpress.comparadores.ComparadorPesoDesc;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.Comparator;
import java.util.List;

/**
 * Análisis de paquetes con la estrategia DIVIDE Y VENCERÁS
 */
public final class DivideYVenceras {

    /**
     * Criterio reutilizado para la combinación: compare(a, b) &lt; 0 significa
     * que a pesa más que b. Se reutiliza el comparador de la carpeta
     * comparadores en lugar de repetir la lógica.
     */
    private static final Comparator<Paquete> POR_PESO_DESC = new ComparadorPesoDesc();

    /** Clase utilitaria: solo métodos estáticos, no se instancia. */
    private DivideYVenceras() {
    }

    /**
     * Devuelve el paquete de MAYOR PESO del conjunto
     */
    public static Paquete paqueteMasPesado(List<Paquete> paquetes) {
        validarLista(paquetes);
        if (paquetes.isEmpty()) {
            return null;
        }
        return masPesadoEntre(paquetes, 0, paquetes.size() - 1);
    }

    /**
     * Método RECURSIVO (divide y vencerás) que busca el más pesado en el
     * rango [inicio, fin]
     */
    private static Paquete masPesadoEntre(List<Paquete> paquetes, int inicio, int fin) {
        // CASO BASE: un solo elemento.
        if (inicio == fin) {
            return paquetes.get(inicio);
        }

        // DIVISIÓN
        int medio = inicio + (fin - inicio) / 2;

        // SUBPROBLEMA IZQUIERDO
        Paquete ganadorIzquierdo = masPesadoEntre(paquetes, inicio, medio);

        // SUBPROBLEMA DERECHO
        Paquete ganadorDerecho = masPesadoEntre(paquetes, medio + 1, fin);

        // COMBINACIÓN
        return combinarMasPesado(ganadorIzquierdo, ganadorDerecho);
    }

    /**
     * Etapa de COMBINACIÓN: de dos candidatos devuelve el más pesado
     */
    private static Paquete combinarMasPesado(Paquete izquierdo, Paquete derecho) {
        return POR_PESO_DESC.compare(izquierdo, derecho) <= 0 ? izquierdo : derecho;
    }

    /**
     * Calcula el peso total de todos los paquetes con divide y vencerás
     */
    public static double pesoTotal(List<Paquete> paquetes) {
        validarLista(paquetes);
        if (paquetes.isEmpty()) {
            return 0.0;
        }
        return sumarPesosEntre(paquetes, 0, paquetes.size() - 1);
    }

    /**
     * Método RECURSIVO (divide y vencerás) que suma los pesos de [inicio, fin]
     */
    private static double sumarPesosEntre(List<Paquete> paquetes, int inicio, int fin) {
        // CASO BASE
        if (inicio == fin) {
            return paquetes.get(inicio).getPeso();
        }

        // DIVISIÓN
        int medio = inicio + (fin - inicio) / 2;

        // SUBPROBLEMAS IZQUIERDO Y DERECHO
        double sumaIzquierda = sumarPesosEntre(paquetes, inicio, medio);
        double sumaDerecha = sumarPesosEntre(paquetes, medio + 1, fin);

        // COMBINACIÓN
        return sumaIzquierda + sumaDerecha;
    }

    /**
     * Verifica que la lista exista
     */
    private static void validarLista(List<Paquete> paquetes) {
        if (paquetes == null) {
            throw new DatoInvalidoException("la lista de paquetes no puede ser nula");
        }
    }
}
