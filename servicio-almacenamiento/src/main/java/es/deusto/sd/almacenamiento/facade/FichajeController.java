package es.deusto.sd.almacenamiento.facade;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.almacenamiento.dto.FichajeDTO;
import es.deusto.sd.almacenamiento.service.AlmacenamientoFichajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/almacenamiento/fichajes")
@Tag(name = "Almacenamiento de fichajes", description = "Persistencia y consulta de los fichajes de entrada y salida de los trabajadores")
public class FichajeController {

    private final AlmacenamientoFichajeService fichajeService;

    public FichajeController(AlmacenamientoFichajeService fichajeService) {
        this.fichajeService = fichajeService;
    }

    @PostMapping
    @Operation(summary = "Guardar un fichaje", description = "Almacena un fichaje de entrada o salida de un trabajador.", responses = {
            @ApiResponse(responseCode = "201", description = "Fichaje guardado correctamente")
    })
    public ResponseEntity<FichajeDTO> guardar(@RequestBody FichajeDTO fichaje) {
        return new ResponseEntity<>(fichajeService.guardarFichaje(fichaje), HttpStatus.CREATED);
    }

    @GetMapping("/{idTrabajador}")
    @Operation(summary = "Obtener los fichajes de un trabajador", description = "Devuelve todos los fichajes del trabajador. "
            + "Si se indica el parámetro dia, devuelve solo los de ese día.", responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de fichajes (vacía si no hay ninguno)"),
                    @ApiResponse(responseCode = "400", description = "El día no tiene el formato AAAA-MM-DD")
            })
    public ResponseEntity<List<FichajeDTO>> obtenerPorTrabajador(
            @Parameter(description = "Identificador del trabajador", example = "TRA001") @PathVariable("idTrabajador") String idTrabajador,
            @Parameter(description = "Día concreto (AAAA-MM-DD). Opcional", example = "2026-10-05") @RequestParam(name = "dia", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dia) {
        return ResponseEntity.ok(fichajeService.obtenerFichajesDeTrabajador(idTrabajador, dia));
    }

    @GetMapping
    @Operation(summary = "Obtener los fichajes entre dos fechas", description = "Devuelve los fichajes de todos los trabajadores "
            + "desde el inicio del día inicio hasta el final del día fin, ambos incluidos.", responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de fichajes del periodo (vacía si no hay ninguno)"),
                    @ApiResponse(responseCode = "400", description = "Alguna de las fechas no tiene el formato AAAA-MM-DD")
            })
    public ResponseEntity<List<FichajeDTO>> obtenerEntre(
            @Parameter(description = "Fecha de inicio del periodo (AAAA-MM-DD)", example = "2026-10-05") @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Fecha de fin del periodo (AAAA-MM-DD)", example = "2026-10-09") @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(fichajeService.obtenerFichajesEntre(inicio.atStartOfDay(), fin.atTime(23, 59, 59)));
    }
}
