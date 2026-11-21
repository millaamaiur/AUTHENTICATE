package es.deusto.sd.authenticate.facade;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.authenticate.dto.UsuarioDTO;
import es.deusto.sd.authenticate.service.UsuariosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios: alta, baja, modificación y consulta")
public class UsuariosController {

    private final UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @PostMapping
    @Operation(summary = "Dar de alta un usuario", description = "Registra un nuevo usuario con los datos proporcionados.", responses = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
            @ApiResponse(responseCode = "409", description = "Ya existe un usuario con ese idTrabajador")
    })
    public ResponseEntity<Void> altaUsuario(
            @RequestBody UsuarioDTO usuario) {

        boolean resultado = usuariosService.altaUsuario(usuario);

        if (resultado) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/{idTrabajador}/baja")
    @Operation(summary = "Dar de baja un usuario", description = "Da de baja al usuario identificado por idTrabajador.", responses = {
            @ApiResponse(responseCode = "204", description = "Usuario dado de baja correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe ningún usuario con ese idTrabajador")
    })
    public ResponseEntity<Void> bajaUsuario(
            @Parameter(description = "Identificador del trabajador", example = "TRA001") @PathVariable String idTrabajador) {

        boolean resultado = usuariosService.bajaUsuario(idTrabajador);

        if (resultado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{idTrabajador}")
    @Operation(summary = "Modificar un usuario", description = "Actualiza los datos del usuario identificado por idTrabajador.", responses = {
            @ApiResponse(responseCode = "204", description = "Usuario modificado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe ningún usuario con ese idTrabajador")
    })
    public ResponseEntity<Void> modificarUsuario(
            @Parameter(description = "Identificador del trabajador", example = "TRA001") @PathVariable String idTrabajador,
            @RequestBody UsuarioDTO datos) {

        boolean resultado = usuariosService.modificarUsuario(idTrabajador, datos);

        if (resultado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{idTrabajador}")
    @Operation(summary = "Consultar un usuario", description = "Obtiene los datos del usuario identificado por idTrabajador.", responses = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado, se devuelven sus datos"),
            @ApiResponse(responseCode = "404", description = "No existe ningún usuario con ese idTrabajador")
    })
    public ResponseEntity<UsuarioDTO> consultarUsuario(
            @Parameter(description = "Identificador del trabajador", example = "TRA001") @PathVariable String idTrabajador) {

        UsuarioDTO u = usuariosService.consultarUsuario(idTrabajador);

        if (u != null) {
            return new ResponseEntity<>(u, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    @Operation(summary = "Consultar todos los usuarios", description = "Obtiene la lista de usuarios.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios (puede estar vacía)")
    })
    public ResponseEntity<List<UsuarioDTO>> consultarUsuarios() {

        List<UsuarioDTO> usuarios = usuariosService.consultarUsuarios();

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @DeleteMapping("/{idTrabajador}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina todos los datos del usuario identificado por idTrabajador: "
            + "sus datos personales y todos sus fichajes.", responses = {
                    @ApiResponse(responseCode = "204", description = "Usuario y todos sus datos eliminados correctamente"),
                    @ApiResponse(responseCode = "404", description = "No existe ningún usuario con ese idTrabajador")
            })
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "Identificador del trabajador", example = "TRA001") @PathVariable String idTrabajador) {

        boolean resultado = usuariosService.eliminarUsuario(idTrabajador);

        if (resultado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
