package quindioexpress.servicio;

import org.junit.jupiter.api.Test;
import quindioexpress.excepciones.DatoInvalidoException;
import quindioexpress.modelo.Paquete;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReporteMunicipiosTest {

    private Paquete paquete(String codigo, String destino) {
        return new Paquete(codigo, destino, 2.5, 3, 20);
    }

    private CentroDistribucion centroConPaquetes() {
        CentroDistribucion centro = new CentroDistribucion();
        centro.registrarPaquete(paquete("PQ731", "Armenia"));
        centro.registrarPaquete(paquete("PQ105", "Salento"));
        centro.registrarPaquete(paquete("PQ942", "Armenia"));
        centro.registrarPaquete(paquete("PQ318", "Calarcá"));
        centro.registrarPaquete(paquete("PQ567", "Salento"));
        centro.registrarPaquete(paquete("PQ829", "Armenia"));
        return centro;
    }

    @Test
    void rechazaUnCentroNulo() {
        assertThrows(DatoInvalidoException.class, () -> new ReporteMunicipios(null));
    }

    @Test
    void devuelveConjuntoVacioCuandoNoHayPaquetes() {
        ReporteMunicipios reporte = new ReporteMunicipios(new CentroDistribucion());

        assertTrue(reporte.obtenerMunicipiosUnicos().isEmpty());
        assertTrue(reporte.obtenerMunicipiosOrdenados().isEmpty());
        assertTrue(reporte.agruparPaquetesPorMunicipio().isEmpty());
    }

    @Test
    void obtieneMunicipiosSinRepetirLosDestinos() {
        ReporteMunicipios reporte = new ReporteMunicipios(centroConPaquetes());

        assertEquals(Set.of("Armenia", "Salento", "Calarcá"),
                reporte.obtenerMunicipiosUnicos());
        assertEquals(3, reporte.obtenerMunicipiosUnicos().size());
    }

    @Test
    void obtieneMunicipiosEnOrdenAlfabeticoSinRepeticiones() {
        ReporteMunicipios reporte = new ReporteMunicipios(centroConPaquetes());

        assertEquals(List.of("Armenia", "Calarcá", "Salento"),
                new ArrayList<>(reporte.obtenerMunicipiosOrdenados()));
    }

    @Test
    void agrupaTodosLosPaquetesPorDestino() {
        ReporteMunicipios reporte = new ReporteMunicipios(centroConPaquetes());

        Map<String, List<Paquete>> agrupados = reporte.agruparPaquetesPorMunicipio();

        assertEquals(Set.of("Armenia", "Salento", "Calarcá"), agrupados.keySet());
        assertEquals(3, agrupados.size());
        assertEquals(List.of("PQ731", "PQ942", "PQ829"), codigos(agrupados.get("Armenia")));
        assertEquals(List.of("PQ105", "PQ567"), codigos(agrupados.get("Salento")));
        assertEquals(List.of("PQ318"), codigos(agrupados.get("Calarcá")));
    }

    @Test
    void conservaElOrdenDeRegistroDentroDeCadaGrupo() {
        CentroDistribucion centro = new CentroDistribucion();
        centro.registrarPaquete(paquete("PQ900", "Armenia"));
        centro.registrarPaquete(paquete("PQ100", "Armenia"));
        centro.registrarPaquete(paquete("PQ500", "Armenia"));

        ReporteMunicipios reporte = new ReporteMunicipios(centro);

        assertEquals(List.of("PQ900", "PQ100", "PQ500"),
                codigos(reporte.agruparPaquetesPorMunicipio().get("Armenia")));
    }

    @Test
    void conservaElOrdenDePrimeraAparicionDeLosMunicipiosEnElAgrupamiento() {
        ReporteMunicipios reporte = new ReporteMunicipios(centroConPaquetes());

        assertEquals(List.of("Armenia", "Salento", "Calarcá"),
                new ArrayList<>(reporte.agruparPaquetesPorMunicipio().keySet()));
    }

    @Test
    void municipiosUnicosNoPermiteModificarElResultado() {
        ReporteMunicipios reporte = new ReporteMunicipios(centroConPaquetes());

        assertThrows(UnsupportedOperationException.class,
                () -> reporte.obtenerMunicipiosUnicos().add("Pereira"));
    }

    @Test
    void municipiosOrdenadosNoPermiteModificarElResultado() {
        ReporteMunicipios reporte = new ReporteMunicipios(centroConPaquetes());

        assertThrows(UnsupportedOperationException.class,
                () -> reporte.obtenerMunicipiosOrdenados().add("Pereira"));
    }

    @Test
    void agrupamientoNoPermiteModificarElMapaNiSusListas() {
        ReporteMunicipios reporte = new ReporteMunicipios(centroConPaquetes());
        Map<String, List<Paquete>> agrupados = reporte.agruparPaquetesPorMunicipio();

        assertThrows(UnsupportedOperationException.class,
                () -> agrupados.put("Pereira", List.of()));
        assertThrows(UnsupportedOperationException.class,
                () -> agrupados.get("Armenia").add(paquete("PQ999", "Armenia")));
    }

    @Test
    void losReportesReflejanLosPaquetesRegistradosDespuesDeCrearElReporte() {
        CentroDistribucion centro = new CentroDistribucion();
        ReporteMunicipios reporte = new ReporteMunicipios(centro);

        centro.registrarPaquete(paquete("PQ001", "Armenia"));
        centro.registrarPaquete(paquete("PQ002", "Pereira"));

        assertEquals(Set.of("Armenia", "Pereira"), reporte.obtenerMunicipiosUnicos());
        assertEquals(2, reporte.agruparPaquetesPorMunicipio().size());
    }

    @Test
    void eliminaDelReporteLosMunicipiosQueYaNoTienenPaquetesRegistrados() {
        CentroDistribucion centro = new CentroDistribucion();
        centro.registrarPaquete(paquete("PQ001", "Armenia"));
        centro.registrarPaquete(paquete("PQ002", "Salento"));
        ReporteMunicipios reporte = new ReporteMunicipios(centro);

        centro.eliminarPaquete("PQ002");

        assertEquals(Set.of("Armenia"), reporte.obtenerMunicipiosUnicos());
        assertFalse(reporte.agruparPaquetesPorMunicipio().containsKey("Salento"));
    }

    private List<String> codigos(List<Paquete> paquetes) {
        return paquetes.stream().map(Paquete::getCodigo).toList();
    }
}
