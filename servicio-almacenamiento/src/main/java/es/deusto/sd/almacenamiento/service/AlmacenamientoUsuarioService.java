package es.deusto.sd.almacenamiento.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.deusto.sd.almacenamiento.access.repository.FichajeRepository;
import es.deusto.sd.almacenamiento.access.repository.UsuarioRepository;
import es.deusto.sd.almacenamiento.dto.UsuarioDTO;
import es.deusto.sd.almacenamiento.entity.Usuario;

@Service
public class AlmacenamientoUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final FichajeRepository fichajeRepository;

    public AlmacenamientoUsuarioService(UsuarioRepository usuarioRepository,
            FichajeRepository fichajeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.fichajeRepository = fichajeRepository;
    }

    // Entidad -> DTO
    private UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(u.getIdTrabajador(), u.getNombre(), u.getApellidos(), u.getDni(),
                u.getVectorBiometrico(), u.isEstado(), u.getFecAlta(), u.getFecBaja());
    }

    // DTO -> Entidad
    private Usuario fromDTO(UsuarioDTO dto) {
        return new Usuario(dto.getIdTrabajador(), dto.getNombre(), dto.getApellidos(), dto.getDni(),
                dto.getVectorBiometrico(), dto.isEstado(), dto.getFecAlta(), dto.getFecBaja());
    }

    // Crea el usuario, o lo actualiza si ya existe ese idTrabajador
    public UsuarioDTO guardarUsuario(UsuarioDTO dto) {
        Usuario guardado = usuarioRepository.save(fromDTO(dto));
        return toDTO(guardado);
    }

    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    // Devuelve null si no existe
    public UsuarioDTO obtenerPorId(String idTrabajador) {
        return usuarioRepository.findById(idTrabajador).map(this::toDTO).orElse(null);
    }

    // Borra el usuario y todos sus fichajes. Devuelve false si el usuario no existe.
    @Transactional
    public boolean eliminarUsuario(String idTrabajador) {
        if (!usuarioRepository.existsById(idTrabajador)) {
            return false;
        }
        fichajeRepository.deleteByIdTrabajador(idTrabajador);
        usuarioRepository.deleteById(idTrabajador);
        return true;
    }
}
