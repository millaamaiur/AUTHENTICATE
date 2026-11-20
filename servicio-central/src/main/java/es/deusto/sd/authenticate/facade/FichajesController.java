package es.deusto.sd.authenticate.facade;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.authenticate.dto.FichajeDTO;
import es.deusto.sd.authenticate.service.FichajesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/fichajes")
@Tag(name = "Fichajes", description = "Registro de fichajes de entrada y salida de los trabajadores")
public class FichajesController {

    private final FichajesService fichajesService;

    public FichajesController(FichajesService fichajesService) {
        this.fichajesService = fichajesService;
    }

    @PostMapping
    @Operation(summary = "Registrar un fichaje", description = "Registra el fichaje de un trabajador en una puerta de un edificio, "
            + "si está activo y la fecha está dentro de su periodo de alta.", responses = {
                    @ApiResponse(responseCode = "201", description = "El fichaje se ha registrado y guardado"),
                    @ApiResponse(responseCode = "409", description = "El fichaje se ha rechazado porque no cumple las condiciones del servicio")
            })
    public ResponseEntity<Void> registrarFichaje(
            @RequestBody FichajeDTO fichaje) {

        boolean resultado = fichajesService.registrarFichaje(fichaje);

        if (resultado) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
