package es.deusto.sd.autentificacion.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import es.deusto.sd.autentificacion.access.AccesoCapturaCara;

@Service
public class ServicioBiometrico {

    private final AccesoCapturaCara accesoCapturaCara;

    public ServicioBiometrico(AccesoCapturaCara accesoCapturaCara) {
        this.accesoCapturaCara = accesoCapturaCara;
    }

    public List<Double> generarVectorComprobacion() throws IOException {
        return accesoCapturaCara.capturarVectorRostro();
    }
}
