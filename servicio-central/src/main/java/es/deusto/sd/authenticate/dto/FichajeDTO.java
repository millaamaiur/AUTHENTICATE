package es.deusto.sd.authenticate.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de un fichaje de un trabajador en una puerta de un edificio")
public class FichajeDTO {

    @Schema(description = "Identificador del trabajador", example = "TRA546")
    private String idTrabajador;

    @Schema(description = "Identificador del edificio", example = "ED5757")
    private String idEdificio;

    @Schema(description = "Identificador de la puerta", example = "PU3491")
    private String idPuerta;

    @Schema(description = "Fecha y hora del fichaje en formato ISO 8601, sin zona horaria", example = "2026-10-28T08:30:00")
    private LocalDateTime timestamp;

    public FichajeDTO() {

    }

    public FichajeDTO(String idTrabajador, String idEdificio, String idPuerta, LocalDateTime timestamp) {
        this.idTrabajador = idTrabajador;
        this.idEdificio = idEdificio;
        this.idPuerta = idPuerta;
        this.timestamp = timestamp;
    }

    public String getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(String idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(String idEdificio) {
        this.idEdificio = idEdificio;
    }

    public String getIdPuerta() {
        return idPuerta;
    }

    public void setIdPuerta(String idPuerta) {
        this.idPuerta = idPuerta;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
