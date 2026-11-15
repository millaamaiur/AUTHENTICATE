package es.deusto.sd.autentificacion.dto;

import java.time.LocalDateTime;

public class FichajeCentralDTO {

    private String idTrabajador;
    private String idEdificio;
    private String idPuerta;
    private LocalDateTime timestamp;

    public FichajeCentralDTO() {}

    public FichajeCentralDTO(String idTrabajador, String idEdificio, String idPuerta, LocalDateTime timestamp) {
        this.idTrabajador = idTrabajador;
        this.idEdificio = idEdificio;
        this.idPuerta = idPuerta;
        this.timestamp = timestamp;
    }

    public String getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(String idTrabajador) { this.idTrabajador = idTrabajador; }

    public String getIdEdificio() { return idEdificio; }
    public void setIdEdificio(String idEdificio) { this.idEdificio = idEdificio; }

    public String getIdPuerta() { return idPuerta; }
    public void setIdPuerta(String idPuerta) { this.idPuerta = idPuerta; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}