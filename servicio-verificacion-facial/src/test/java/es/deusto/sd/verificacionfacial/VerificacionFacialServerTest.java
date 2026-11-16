package es.deusto.sd.verificacionfacial;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class VerificacionFacialServerTest {

    @Test
    @Timeout(30)
    void respondePorSocketYCierraConexionesIncompletas() throws Exception {
        //Comprobar que el puerto esta libre para no probar otro servidor
        try (ServerSocket comprobacion = new ServerSocket(9090)) {
            assertEquals(9090, comprobacion.getLocalPort());
        }

        //Arrancar el servidor real en otro proceso
        String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        String clases = Path.of(VerificacionFacialServer.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI()).toString();
        Process servidor = new ProcessBuilder(java, "-cp", clases,
                VerificacionFacialServer.class.getName())
                .redirectErrorStream(true)
                .redirectOutput(ProcessBuilder.Redirect.DISCARD)
                .start();

        try {
            esperarArranque(servidor);

            //Comprobar el protocolo y varias conexiones consecutivas
            assertEquals("true", consultar("0.12,0.45", "0.12,0.45"));
            assertEquals("true", consultar("0.12", "0.14"));
            assertEquals("false", consultar("0.12", "0.15"));
            assertEquals("false", consultar("0.12,0.45", "0.12"));
            assertEquals("false", consultar("hola", "0.12"));
            assertEquals("false", consultar("0.12,", "0.12"));
            assertEquals("false", consultar("", "0.12"));

            //Enviar solo un vector y comprobar que el servidor cierra por timeout
            try (Socket socket = new Socket("localhost", 9090);
                    PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader lector = new BufferedReader(new InputStreamReader(
                            socket.getInputStream(), StandardCharsets.UTF_8))) {
                socket.setSoTimeout(VerificacionFacialServer.TIEMPO_ESPERA_MS + 4000);
                long inicio = System.nanoTime();
                escritor.println("0.12");
                assertNull(lector.readLine(), "El timeout debe cerrar sin enviar un booleano");
                long transcurrido = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - inicio);
                assertTrue(transcurrido >= VerificacionFacialServer.TIEMPO_ESPERA_MS - 500,
                        "La conexion no debe cerrarse antes del tiempo de espera");
            }

            //Comprobar que el timeout no detiene el servidor
            assertTrue(servidor.isAlive());
            assertEquals("true", consultar("0.17", "0.17"));
        } finally {
            //Detener siempre el servidor de prueba
            servidor.destroy();
            if (!servidor.waitFor(5, TimeUnit.SECONDS)) {
                servidor.destroyForcibly();
                assertTrue(servidor.waitFor(5, TimeUnit.SECONDS));
            }
        }
    }

    private void esperarArranque(Process servidor) throws Exception {
        long limite = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (System.nanoTime() < limite) {
            assertTrue(servidor.isAlive(), "El servidor no ha podido arrancar");
            try (Socket socket = new Socket("localhost", 9090)) {
                return;
            } catch (java.io.IOException e) {
                Thread.sleep(50);
            }
        }
        throw new AssertionError("El servidor no escucha en el puerto 9090");
    }

    private String consultar(String referencia, String capturado) throws Exception {
        try (Socket socket = new Socket("localhost", 9090);
                PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader lector = new BufferedReader(new InputStreamReader(
                        socket.getInputStream(), StandardCharsets.UTF_8))) {
            socket.setSoTimeout(3000);
            escritor.println(referencia);
            escritor.println(capturado);
            String resultado = lector.readLine();
            assertNull(lector.readLine(), "El servidor debe cerrar al terminar la respuesta");
            return resultado;
        }
    }
}
