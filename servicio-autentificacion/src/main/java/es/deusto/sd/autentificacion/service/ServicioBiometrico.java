package es.deusto.sd.autentificacion.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import es.deusto.sd.autentificacion.access.AccesoCapturaCara;

@Service
public class ServicioBiometrico {

    private final AccesoCapturaCara accesoCapturaCara;

    public ServicioBiometrico(AccesoCapturaCara accesoCapturaCara) {
        this.accesoCapturaCara = accesoCapturaCara;
    }

    public double[] generarVectorComprobacion() throws IOException {
        return accesoCapturaCara.capturarVectorRostro();
    }
}
