package es.deusto.sd.authenticate.access;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import es.deusto.sd.authenticate.entity.Fichaje;
import es.deusto.sd.authenticate.entity.MovimientoUsuario;
import es.deusto.sd.authenticate.entity.Usuario;

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

    public void eliminarUsuario(String idTrabajador) {
        storage.getUsuarios().remove(idTrabajador);
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

        for (Fichaje f : storage.getFichajes()) {

            boolean condId =
                    f.getIdTrabajador().equals(idTrabajador);

            boolean condDia =
                    dia.equals(f.getTimestamp().toLocalDate());

            if (condDia && condId) {
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

    public List<MovimientoUsuario> obtenerMovimientosUsuario(String idTrabajador) {

        List<MovimientoUsuario> resultado = new ArrayList<>();

        for (MovimientoUsuario movimiento : storage.getMovimientosUsuarios()) {
            if (movimiento.getIdTrabajador().equals(idTrabajador)) {
                resultado.add(movimiento);
            }
        }

        return resultado;
    }

    public void guardarMovimiento(MovimientoUsuario movimiento) {
        storage.getMovimientosUsuarios().add(movimiento);
    }

    public List<MovimientoUsuario> obtenerMovimientosEntre(
            LocalDate inicio,
            LocalDate fin) {

        List<MovimientoUsuario> resultado = new ArrayList<>();

        for (MovimientoUsuario movimiento : storage.getMovimientosUsuarios()) {

            LocalDate fecha =
                    movimiento.getFecha().toLocalDate();

            if (!fecha.isBefore(inicio) && !fecha.isAfter(fin)) {
                resultado.add(movimiento);
            }
        }

        return resultado;
    }
}