package es.deusto.sd.authenticate.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.sd.authenticate.access.AccesoDatosCentral;
import es.deusto.sd.authenticate.dto.UsuarioDTO;
import es.deusto.sd.authenticate.entity.Usuario;

@Service
public class UsuariosService {

    private final AccesoDatosCentral accesoDatos;

    public UsuariosService(AccesoDatosCentral accesoDatos) {
        this.accesoDatos = accesoDatos;
    }

    public boolean altaUsuario(UsuarioDTO usuario) {

        Optional<Usuario> usuarioExistente = accesoDatos.obtenerUsuario(usuario.getIdTrabajador());

        if (usuarioExistente.isPresent()) {
            return false;
        }

        Usuario nuevoUsuario = new Usuario(
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getDni(),
                usuario.getIdTrabajador(),
                usuario.getVectorBiometrico(),
                LocalDateTime.now(),
                true)
                ;

        accesoDatos.guardarUsuario(nuevoUsuario);

        return true;
    }

    public boolean bajaUsuario(String idTrabajador) {

        Optional<Usuario> usuarioExis = accesoDatos.obtenerUsuario(idTrabajador);

        if (usuarioExis.isPresent()) {

            Usuario u = usuarioExis.get();
            u.setEstado(false);
            u.setFecBaja(LocalDateTime.now());

            accesoDatos.guardarUsuario(u);

            return true;
        }

        return false;
    }

    public UsuarioDTO consultarUsuario(String idTrabajador) {

        Optional<Usuario> usuarioExis = accesoDatos.obtenerUsuario(idTrabajador);

        if (usuarioExis.isPresent()) {

            Usuario usuario = usuarioExis.get();

            UsuarioDTO result = new UsuarioDTO(
                    usuario.getNombre(),
                    usuario.getApellidos(),
                    usuario.getDni(),
                    usuario.getIdTrabajador(),
                    usuario.getVectorBiometrico());

            return result;
        }

        return null;
    }

}