package es.deusto.sd.autentificacion.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.deusto.sd.autentificacion.access.AccesoServicioCentral;
import es.deusto.sd.autentificacion.access.AccesoVerificacionFacial;
import es.deusto.sd.autentificacion.dto.FichajeCentralDTO;
import es.deusto.sd.autentificacion.dto.TarjetaDTO;

@Service
public class ServicioTarjetaUsuario {

    private final ServicioBiometrico servicioBiometrico;
    private final AccesoVerificacionFacial accesoVerificacionFacial;
    private final AccesoServicioCentral accesoServicioCentral;

    @Value("${edge.edificio.id}")
    private String idEdificio;

    @Value("${edge.puerta.id}")
    private String idPuerta;

    public ServicioTarjetaUsuario(
            ServicioBiometrico servicioBiometrico,
            AccesoVerificacionFacial accesoVerificacionFacial,
            AccesoServicioCentral accesoServicioCentral) {

        this.servicioBiometrico = servicioBiometrico;
        this.accesoVerificacionFacial = accesoVerificacionFacial;
        this.accesoServicioCentral = accesoServicioCentral;
    }

    public boolean autenticarTarjeta(TarjetaDTO tarjeta) {

        if (tarjeta == null
                || tarjeta.getIdTrabajador() == null
                || tarjeta.getVectorReferencia() == null) {
            return false;
        }

        try {
            // Captura de rostro: se lee el vector de comprobacion del archivo JSON
            List<Double> vectorComprobacion = servicioBiometrico.generarVectorComprobacion();

            // Verificacion facial por sockets: referencia y comprobacion
            boolean verificado = accesoVerificacionFacial.verificar(
                    tarjeta.getVectorReferencia(),
                    vectorComprobacion);

            if (!verificado) {
                return false;
            }

            // Si la verificacion es satisfactoria, se prepara el evento de fichaje
            FichajeCentralDTO fichaje = new FichajeCentralDTO(
                    tarjeta.getIdTrabajador(),
                    idEdificio,
                    idPuerta,
                    LocalDateTime.now());

            // El servicio central valida y persiste el fichaje
            return accesoServicioCentral.registrarFichaje(fichaje);

        } catch (IOException e) {
            System.err.println(" # ServicioTarjetaUsuario: IO error: " + e.getMessage());
            return false;
        }
    }
}
