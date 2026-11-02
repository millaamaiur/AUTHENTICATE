package es.deusto.sd.authenticate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Data Transfer Object que representa a un trabajador del Ayuntamiento dado de alta en el sistema de fichaje")
public class UsuarioDTO {

    @Schema(description = "Nombre del trabajador", example = "Amaiur")
    private String nombre;

    @Schema(description = "Apellidos del trabajador", example = "Milla Etxeberria")
    private String apellidos;

    @Schema(description = "DNI del trabajador", example = "12345678A")
    private String dni;

    @Schema(description = "Identificador del trabajador", example = "TRA546")
    private String idTrabajador;

    @Schema(description = "Vector biométrico de referencia del trabajador, generado a partir de su alta en el sistema", example = "0.12,-0.45,0.78")
    private String vectorBiometrico;

    public UsuarioDTO() {

    }

    public UsuarioDTO(String nombre, String apellidos, String dni, String idTrabajador, String vectorBiometrico) {
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