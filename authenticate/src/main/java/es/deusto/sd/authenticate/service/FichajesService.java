package es.deusto.sd.authenticate.service;
import es.deusto.sd.authenticate.access.AccesoDatosCentral;
import es.deusto.sd.authenticate.dto.FichajeDTO;
import es.deusto.sd.authenticate.entity.Fichaje;
import es.deusto.sd.authenticate.entity.Usuario;
import java.time.LocalDateTime;
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

        Usuario usuario = usuarioOpt.get();

        LocalDateTime timestamp = fichaje.getTimestamp();

        // Fecha de alta del usuario: no puede fichar antes de darse de alta
        LocalDateTime fecAlta = usuario.getFecAlta();
        if (fecAlta != null && timestamp.isBefore(fecAlta)) {
            return false;
        }

        // Tampoco puede fichar después de su fecha de baja
        LocalDateTime fecBaja = usuario.getFecBaja();
        if (fecBaja != null && timestamp.isAfter(fecBaja)) {
            return false;
        }

        // Y debe estar activo
        if (!usuario.isEstado()) {
            return false;
        }

        Fichaje nuevoFichaje = new Fichaje(
                fichaje.getIdTrabajador(),
                fichaje.getIdEdificio(),
                fichaje.getIdPuerta(),
                timestamp);

        accesoDatos.guardarFichaje(nuevoFichaje);

        return true;
    }
}
