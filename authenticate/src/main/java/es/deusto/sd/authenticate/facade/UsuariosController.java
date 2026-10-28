package es.deusto.sd.authenticate.facade;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.sd.authenticate.dto.UsuarioDTO;
import es.deusto.sd.authenticate.service.UsuariosService;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    private final UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @PostMapping
    public ResponseEntity<Void> altaUsuario(
            @RequestBody UsuarioDTO usuario) {

        boolean resultado = usuariosService.altaUsuario(usuario);

        if (resultado) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{idTrabajador}")
    public ResponseEntity<Void> bajaUsuario(
            @PathVariable String idTrabajador) {

        boolean resultado = usuariosService.bajaUsuario(idTrabajador);

        if (resultado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{idTrabajador}")
    public ResponseEntity<Void> modificarUsuario(
            @PathVariable String idTrabajador,
            @RequestBody UsuarioDTO datos) {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{idTrabajador}")
    public ResponseEntity<UsuarioDTO> consultarUsuario(
            @PathVariable String idTrabajador) {

        UsuarioDTO u = usuariosService.consultarUsuario(idTrabajador);

        if (u != null) {
            return new ResponseEntity<>(u, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping()
    public ResponseEntity<List<UsuarioDTO>> consultarUsuarios() {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}