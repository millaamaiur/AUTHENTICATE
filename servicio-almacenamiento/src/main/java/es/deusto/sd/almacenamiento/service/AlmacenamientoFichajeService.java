package es.deusto.sd.almacenamiento.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import es.deusto.sd.almacenamiento.access.repository.FichajeRepository;
import es.deusto.sd.almacenamiento.entity.Fichaje;

@Service
public class AlmacenamientoFichajeService {

    private final FichajeRepository fichajeRepository;

    public AlmacenamientoFichajeService(FichajeRepository fichajeRepository) {
        this.fichajeRepository = fichajeRepository;
    }

    public Fichaje guardarFichaje(Fichaje fichaje) {
        return fichajeRepository.save(fichaje);
    }

    public List<Fichaje> obtenerFichajesDeTrabajador(String idTrabajador) {
        return fichajeRepository.findByIdTrabajador(idTrabajador);
    }
    
    public List<Fichaje> obtenerFichajesEntre(LocalDateTime inicio, LocalDateTime fin) {
        return fichajeRepository.findByTimestampBetween(inicio, fin);
    }
}