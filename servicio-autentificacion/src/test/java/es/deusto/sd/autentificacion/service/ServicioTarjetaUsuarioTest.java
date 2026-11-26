package es.deusto.sd.autentificacion.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.deusto.sd.autentificacion.access.AccesoServicioCentral;
import es.deusto.sd.autentificacion.access.AccesoVerificacionFacial;
import es.deusto.sd.autentificacion.dto.FichajeCentralDTO;
import es.deusto.sd.autentificacion.dto.TarjetaDTO;

@ExtendWith(MockitoExtension.class)
class ServicioTarjetaUsuarioTest {

    private static final double[] REFERENCIA = { 0.12, 0.45, 0.78 };
    private static final double[] CAPTURA = { 0.11, 0.46, 0.79 };

    @Mock
    private ServicioBiometrico servicioBiometrico;

    @Mock
    private AccesoVerificacionFacial accesoVerificacionFacial;

    @Mock
    private AccesoServicioCentral accesoServicioCentral;

    private ServicioTarjetaUsuario servicio;

    @BeforeEach
    void crearServicio() {
        servicio = new ServicioTarjetaUsuario(
                servicioBiometrico,
                accesoVerificacionFacial,
                accesoServicioCentral,
                "ED01",
                "PU01");
    }

    @Test
    void siLaVerificacionFacialEsFalseNoSeEnviaFichaje() throws Exception {

        when(servicioBiometrico.generarVectorComprobacion()).thenReturn(CAPTURA);
        when(accesoVerificacionFacial.verificar(REFERENCIA, CAPTURA)).thenReturn(false);

        boolean resultado = servicio.autenticarTarjeta(
                new TarjetaDTO("TRA001", REFERENCIA));

        assertFalse(resultado);
        verifyNoInteractions(accesoServicioCentral);
    }

    @Test
    void siLaVerificacionEsCorrectaSeEnviaElFichajeYSeDevuelveSuResultado() throws Exception {

        when(servicioBiometrico.generarVectorComprobacion()).thenReturn(CAPTURA);
        when(accesoVerificacionFacial.verificar(REFERENCIA, CAPTURA)).thenReturn(true);
        when(accesoServicioCentral.registrarFichaje(any())).thenReturn(true);

        boolean resultado = servicio.autenticarTarjeta(
                new TarjetaDTO("TRA001", REFERENCIA));

        assertTrue(resultado);

        ArgumentCaptor<FichajeCentralDTO> captor = ArgumentCaptor.forClass(FichajeCentralDTO.class);
        verify(accesoServicioCentral).registrarFichaje(captor.capture());

        FichajeCentralDTO fichaje = captor.getValue();
        assertEquals("TRA001", fichaje.getIdTrabajador());
        assertEquals("ED01", fichaje.getIdEdificio());
        assertEquals("PU01", fichaje.getIdPuerta());
        assertNotNull(fichaje.getTimestamp());
    }

    @Test
    void devuelveFalseSiElServicioCentralRechazaElFichaje() throws Exception {

        when(servicioBiometrico.generarVectorComprobacion()).thenReturn(CAPTURA);
        when(accesoVerificacionFacial.verificar(REFERENCIA, CAPTURA)).thenReturn(true);
        when(accesoServicioCentral.registrarFichaje(any())).thenReturn(false);

        boolean resultado = servicio.autenticarTarjeta(
                new TarjetaDTO("TRA001", REFERENCIA));

        assertFalse(resultado);
    }

    @Test
    void devuelveFalseSiNoSePuedeLeerLaCaptura() throws Exception {

        when(servicioBiometrico.generarVectorComprobacion())
                .thenThrow(new IOException("archivo no disponible"));

        boolean resultado = servicio.autenticarTarjeta(
                new TarjetaDTO("TRA001", REFERENCIA));

        assertFalse(resultado);
        verifyNoInteractions(accesoVerificacionFacial, accesoServicioCentral);
    }

    @Test
    void devuelveFalseSiLaTarjetaNoTieneDatos() throws Exception {

        assertFalse(servicio.autenticarTarjeta(null));
        assertFalse(servicio.autenticarTarjeta(new TarjetaDTO(null, REFERENCIA)));
        assertFalse(servicio.autenticarTarjeta(new TarjetaDTO("TRA001", null)));

        verifyNoInteractions(servicioBiometrico, accesoVerificacionFacial, accesoServicioCentral);
    }
}
