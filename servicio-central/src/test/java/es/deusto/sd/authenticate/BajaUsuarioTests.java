package es.deusto.sd.authenticate;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import es.deusto.sd.authenticate.access.AccesoDatosCentral;
import es.deusto.sd.authenticate.access.Storage;
import es.deusto.sd.authenticate.dto.FichajeDTO;
import es.deusto.sd.authenticate.entity.Usuario;
import es.deusto.sd.authenticate.service.FichajesService;
import es.deusto.sd.authenticate.service.InformesService;
import es.deusto.sd.authenticate.service.UsuariosService;

class BajaUsuarioTests {

    @Test
    void bajaConservaUsuarioFichajesYFechaOriginal() {
        Storage storage = new Storage();
        AccesoDatosCentral acceso = new AccesoDatosCentral(storage);
        UsuariosService usuarios = new UsuariosService(acceso);
        FichajesService fichajes = new FichajesService(acceso);
        Usuario usuario = acceso.obtenerUsuario("TRA001").orElseThrow();
        var fichajesAnteriores = storage.getFichajes().stream()
                .filter(f -> f.getIdTrabajador().equals("TRA001")).toList();

        assertTrue(usuarios.bajaUsuario("TRA001"));
        assertSame(usuario, acceso.obtenerUsuario("TRA001").orElseThrow());
        assertFalse(usuario.isEstado());
        assertNotNull(usuario.getFecBaja());
        assertEquals(fichajesAnteriores, storage.getFichajes().stream()
                .filter(f -> f.getIdTrabajador().equals("TRA001")).toList());

        // Una fecha fija permite detectar si repetir la baja la sobrescribe.
        LocalDateTime fechaOriginal = LocalDateTime.of(2026, 10, 10, 12, 0);
        usuario.setFecBaja(fechaOriginal);
        assertTrue(usuarios.bajaUsuario("TRA001"));
        assertEquals(fechaOriginal, usuario.getFecBaja());
        assertFalse(fichajes.registrarFichaje(new FichajeDTO(
                "TRA001", "ED01", "PU01", fechaOriginal.plusDays(1))));
        assertFalse(usuarios.bajaUsuario("NO-EXISTE"));
    }

    @Test
    void informeIncluyeBajasPeroSoloCuentaFichajesDeActivos() {
        InformesService informes = new InformesService(new AccesoDatosCentral(new Storage()));
        var informe = informes.generarInforme(
                LocalDate.of(2026, 10, 5), LocalDate.of(2026, 10, 9));

        assertEquals(Set.of("TRA002", "TRA007"), Set.copyOf(informe.getAltas()));
        assertEquals(Set.of("TRA003"), Set.copyOf(informe.getBajas()));
        assertEquals(Map.of("TRA001", 2, "TRA002", 2, "TRA005", 1,
                "TRA006", 0, "TRA007", 0, "TRA008", 1),
                informe.getFichajesCorrectosPorTrabajador());
    }
}
