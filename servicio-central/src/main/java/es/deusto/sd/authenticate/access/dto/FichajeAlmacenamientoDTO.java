package es.deusto.sd.authenticate.access.dto;

import java.time.LocalDateTime;

import es.deusto.sd.authenticate.entity.Fichaje.TipoFichaje;

/**
 * Fichaje tal y como lo intercambia el servicio de almacenamiento por REST.
 * Mismos campos que el FichajeDTO del servicio de almacenamiento.
 */
public class FichajeAlmacenamientoDTO {

    private String idTrabajador;
    private String idEdificio;
    private String idPuerta;
    private LocalDateTime timestamp;
    private TipoFichaje tipo;

    public FichajeAlmacenamientoDTO() {
    }

    public FichajeAlmacenamientoDTO(String idTrabajador, String idEdificio, String idPuerta,
            LocalDateTime timestamp, TipoFichaje tipo) {
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
