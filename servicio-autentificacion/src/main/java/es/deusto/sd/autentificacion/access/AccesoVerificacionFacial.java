package es.deusto.sd.autentificacion.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class AccesoVerificacionFacial {

    private static final int TIEMPO_ESPERA_MS = 5000;

    private final String host;
    private final int puerto;

    public AccesoVerificacionFacial(
            @Value("${verificacion.host}") String host,
            @Value("${verificacion.port}") int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    public boolean verificar(double[] vectorReferencia, double[] vectorComprobacion) {

        if (!vectorValido(vectorReferencia) || !vectorValido(vectorComprobacion)) {
            return false;
        }

        try (Socket socket = new Socket()) {

            socket.connect(new InetSocketAddress(host, puerto), TIEMPO_ESPERA_MS);
            socket.setSoTimeout(TIEMPO_ESPERA_MS);

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // (1) El servicio de autentificacion envia el primer vector
                out.println(convertirVector(vectorReferencia));

                // (2) El servicio de autentificacion envia el segundo vector
                out.println(convertirVector(vectorComprobacion));

                // (4) El servicio de verificacion devuelve el resultado
                String respuesta = in.readLine();

                return respuesta != null && Boolean.parseBoolean(respuesta.trim());
            }

        } catch (IOException e) {
            System.err.println(" # AccesoVerificacionFacial: IO error: " + e.getMessage());
            return false;
        }
    }

    private boolean vectorValido(double[] vector) {
        return vector != null && vector.length > 0;
    }

    private String convertirVector(double[] vector) {

        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < vector.length; i++) {

            if (i > 0) {
                resultado.append(",");
            }

            resultado.append(vector[i]);
        }

        return resultado.toString();
    }
}
