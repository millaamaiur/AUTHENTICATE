package es.deusto.sd.authenticate.access;

import java.util.Optional;
import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.deusto.sd.authenticate.entity.Usuario;
import es.deusto.sd.authenticate.entity.Fichaje;

@Repository
public class AccesoDatosCentral {

    private final Storage storage;

    public AccesoDatosCentral(Storage storage) {
        this.storage = storage;
    }

    public void guardarUsuario(Usuario usuario) {
        storage.getUsuarios().put(
                usuario.getIdTrabajador(),
                usuario);
    }

    public Optional<Usuario> obtenerUsuario(String idTrabajador) {
        return Optional.ofNullable(
                storage.getUsuarios().get(idTrabajador));

    }

    public Optional<Map<String, Usuario>> obtenerUsuarios() {
        return Optional.ofNullable(
                storage.getUsuarios());
    }

    public void guardarFichaje(Fichaje fichaje) {
        storage.getFichajes().add(fichaje);
    }

    public List<Fichaje> obtenerFichajes(String idTrabajador, LocalDate dia) {
        List<Fichaje> resultado = new ArrayList<>();

        for(Fichaje f : storage.getFichajes()){
            boolean condId = f.getIdTrabajador().equals(idTrabajador);
            boolean condDia = dia.equals(f.getTimestamp().toLocalDate());

            if(condDia && condId){
                resultado.add(f);
            }
        }
        return resultado;
    }

    public List<Fichaje> obtenerFichajesEntre(LocalDate inicio, LocalDate fin) {
        List<Fichaje> resultado = new ArrayList<>();

        for (Fichaje f : storage.getFichajes()) {
            LocalDate dia = f.getTimestamp().toLocalDate();

            if (!dia.isBefore(inicio) && !dia.isAfter(fin)) {
                resultado.add(f);
            }
        }
        return resultado;
    }
}
