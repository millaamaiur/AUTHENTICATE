package es.deusto.sd.almacenamiento.dto;

import java.time.LocalDateTime;

import es.deusto.sd.almacenamiento.entity.Fichaje.TipoFichaje;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Registro de un fichaje de entrada o salida de un trabajador")
public class FichajeDTO {

    @Schema(description = "Identificador del trabajador que ficha", example = "TRA001")
    private String idTrabajador;

    @Schema(description = "Identificador del edificio donde se ficha", example = "ED01")
    private String idEdificio;

    @Schema(description = "Identificador de la puerta donde se ficha", example = "PU01")
    private String idPuerta;

    @Schema(description = "Fecha y hora exactas del fichaje", example = "2026-10-05T08:00:00")
    private LocalDateTime timestamp;

    @Schema(description = "Tipo de fichaje: ENTRADA o SALIDA", example = "ENTRADA")
    private TipoFichaje tipo;

    public FichajeDTO() {
    }

    public FichajeDTO(String idTrabajador, String idEdificio, String idPuerta, LocalDateTime timestamp,
            TipoFichaje tipo) {
        this.idTrabajador = idTrabajador;
        this.idEdificio = idEdificio;
        this.idPuerta = idPuerta;
        this.timestamp = timestamp;
        this.tipo = tipo;
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

    public TipoFichaje getTipo() {
        return tipo;
    }

    public void setTipo(TipoFichaje tipo) {
        this.tipo = tipo;
    }
}