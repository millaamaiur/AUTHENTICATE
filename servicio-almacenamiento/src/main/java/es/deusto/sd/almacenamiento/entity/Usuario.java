package es.deusto.sd.almacenamiento.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {
    @Id
    private String idTrabajador;
    private String nombre;
    private String apellidos;
    private String dni;
    private String vectorBiometrico;
    private boolean activo;

    public Usuario() {}

    public Usuario(String idTrabajador, String nombre, String apellidos, String dni, String vectorBiometrico, boolean activo) {
        this.idTrabajador = idTrabajador;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.vectorBiometrico = vectorBiometrico;
        this.activo = activo;
    }

    public String getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(String idTrabajador) { this.idTrabajador = idTrabajador; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getVectorBiometrico() { return vectorBiometrico; }
    public void setVectorBiometrico(String vectorBiometrico) { this.vectorBiometrico = vectorBiometrico; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}