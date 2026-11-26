package es.deusto.sd.autentificacion.access;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AccesoCapturaCaraTest {

    @TempDir
    Path directorioTemporal;

    @Test
    void leeElVectorBiometricoDelArchivoJson() throws Exception {

        Path archivo = directorioTemporal.resolve("captura.json");
        Files.writeString(archivo, "{\"vector\":[0.12,0.45,0.78]}");

        AccesoCapturaCara acceso = new AccesoCapturaCara(archivo.toString());

        assertArrayEquals(new double[] { 0.12, 0.45, 0.78 },
                acceso.capturarVectorRostro());
    }

    @Test
    void devuelveVectorVacioSiNoHayVector() throws Exception {

        Path archivo = directorioTemporal.resolve("captura.json");
        Files.writeString(archivo, "{}");

        AccesoCapturaCara acceso = new AccesoCapturaCara(archivo.toString());

        assertArrayEquals(new double[0], acceso.capturarVectorRostro());
    }
}
