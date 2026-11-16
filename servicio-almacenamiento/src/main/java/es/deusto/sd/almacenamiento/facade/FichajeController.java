package es.deusto.sd.almacenamiento.facade;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.almacenamiento.entity.Fichaje;
import es.deusto.sd.almacenamiento.service.AlmacenamientoFichajeService;

@RestController
@RequestMapping("/almacenamiento/fichajes")
public class FichajeController {
    private final AlmacenamientoFichajeService fichajeService;

    public FichajeController(AlmacenamientoFichajeService fichajeService) {
        this.fichajeService = fichajeService;
    }

    @PostMapping
    public ResponseEntity<Fichaje> guardar(@RequestBody Fichaje fichaje) {
        return ResponseEntity.ok(fichajeService.guardarFichaje(fichaje));
    }

    @GetMapping("/{idTrabajador}")
    public ResponseEntity<List<Fichaje>> obtenerPorTrabajador(@PathVariable String idTrabajador) {
        return ResponseEntity.ok(fichajeService.obtenerFichajesDeTrabajador(idTrabajador));
    }
}