package es.deusto.sd.autentificacion.facade;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.deusto.sd.autentificacion.dto.TarjetaDTO;
import es.deusto.sd.autentificacion.service.ServicioTarjetaUsuario;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/autentificacion")
public class ControladorTarjetaUsuario {

    private final ServicioTarjetaUsuario servicioTarjetaUsuario;

    public ControladorTarjetaUsuario(ServicioTarjetaUsuario servicioTarjetaUsuario) {
        this.servicioTarjetaUsuario = servicioTarjetaUsuario;
    }

    @PostMapping
    @Operation(
        summary = "Autenticar una tarjeta",
        description = "Recibe los datos de la tarjeta, captura el vector biometrico de comprobacion, "
            + "lo verifica por sockets con el servicio de verificacion facial y, si es correcto, "
            + "registra el fichaje en el servicio central.")
    public ResponseEntity<Boolean> autenticarTarjeta(
            @RequestBody TarjetaDTO tarjeta) {

        boolean resultado = servicioTarjetaUsuario.autenticarTarjeta(tarjeta);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
    }
}
