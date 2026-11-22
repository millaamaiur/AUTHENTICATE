package es.deusto.sd.almacenamiento.facade;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.almacenamiento.dto.UsuarioDTO;
import es.deusto.sd.almacenamiento.service.AlmacenamientoUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/almacenamiento/usuarios")
@Tag(name = "Almacenamiento de usuarios", description = "Persistencia y consulta de los usuarios del sistema")
public class UsuarioController {

    private final AlmacenamientoUsuarioService usuarioService;

    public UsuarioController(AlmacenamientoUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Guardar un usuario", description = "Almacena un usuario. Si ya existe un usuario con ese "
            + "idTrabajador, se actualizan sus datos (se usa para el alta, la baja y la modificación).", responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario guardado correctamente")
            })
    public ResponseEntity<UsuarioDTO> guardar(@RequestBody UsuarioDTO usuario) {
        return new ResponseEntity<>(usuarioService.guardarUsuario(usuario), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve todos los usuarios almacenados, "
            + "tanto de alta como de baja.", responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuarios (vacía si no hay ninguno)")
            })
    public ResponseEntity<List<UsuarioDTO>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    @GetMapping("/{idTrabajador}")
    @Operation(summary = "Obtener un usuario", description = "Devuelve los datos de un usuario a partir de su idTrabajador.", responses = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "No existe ningún usuario con ese idTrabajador")
    })
    public ResponseEntity<UsuarioDTO> obtenerPorId(
            @Parameter(description = "Identificador del trabajador", example = "TRA001") @PathVariable("idTrabajador") String idTrabajador) {
        UsuarioDTO usuario = usuarioService.obtenerPorId(idTrabajador);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{idTrabajador}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina el usuario y todos sus fichajes.", responses = {
            @ApiResponse(responseCode = "204", description = "Usuario y fichajes eliminados correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe ningún usuario con ese idTrabajador")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador del trabajador", example = "TRA001") @PathVariable("idTrabajador") String idTrabajador) {
        boolean eliminado = usuarioService.eliminarUsuario(idTrabajador);
        if (eliminado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
