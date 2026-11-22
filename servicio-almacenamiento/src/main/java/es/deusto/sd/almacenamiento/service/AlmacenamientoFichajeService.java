package es.deusto.sd.almacenamiento.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import es.deusto.sd.almacenamiento.access.repository.FichajeRepository;
import es.deusto.sd.almacenamiento.dto.FichajeDTO;
import es.deusto.sd.almacenamiento.entity.Fichaje;

@Service
public class AlmacenamientoFichajeService {

    private final FichajeRepository fichajeRepository;

    public AlmacenamientoFichajeService(FichajeRepository fichajeRepository) {
        this.fichajeRepository = fichajeRepository;
    }

    // Entidad -> DTO
    private FichajeDTO toDTO(Fichaje f) {
        return new FichajeDTO(f.getIdTrabajador(), f.getIdEdificio(), f.getIdPuerta(),
                f.getTimestamp(), f.getTipo());
    }

    // DTO -> Entidad
    private Fichaje fromDTO(FichajeDTO dto) {
        return new Fichaje(dto.getIdTrabajador(), dto.getIdEdificio(), dto.getIdPuerta(),
                dto.getTimestamp(), dto.getTipo());
    }

    public FichajeDTO guardarFichaje(FichajeDTO dto) {
        Fichaje guardado = fichajeRepository.save(fromDTO(dto));
        return toDTO(guardado);
    }

    public List<FichajeDTO> obtenerFichajesDeTrabajador(String idTrabajador, LocalDate dia) {
    List<Fichaje> fichajes;
    if (dia == null) {
        fichajes = fichajeRepository.findByIdTrabajador(idTrabajador);
    } else {
        fichajes = fichajeRepository.findByIdTrabajadorAndTimestampBetween(
                idTrabajador, dia.atStartOfDay(), dia.atTime(23, 59, 59));
    }
    return fichajes.stream().map(this::toDTO).toList();
}

    public List<FichajeDTO> obtenerFichajesEntre(LocalDateTime inicio, LocalDateTime fin) {
        List<Fichaje> fichajes;
        fichajes = fichajeRepository.findByTimestampBetween(inicio, fin);
        return fichajes.stream().map(this::toDTO).toList();
    }
}