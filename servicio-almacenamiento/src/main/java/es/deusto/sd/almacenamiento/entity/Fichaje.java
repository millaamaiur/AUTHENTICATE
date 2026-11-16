package es.deusto.sd.almacenamiento.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
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

    public Fichaje() {}

    public Fichaje(String idTrabajador, String idEdificio, String idPuerta, LocalDateTime timestamp) {
        this.idTrabajador = idTrabajador;
        this.idEdificio = idEdificio;
        this.idPuerta = idPuerta;
        this.timestamp = timestamp;
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
}