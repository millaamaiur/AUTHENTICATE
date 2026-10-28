package es.deusto.sd.authenticate.facade;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.deusto.sd.authenticate.dto.FichajeDTO;
import es.deusto.sd.authenticate.service.FichajesService;

@RestController
@RequestMapping("/fichajes")
public class FichajesController {

    private final FichajesService fichajesService;

    public FichajesController(FichajesService fichajesService) {
        this.fichajesService = fichajesService;
    }

    @PostMapping
    public ResponseEntity<Void> registrarFichaje(
            @RequestBody FichajeDTO fichaje) {

        boolean resultado = fichajesService.registrarFichaje(fichaje);

        if (resultado) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
