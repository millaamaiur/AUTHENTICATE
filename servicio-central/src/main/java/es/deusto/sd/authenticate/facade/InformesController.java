package es.deusto.sd.authenticate.facade;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.authenticate.dto.InformeDTO;
import es.deusto.sd.authenticate.service.InformesService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/informes")
@Tag(name = "Informes", description = "Generación de informes sobre empleados y sus fichajes entre dos fechas")
public class InformesController {

    private final InformesService informesService;

    public InformesController(InformesService informesService) {
        this.informesService = informesService;
    }

    @GetMapping
    @Operation(summary = "Obtener un informe", description = "Genera un informe para el periodo indicado mediante los parámetros "
            + "inicio y fin, con formato de fecha AAAA-MM-DD.", responses = {
                    @ApiResponse(responseCode = "200", description = "Informe generado correctamente con las altas, bajas y fichajes correctos del periodo"),
                    @ApiResponse(responseCode = "400", description = "Alguna de las fechas no tiene el formato AAAA-MM-DD")
            })
    public ResponseEntity<InformeDTO> obtenerInforme(
            @Parameter(description = "Fecha de inicio del periodo del informe (AAAA-MM-DD)", example = "2026-10-05") @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Fecha de fin del periodo del informe (AAAA-MM-DD)", example = "2026-10-09") @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        InformeDTO informe = informesService.generarInforme(inicio, fin);
        return ResponseEntity.ok(informe);
    }
}