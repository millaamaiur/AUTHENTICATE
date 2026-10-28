package es.deusto.sd.authenticate.entity;

import java.time.LocalDateTime;

public class Usuario {

    private String nombre;
    private String apellidos;
    private String dni;
    private String idTrabajador;
    private String vectorBiometrico;
    private LocalDateTime fecAlta;
    private LocalDateTime fecBaja;
    private boolean estado;

    public Usuario() {

    }

    public Usuario(String nombre, String apellidos, String dni, String idTrabajador, String vectorBiometrico,
            LocalDateTime fecAlta, boolean estado) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.idTrabajador = idTrabajador;
        this.vectorBiometrico = vectorBiometrico;
        this.fecAlta = fecAlta;
        this.estado = estado;
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

    public String getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(String idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getVectorBiometrico() {
        return vectorBiometrico;
    }

    public void setVectorBiometrico(String vectorBiometrico) {
        this.vectorBiometrico = vectorBiometrico;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}