package es.deusto.sd.authenticate.entity;

import java.time.LocalDateTime;

public class Fichaje {

    private String idTrabajador; // TRA 546
    private String idEdificio; // ED 5757
    private String idPuerta; // PU 3491
    private LocalDateTime timestamp;
    private TipoFichaje tipo; // 1.º del día = ENTRADA, 2.º = SALIDA

    public Fichaje() {

    }

    public Fichaje(String idTrabajador, String idEdificio, String idPuerta, LocalDateTime timestamp,
            TipoFichaje tipo) {
        this.idTrabajador = idTrabajador;
        this.idEdificio = idEdificio;
        this.idPuerta = idPuerta;
        this.timestamp = timestamp;
        this.tipo = tipo;
    }

    public TipoFichaje getTipo() {
        return tipo;
    }

    public void setTipo(TipoFichaje tipo) {
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
}