package es.deusto.sd.authenticate.dto;

import java.util.List;
import java.util.Map;

public class InformeDTO {
    private List<String> altas;
    private List<String> bajas;
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