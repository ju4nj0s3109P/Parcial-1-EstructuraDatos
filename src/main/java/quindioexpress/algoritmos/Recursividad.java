package quindioexpress.algoritmos;

import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.List;

/**
 * Operaciones que el enunciado pide resolver con RECURSIVIDAD (sin for,
 * while ni do-while)
 */
public final class Recursividad {

    /** Clase utilitaria: solo métodos estáticos, no se instancia. */
    private Recursividad() {
    }

    /**
     * Calcula el peso total (kg) de los paquetes cuyo destino es el municipio
     * indicado. El nombre del municipio se compara sin importar mayúsculas
     */
    public static double pesoTotalPorMunicipio(List<Paquete> paquetes, String municipio) {
        validarLista(paquetes);
        if (municipio == null || municipio.trim().isEmpty()) {
            throw new DatoInvalidoException("el municipio no puede estar vacio");
        }
        return pesoTotalDesde(paquetes, municipio.trim(), 0);
    }

    /**
     * Método RECURSIVO: suma los pesos del municipio desde la posición
     * {@code indice} hasta el final de la lista
     */
    private static double pesoTotalDesde(List<Paquete> paquetes, String municipio, int indice) {
        // CASO BASE: no quedan paquetes por revisar.
        if (indice == paquetes.size()) {
            return 0.0;
        }

        // CASO RECURSIVO
        Paquete actual = paquetes.get(indice);
        double aporte = actual.getDestino().equalsIgnoreCase(municipio)
                ? actual.getPeso()
                : 0.0;

        // La llamada queda pendiente hasta que se resuelva el resto de la lista.
        double pesoDelResto = pesoTotalDesde(paquetes, municipio, indice + 1);
        return aporte + pesoDelResto;
    }

    /**
     * Cuenta cuántos paquetes tienen prioridad MAYOR O IGUAL a
     * {@code prioridadMinima}
     */
    public static int contarConPrioridadMinima(List<Paquete> paquetes, int prioridadMinima) {
        validarLista(paquetes);
        return contarDesde(paquetes, prioridadMinima, 0);
    }

    /**
     * Método RECURSIVO: cuenta los paquetes que cumplen la prioridad mínima
     * desde la posición {@code indice} hasta el final
     */
    private static int contarDesde(List<Paquete> paquetes, int prioridadMinima, int indice) {
        // CASO BASE: no quedan paquetes por revisar.
        if (indice == paquetes.size()) {
            return 0;
        }

        // CASO RECURSIVO
        int cuentaActual = paquetes.get(indice).getPrioridad() >= prioridadMinima ? 1 : 0;
        int cuentaDelResto = contarDesde(paquetes, prioridadMinima, indice + 1);
        return cuentaActual + cuentaDelResto;
    }

    /**
     * Verifica que la lista exista. Una lista vacía SÍ es válida: la recursión
     * entra directo al caso base y devuelve 0
     */
    private static void validarLista(List<Paquete> paquetes) {
        if (paquetes == null) {
            throw new DatoInvalidoException("la lista de paquetes no puede ser nula");
        }
    }
}
