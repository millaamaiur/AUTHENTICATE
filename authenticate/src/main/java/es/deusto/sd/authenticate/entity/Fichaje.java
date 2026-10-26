package es.deusto.sd.authenticate.entity;

import java.time.LocalDateTime;

public class Fichaje {

    private String idTrabajador; // TRA 546
    private String idEdificio; // ED 5757
    private String idPuerta; // PU 3491
    private LocalDateTime timestamp;

    public Fichaje() {

    }

    public Fichaje(String idTrabajador, String idEdificio, String idPuerta, LocalDateTime timestamp) {
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