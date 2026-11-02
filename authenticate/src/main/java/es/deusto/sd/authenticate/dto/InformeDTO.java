package es.deusto.sd.authenticate.dto;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object que representa el informe generado entre dos fechas, con las altas, bajas y fichajes correctos de los trabajadores")
public class InformeDTO {

    @Schema(description = "Listado de identificadores de los trabajadores dados de alta en el periodo", example = "[\"TRA546\", \"TRA547\"]")
    private List<String> altas;

    @Schema(description = "Listado de identificadores de los trabajadores dados de baja en el periodo", example = "[\"TRA112\"]")
    private List<String> bajas;

    @Schema(description = "Mapa que asocia el identificador de cada trabajador con su número de fichajes correctos (entrada y salida con un tiempo entre ambas mayor o igual a 8 horas)", example = "{\"TRA546\": 18, \"TRA547\": 20}")
    private Map<String, Integer> fichajesCorrectosPorTrabajador;

    public InformeDTO(List<String> altas, List<String> bajas, Map<String, Integer> fichajesCorrectosPorTrabajador) {
        this.altas = altas;
        this.bajas = bajas;
        this.fichajesCorrectosPorTrabajador = fichajesCorrectosPorTrabajador;
    }

    // Getters y Setters
    public List<String> getAltas() { return altas; }
    public void setAltas(List<String> altas) { this.altas = altas; }
    public List<String> getBajas() { return bajas; }
    public void setBajas(List<String> bajas) { this.bajas = bajas; }
    public Map<String, Integer> getFichajesCorrectosPorTrabajador() { return fichajesCorrectosPorTrabajador; }
    public void setFichajesCorrectosPorTrabajador(Map<String, Integer> fichajesCorrectosPorTrabajador) { this.fichajesCorrectosPorTrabajador = fichajesCorrectosPorTrabajador; }
}