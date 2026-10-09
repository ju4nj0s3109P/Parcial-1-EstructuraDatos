package quindioexpress.modelo;

/**
 * representa un paquete que llega al centro de distribucion.
 * guarda sus datos y permite ordenarlo por codigo.
 */
public class Paquete implements Comparable<Paquete> {

    private String codigo;
    private String destino;
    private double peso;
    private int prioridad;
    private int tiempoEstimado;

    /**
     * crea un paquete con prioridad normal.
     */
    public Paquete(String codigo, String destino, double peso, int tiempoEstimado) {
        this(codigo, destino, peso, 5, tiempoEstimado);
    }

    /**
     * crea un paquete con todos sus datos.
     */
    public Paquete(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {
        setCodigo(codigo);
        setDestino(destino);
        setPeso(peso);
        setPrioridad(prioridad);
        setTiempoEstimado(tiempoEstimado);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("el codigo no puede estar vacio");
        }
        this.codigo = codigo.trim();
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        if (destino == null || destino.trim().isEmpty()) {
            throw new IllegalArgumentException("el destino no puede estar vacio");
        }
        this.destino = destino.trim();
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        if (!Double.isFinite(peso) || peso <= 0) {
            throw new IllegalArgumentException("el peso debe ser mayor que cero");
        }
        this.peso = peso;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("la prioridad debe estar entre 1 y 5");
        }
        this.prioridad = prioridad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(int tiempoEstimado) {
        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException("el tiempo estimado debe ser mayor que cero");
        }
        this.tiempoEstimado = tiempoEstimado;
    }

    @Override
    public int compareTo(Paquete otro) {
        if (otro == null) {
            throw new NullPointerException("no se puede comparar con un paquete nulo");
        }
        return this.codigo.compareTo(otro.codigo);
    }

    @Override
    public String toString() {
        return "Paquete{" +
                "codigo='" + codigo + '\'' +
                ", destino='" + destino + '\'' +
                ", peso=" + peso +
                ", prioridad=" + prioridad +
                ", tiempoEstimado=" + tiempoEstimado +
                '}';
    }
}
