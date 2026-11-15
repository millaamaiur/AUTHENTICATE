package es.deusto.sd.autentificacion.access;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Repository
public class AccesoCapturaCara {

    @Value("${edge.rostro.path}")
    private String rutaArchivo;

    public List<Double> capturarVectorRostro() throws IOException {

        String contenido = Files.readString(Path.of(rutaArchivo), StandardCharsets.UTF_8);
        JsonObject objeto = JsonParser.parseString(contenido).getAsJsonObject();
        JsonArray array = objeto.getAsJsonArray("vector");

        List<Double> vector = new ArrayList<>();

        if (array != null) {
            for (JsonElement elemento : array) {
                vector.add(elemento.getAsDouble());
            }
        }

        return vector;
    }
}
