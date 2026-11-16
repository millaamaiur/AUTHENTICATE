package es.deusto.sd.almacenamiento.access.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.deusto.sd.almacenamiento.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
}