package es.deusto.sd.authenticate.access;

import java.util.Optional;

import org.springframework.stereotype.Repository;

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
            usuario
        );
    }

    public Optional<Usuario> obtenerUsuario(String idTrabajador) {
    return Optional.ofNullable(
        storage.getUsuarios().get(idTrabajador)
    );
}
}