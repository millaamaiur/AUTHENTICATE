package es.deusto.sd.autentificacion.access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

class AccesoVerificacionFacialTest {

    @Test
    void enviaPrimeroLaReferenciaYDespuesLaCaptura() throws Exception {

        List<String> lineas = verificarConServidorDePrueba(
                "true",
                new double[] { 0.12, 0.45, 0.78 },
                new double[] { 0.11, 0.46, 0.79 });

        assertEquals(List.of("0.12,0.45,0.78", "0.11,0.46,0.79"), lineas);
    }

    @Test
    void devuelveElBooleanQueRespondeElServicio() throws Exception {

        assertEquals(List.of("1.0", "1.0"), verificarConServidorDePrueba(
                "true",
                new double[] { 1.0 },
                new double[] { 1.0 }));

        assertEquals(List.of("1.0", "1.0"), verificarConServidorDePrueba(
                "false",
                new double[] { 1.0 },
                new double[] { 1.0 }));
    }

    @Test
    void devuelveFalseSiFaltaAlgunVectorSinAbrirSocket() {

        AccesoVerificacionFacial acceso = new AccesoVerificacionFacial("127.0.0.1", 1);

        assertFalse(acceso.verificar(null, new double[] { 1.0 }));
        assertFalse(acceso.verificar(new double[] { 1.0 }, null));
        assertFalse(acceso.verificar(new double[0], new double[] { 1.0 }));
        assertFalse(acceso.verificar(new double[] { 1.0 }, new double[0]));
    }

    private List<String> verificarConServidorDePrueba(
            String respuesta,
            double[] vectorReferencia,
            double[] vectorCapturado) throws Exception {

        ExecutorService ejecutor = Executors.newSingleThreadExecutor();

        try (ServerSocket servidor = new ServerSocket(
                0, 50, InetAddress.getByName("127.0.0.1"))) {

            Future<List<String>> future = ejecutor.submit(() -> {
                try (Socket socket = servidor.accept();
                        BufferedReader entrada = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        PrintWriter salida = new PrintWriter(
                                socket.getOutputStream(), true)) {

                    List<String> lineas = List.of(
                            entrada.readLine(),
                            entrada.readLine());

                    salida.println(respuesta);

                    return lineas;
                }
            });

            AccesoVerificacionFacial acceso = new AccesoVerificacionFacial(
                    "127.0.0.1", servidor.getLocalPort());

            boolean resultado = acceso.verificar(vectorReferencia, vectorCapturado);

            assertEquals(Boolean.parseBoolean(respuesta), resultado);

            return future.get(5, TimeUnit.SECONDS);

        } finally {
            ejecutor.shutdownNow();
        }
    }
}
