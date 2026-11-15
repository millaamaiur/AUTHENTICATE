package es.deusto.sd.autentificacion.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos leidos de la tarjeta nominativa de un trabajador")
public class TarjetaDTO {

    @Schema(description = "Identificador del trabajador en el Ayuntamiento", example = "TRA001")
    private String idTrabajador;

    @Schema(description = "Vector biometrico de referencia asociado al trabajador",
            example = "[0.15, -0.42, 0.88, 0.31, -0.09]")
    private List<Double> vectorReferencia;

    public TarjetaDTO() {

    }

    public TarjetaDTO(String idTrabajador, List<Double> vectorReferencia) {
        this.idTrabajador = idTrabajador;
        this.vectorReferencia = vectorReferencia;
    }

    public String getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(String idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public List<Double> getVectorReferencia() {
        return vectorReferencia;
    }

    public void setVectorReferencia(List<Double> vectorReferencia) {
        this.vectorReferencia = vectorReferencia;
    }
}
