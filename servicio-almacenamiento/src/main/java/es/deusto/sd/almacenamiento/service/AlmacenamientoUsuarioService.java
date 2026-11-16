package es.deusto.sd.almacenamiento.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.deusto.sd.almacenamiento.access.repository.UsuarioRepository;
import es.deusto.sd.almacenamiento.entity.Usuario;

@Service
public class AlmacenamientoUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public AlmacenamientoUsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(String idTrabajador) {
        Optional<Usuario> usuario = usuarioRepository.findById(idTrabajador);
        return usuario.orElse(null);
    }

    public void eliminarUsuario(String idTrabajador) {
        usuarioRepository.deleteById(idTrabajador);
    }
}