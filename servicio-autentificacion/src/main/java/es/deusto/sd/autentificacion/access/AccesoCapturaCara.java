package es.deusto.sd.autentificacion.access;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Repository
public class AccesoCapturaCara {

    private final String rutaArchivo;

    public AccesoCapturaCara(@Value("${edge.rostro.path}") String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public double[] capturarVectorRostro() throws IOException {

        String contenido = Files.readString(Path.of(rutaArchivo), StandardCharsets.UTF_8);
        JsonObject objeto = JsonParser.parseString(contenido).getAsJsonObject();
        JsonArray array = objeto.getAsJsonArray("vector");

        if (array == null) {
            return new double[0];
        }

        double[] vector = new double[array.size()];

        for (int i = 0; i < array.size(); i++) {
            JsonElement elemento = array.get(i);
            vector[i] = elemento.getAsDouble();
        }

        return vector;
    }
}
