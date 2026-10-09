package quindioexpress.modelo;

/**
 * representa a un repartidor del centro de distribucion.
 * guarda su identificacion, nombre, zona y disponibilidad.
 */
public class Repartidor {

    private String identificacion;
    private String nombre;
    private String zona;
    private boolean disponible;

    /**
     * crea un repartidor disponible.
     */
    public Repartidor(String identificacion, String nombre, String zona) {
        this(identificacion, nombre, zona, true);
    }

    /**
     * crea un repartidor con su estado de disponibilidad.
     */
    public Repartidor(String identificacion, String nombre, String zona, boolean disponible) {
        setIdentificacion(identificacion);
        setNombre(nombre);
        setZona(zona);
        this.disponible = disponible;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        if (identificacion == null || identificacion.trim().isEmpty()) {
            throw new IllegalArgumentException("la identificacion no puede estar vacia");
        }
        this.identificacion = identificacion.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("el nombre no puede estar vacio");
        }
        this.nombre = nombre.trim();
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        if (zona == null || zona.trim().isEmpty()) {
            throw new IllegalArgumentException("la zona no puede estar vacia");
        }
        this.zona = zona.trim();
    }

    public boolean isDisponible() {
        return disponible;
    }

    public boolean estaDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Repartidor{" +
                "identificacion='" + identificacion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", zona='" + zona + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}
