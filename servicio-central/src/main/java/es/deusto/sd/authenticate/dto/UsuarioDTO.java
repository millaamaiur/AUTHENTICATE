package es.deusto.sd.authenticate.dto;

import java.time.LocalDateTime;

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

    @Schema(description = "Estado del trabajador: true si está de alta, false si está de baja. Solo lectura: lo asigna el sistema", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean estado;

    @Schema(description = "Fecha y hora del alta. Solo lectura: la asigna el sistema", example = "2026-11-26T11:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fecAlta;

    @Schema(description = "Fecha y hora de la baja (null si el trabajador está de alta). Solo lectura: la asigna el sistema", example = "2026-11-27T09:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime fecBaja;

    public UsuarioDTO() {

    }

    public UsuarioDTO(String nombre, String apellidos, String dni, String idTrabajador, String vectorBiometrico) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.idTrabajador = idTrabajador;
        this.vectorBiometrico = vectorBiometrico;
    }

    public UsuarioDTO(String nombre, String apellidos, String dni, String idTrabajador, String vectorBiometrico,
            Boolean estado, LocalDateTime fecAlta, LocalDateTime fecBaja) {
        this(nombre, apellidos, dni, idTrabajador, vectorBiometrico);
        this.estado = estado;
        this.fecAlta = fecAlta;
        this.fecBaja = fecBaja;
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

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
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
