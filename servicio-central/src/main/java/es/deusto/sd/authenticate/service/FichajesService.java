package es.deusto.sd.authenticate.service;

import es.deusto.sd.authenticate.access.AccesoDatosCentral;
import es.deusto.sd.authenticate.dto.FichajeDTO;
import es.deusto.sd.authenticate.entity.Fichaje;
import es.deusto.sd.authenticate.entity.Fichaje.TipoFichaje;
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

        // No puede fichar antes de su última ALTA
        Usuario usuario = usuarioOpt.get();

        if (!usuario.isEstado()) {
            return false;
        }

        LocalDateTime fechaAlta = usuario.getFecAlta();

        if (fechaAlta != null && fichaje.getTimestamp().isBefore(fechaAlta)) {
            return false;
        }

        //1.º evento del día = entrada; los siguientes = salida (el último del día es la salida)
        int fichajesDelDia = accesoDatos.obtenerFichajes(
                fichaje.getIdTrabajador(),
                fichaje.getTimestamp().toLocalDate()).size();

        TipoFichaje tipo = fichajesDelDia == 0 ? TipoFichaje.ENTRADA : TipoFichaje.SALIDA;

        Fichaje nuevoFichaje = new Fichaje(
                fichaje.getIdTrabajador(),
                fichaje.getIdEdificio(),
                fichaje.getIdPuerta(),
                fichaje.getTimestamp(),
                tipo);

        accesoDatos.guardarFichaje(nuevoFichaje);

        return true;
    }
}
