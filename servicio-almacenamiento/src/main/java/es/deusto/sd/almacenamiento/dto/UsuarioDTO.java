package es.deusto.sd.almacenamiento.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de un usuario del sistema")
public class UsuarioDTO {

    @Schema(description = "Identificador de un trabajador", example = "TRA001")
    private String idTrabajador;
    @Schema(description = "Nombre de un trabajador", example = "Eric")
    private String nombre;
    @Schema(description = "Apellidos de un trabajador", example = "Ruiz")
    private String apellidos;
    @Schema(description = "Dni de un trabajador", example = "65221741L")
    private String dni;
    @Schema(description = "Vector biométrico de referencia del trabajador, generado a partir de su alta en el sistema", example = "0.12,-0.45,0.78")
    private String vectorBiometrico;
    @Schema(description = "Estado del usuario: true = de alta, false = de baja", example = "true")
    private boolean estado;
    @Schema(description = "Fecha de alta de un trabajador", example = "2026-10-05T08:00:00")
    private LocalDateTime fecAlta;
    @Schema(description = "Fecha de baja de un trabajador (null si sigue de alta)", example = "2026-18-05T08:00:00")
    private LocalDateTime fecBaja;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String idTrabajador, String nombre, String apellidos, String dni, String vectorBiometrico,
            boolean estado, LocalDateTime fecAlta, LocalDateTime fecBaja) {
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
