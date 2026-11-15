package es.deusto.sd.autentificacion.dto;

import java.util.List;

public class TarjetaDTO {
    private String idTrabajador;
    private List<Double> vectorReferencia;

    public TarjetaDTO() {}

    public String getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(String idTrabajador) { this.idTrabajador = idTrabajador; }

    public List<Double> getVectorReferencia() { return vectorReferencia; }
    public void setVectorReferencia(List<Double> vectorReferencia) { this.vectorReferencia = vectorReferencia; }
}