package es.deusto.sd.autentificacion.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class AccesoVerificacionFacial {

    @Value("${verificacion.host}")
    private String host;

    @Value("${verificacion.port}")
    private int puerto;

    public boolean verificar(List<Double> vectorReferencia, List<Double> vectorComprobacion) {

        if (!vectorValido(vectorReferencia) || !vectorValido(vectorComprobacion)) {
            return false;
        }

        try (
                Socket socket = new Socket(host, puerto);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {

            // (1) El servicio de autentificacion envia el primer vector
            out.println(convertirVector(vectorReferencia));

            // (2) El servicio de autentificacion envia el segundo vector
            out.println(convertirVector(vectorComprobacion));

            // (4) El servicio de verificacion devuelve el resultado
            String respuesta = in.readLine();

            return Boolean.parseBoolean(respuesta);

        } catch (IOException e) {
            System.err.println(" # AccesoVerificacionFacial: IO error: " + e.getMessage());
            return false;
        }
    }

    private boolean vectorValido(List<Double> vector) {
        return vector != null && !vector.isEmpty();
    }

    private String convertirVector(List<Double> vector) {

        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < vector.size(); i++) {

            if (i > 0) {
                resultado.append(",");
            }

            resultado.append(vector.get(i));
        }

        return resultado.toString();
    }
}
