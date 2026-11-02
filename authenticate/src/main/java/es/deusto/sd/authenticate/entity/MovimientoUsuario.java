package es.deusto.sd.authenticate.entity;

import java.time.LocalDateTime;

public class MovimientoUsuario {

    private String idTrabajador;
    private TipoMovimientoUsuario tipo;
    private LocalDateTime fecha;

    public MovimientoUsuario() {

    }

    public MovimientoUsuario(String idTrabajador, TipoMovimientoUsuario tipo, LocalDateTime fecha) {
        this.idTrabajador = idTrabajador;
        this.tipo = tipo;
        this.fecha = fecha;
    }

    public String getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(String idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public TipoMovimientoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimientoUsuario tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}