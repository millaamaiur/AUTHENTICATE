package es.deusto.sd.authenticate.entity;

public class Usuario {

    private String nombre;
    private String apellidos;
    private String dni;
    private String idTrabajador;
    private String vectorBiometrico;

    public Usuario() {

    }

    public Usuario(String nombre, String apellidos, String dni, String idTrabajador, String vectorBiometrico) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.idTrabajador = idTrabajador;
        this.vectorBiometrico = vectorBiometrico;
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
}