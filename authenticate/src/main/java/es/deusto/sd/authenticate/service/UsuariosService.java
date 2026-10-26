package es.deusto.sd.authenticate.service;

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
                usuario.getVectorBiometrico());

        accesoDatos.guardarUsuario(nuevoUsuario);

        return true;
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