package es.deusto.sd.almacenamiento.facade;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.almacenamiento.entity.Fichaje;
import es.deusto.sd.almacenamiento.entity.Usuario;
import es.deusto.sd.almacenamiento.service.AlmacenamientoFichajeService;
import es.deusto.sd.almacenamiento.service.AlmacenamientoUsuarioService;

@RestController
@RequestMapping("/almacenamiento")
public class AlmacenamientoController {

    private final AlmacenamientoUsuarioService usuarioService;
    private final AlmacenamientoFichajeService fichajeService;

    public AlmacenamientoController(AlmacenamientoUsuarioService usuarioService, AlmacenamientoFichajeService fichajeService) {
        this.usuarioService = usuarioService;
        this.fichajeService = fichajeService;
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario) {
        Usuario guardado = usuarioService.guardarUsuario(usuario);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> obtenerTodosUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/usuarios/{idTrabajador}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String idTrabajador) {
        Usuario u = usuarioService.obtenerPorId(idTrabajador);
        if (u != null) {
            return ResponseEntity.ok(u);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/usuarios/{idTrabajador}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String idTrabajador) {
        usuarioService.eliminarUsuario(idTrabajador);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/fichajes")
    public ResponseEntity<Fichaje> guardarFichaje(@RequestBody Fichaje fichaje) {
        Fichaje guardado = fichajeService.guardarFichaje(fichaje);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping("/fichajes/{idTrabajador}")
    public ResponseEntity<List<Fichaje>> obtenerFichajesTrabajador(@PathVariable String idTrabajador) {
        return ResponseEntity.ok(fichajeService.obtenerFichajesDeTrabajador(idTrabajador));
    }
}