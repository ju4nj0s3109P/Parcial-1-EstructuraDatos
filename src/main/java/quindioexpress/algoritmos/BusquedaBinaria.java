package quindioexpress.algoritmos;

import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Búsqueda binaria de un paquete por su código sobre una lista ORDENADA
 */
public final class BusquedaBinaria {
    private BusquedaBinaria() {
    }

    /**
     * Devuelve una COPIA de la lista ordenada por código (orden natural)
     */
    public static List<Paquete> ordenarPorCodigo(List<Paquete> paquetes) {
        validarLista(paquetes);
        List<Paquete> copia = new ArrayList<>(paquetes);
        Collections.sort(copia);
        return copia;
    }

    /**
     * Busca la POSICIÓN de un código en la lista ordenada
     */
    public static int buscarPosicion(List<Paquete> ordenados, String codigo) {
        validarLista(ordenados);
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new DatoInvalidoException("el codigo no puede estar vacio");
        }
        return buscarRecursivo(ordenados, codigo.trim(), 0, ordenados.size() - 1);
    }

    /**
     * Busca el PAQUETE con el código indicado en la lista ordenada
     */
    public static Paquete buscar(List<Paquete> ordenados, String codigo) {
        int posicion = buscarPosicion(ordenados, codigo);
        return posicion == -1 ? null : ordenados.get(posicion);
    }

    /**
     * Método RECURSIVO de la búsqueda binaria sobre el rango [inicio, fin]
     */
    private static int buscarRecursivo(List<Paquete> ordenados, String codigo, int inicio, int fin) {
        // CASO BASE 1: rango vacío, el código no está.
        if (inicio > fin) {
            return -1;
        }

        int medio = inicio + (fin - inicio) / 2;
        int comparacion = codigo.compareTo(ordenados.get(medio).getCodigo());

        // CASO BASE 2: encontrado.
        if (comparacion == 0) {
            return medio;
        }

        // CASO RECURSIVO: descartar la mitad donde no puede estar.
        if (comparacion < 0) {
            return buscarRecursivo(ordenados, codigo, inicio, medio - 1);
        }
        return buscarRecursivo(ordenados, codigo, medio + 1, fin);
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
