package es.deusto.sd.almacenamiento.access.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.sd.almacenamiento.entity.Fichaje;

public interface FichajeRepository extends JpaRepository<Fichaje, Long> {
    List<Fichaje> findByIdTrabajador(String idTrabajador);
    List<Fichaje> findByTimestampBetween(LocalDateTime inicio, LocalDateTime fin);
}