package es.deusto.sd.almacenamiento.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Fichaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idTrabajador;
    private String idEdificio;
    private String idPuerta;
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private TipoFichaje tipo;

    public Fichaje() {}

    

    public Fichaje(String idTrabajador, String idEdificio, String idPuerta, LocalDateTime timestamp,
            TipoFichaje tipo) {
        this.idTrabajador = idTrabajador;
        this.idEdificio = idEdificio;
        this.idPuerta = idPuerta;
        this.timestamp = timestamp;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdTrabajador() { return idTrabajador; }
    public void setIdTrabajador(String idTrabajador) { this.idTrabajador = idTrabajador; }
    public String getIdEdificio() { return idEdificio; }
    public void setIdEdificio(String idEdificio) { this.idEdificio = idEdificio; }
    public String getIdPuerta() { return idPuerta; }
    public void setIdPuerta(String idPuerta) { this.idPuerta = idPuerta; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public enum TipoFichaje {ENTRADA, SALIDA}

    public TipoFichaje getTipo() {
        return tipo;
    }

    public void setTipo(TipoFichaje tipo) {
        this.tipo = tipo;
    }
}