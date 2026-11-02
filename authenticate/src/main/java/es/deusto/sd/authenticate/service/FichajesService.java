package es.deusto.sd.authenticate.service;

import es.deusto.sd.authenticate.access.AccesoDatosCentral;
import es.deusto.sd.authenticate.dto.FichajeDTO;
import es.deusto.sd.authenticate.entity.Fichaje;
import es.deusto.sd.authenticate.entity.MovimientoUsuario;
import es.deusto.sd.authenticate.entity.TipoFichaje;
import es.deusto.sd.authenticate.entity.TipoMovimientoUsuario;
import es.deusto.sd.authenticate.entity.Usuario;
import java.time.LocalDateTime;
import java.util.Comparator;
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
        LocalDateTime ultimaAlta = accesoDatos.obtenerMovimientosUsuario(fichaje.getIdTrabajador()).stream()
                .filter(m -> m.getTipo() == TipoMovimientoUsuario.ALTA)
                .map(MovimientoUsuario::getFecha)
                .max(Comparator.naturalOrder())
                .orElse(null);

        if (ultimaAlta != null && fichaje.getTimestamp().isBefore(ultimaAlta)) {
            return false;
        }

        // 1.º evento del día = entrada, 2.º = salida; no se admite un tercero
        int fichajesDelDia = accesoDatos.obtenerFichajes(
                fichaje.getIdTrabajador(),
                fichaje.getTimestamp().toLocalDate()).size();

        if (fichajesDelDia >= 2) {
            return false;
        }

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
