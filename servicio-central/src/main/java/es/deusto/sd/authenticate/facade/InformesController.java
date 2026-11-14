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

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/informes")
public class InformesController {

    private final InformesService informesService;

    public InformesController(InformesService informesService) {
        this.informesService = informesService;
    }

    @GetMapping
    @Operation(
        summary = "Obtener un informe",
        description = "Genera un informe para el periodo indicado mediante los parámetros "
            + "inicio y fin, con formato de fecha AAAA-MM-DD.")
    public ResponseEntity<InformeDTO> obtenerInforme(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        InformeDTO informe = informesService.generarInforme(inicio, fin);
        return ResponseEntity.ok(informe);
    }
}
