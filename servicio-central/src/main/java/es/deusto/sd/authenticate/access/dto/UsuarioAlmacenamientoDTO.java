package es.deusto.sd.authenticate.access.dto;

import java.time.LocalDateTime;

/**
 * Usuario tal y como lo intercambia el servicio de almacenamiento por REST.
 * Mismos campos que el UsuarioDTO del servicio de almacenamiento.
 */
public class UsuarioAlmacenamientoDTO {

    private String idTrabajador;
    private String nombre;
    private String apellidos;
    private String dni;
    private String vectorBiometrico;
    private boolean estado;
    private LocalDateTime fecAlta;
    private LocalDateTime fecBaja;

    public UsuarioAlmacenamientoDTO() {
    }

    public UsuarioAlmacenamientoDTO(String idTrabajador, String nombre, String apellidos, String dni,
            String vectorBiometrico, boolean estado, LocalDateTime fecAlta, LocalDateTime fecBaja) {
        this.idTrabajador = idTrabajador;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.vectorBiometrico = vectorBiometrico;
        this.estado = estado;
        this.fecAlta = fecAlta;
        this.fecBaja = fecBaja;
    }

    public String getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(String idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getVectorBiometrico() {
        return vectorBiometrico;
    }

    public void setVectorBiometrico(String vectorBiometrico) {
        this.vectorBiometrico = vectorBiometrico;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecAlta() {
        return fecAlta;
    }

    public void setFecAlta(LocalDateTime fecAlta) {
        this.fecAlta = fecAlta;
    }

    public LocalDateTime getFecBaja() {
        return fecBaja;
    }

    public void setFecBaja(LocalDateTime fecBaja) {
        this.fecBaja = fecBaja;
    }
}
