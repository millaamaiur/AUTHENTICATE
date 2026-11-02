package es.deusto.sd.authenticate.service;

import es.deusto.sd.authenticate.access.AccesoDatosCentral;
import es.deusto.sd.authenticate.dto.FichajeDTO;
import es.deusto.sd.authenticate.entity.Fichaje;
import es.deusto.sd.authenticate.entity.Usuario;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class FichajesService {
    private final AccesoDatosCentral accesoDatos;

    public FichajesService(AccesoDatosCentral accesoDatos) {
        this.accesoDatos = accesoDatos;
    }

    public boolean registrarFichaje(FichajeDTO fichaje) {

        Optional<Usuario> usuarioOpt = accesoDatos.obtenerUsuario(fichaje.getIdTrabajador());

        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Fichaje nuevoFichaje = new Fichaje(
                fichaje.getIdTrabajador(),
                fichaje.getIdEdificio(),
                fichaje.getIdPuerta(),
                fichaje.getTimestamp());

        accesoDatos.guardarFichaje(nuevoFichaje);

        return true;
    }
}
