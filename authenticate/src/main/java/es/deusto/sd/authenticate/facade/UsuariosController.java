package es.deusto.sd.authenticate.facade;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.sd.authenticate.dto.UsuarioDTO;

@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

    @PostMapping
    public ResponseEntity<Void> altaUsuario(
            @RequestBody UsuarioDTO usuario) {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @DeleteMapping("/{idTrabajador}")
    public ResponseEntity<Void> bajaUsuario(
            @PathVariable String idTrabajador) {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
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

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping()
    public ResponseEntity<List<UsuarioDTO>> consultarUsuarios() {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}