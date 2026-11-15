package es.deusto.sd.autentificacion.access;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class AccesoCapturaFacial {

    @Value("${edge.rostro.path}")
    private String rutaArchivo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Double> capturarVectorRostro() throws IOException {
        JsonNode root = objectMapper.readTree(new File(rutaArchivo));
        JsonNode arrayNode = root.get("vector");

        List<Double> vector = new ArrayList<>();
        if (arrayNode != null && arrayNode.isArray()) {
            for (JsonNode n : arrayNode) {
                vector.add(n.asDouble());
            }
        }
        return vector;
    }
}