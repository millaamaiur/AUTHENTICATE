package es.deusto.sd.almacenamiento.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import es.deusto.sd.almacenamiento.access.repository.FichajeRepository;
import es.deusto.sd.almacenamiento.access.repository.UsuarioRepository;
import es.deusto.sd.almacenamiento.entity.Fichaje;
import es.deusto.sd.almacenamiento.entity.Fichaje.TipoFichaje;
import es.deusto.sd.almacenamiento.entity.Usuario;

/**
 * Carga datos de prueba al arrancar, solo si la base de datos está vacía.
 * Semana de referencia para el informe: lunes 05/10/2026 - viernes 09/10/2026.
 */
@Component
public class DatosPrueba implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final FichajeRepository fichajeRepository;

    public DatosPrueba(UsuarioRepository usuarioRepository, FichajeRepository fichajeRepository) {
        this.usuarioRepository = usuarioRepository;
        this.fichajeRepository = fichajeRepository;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return;
        }

        // Usuarios: TRA001-TRA002, TRA005-TRA008 de alta; TRA003-TRA004 de baja
        añadirUsuario("Ane", "Etxeberria Lasa", "11111111A", "TRA001", "VB-0001", "2026-09-01T09:00", null, true);
        añadirUsuario("Jon", "Garmendia Arrieta", "22222222B", "TRA002", "VB-0002", "2026-10-06T09:00", null, true);
        añadirUsuario("Miren", "Lasa Etxeberria", "33333333C", "TRA003", "VB-0003", "2026-03-01T09:00", "2026-10-08T12:00", false);
        añadirUsuario("Iker", "Aranburu Lasa", "44444444D", "TRA004", "VB-0004", "2025-01-15T09:00", "2026-09-15T12:00", false);
        añadirUsuario("Leire", "Otegi Mendizabal", "55555555E", "TRA005", "VB-0005", "2026-02-01T09:00", null, true);
        añadirUsuario("Maialen", "Arriola Etxeberria", "66666666F", "TRA006", "VB-0006", "2026-01-10T09:00", null, true);
        añadirUsuario("Unai", "Mendizabal Lasa", "77777777G", "TRA007", "VB-0007", "2026-10-09T10:00", null, true);
        añadirUsuario("Nerea", "Zubia Aranburu", "88888888H", "TRA008", "VB-0008", "2026-04-01T09:00", null, true);

        // Fichajes
        // TRA005 tiene 3 fichajes el 05/10: el informe no lo cuenta como día correcto
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-05T08:00");
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-05T16:00");
        añadirFichaje("TRA001", "ED01", "PU02", "2026-10-06T07:45");
        añadirFichaje("TRA001", "ED01", "PU02", "2026-10-06T16:45");
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-07T09:00");
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-07T15:00");
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-08T08:00");
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-09T08:00");
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-09T15:59");
        añadirFichaje("TRA002", "ED02", "PU05", "2026-10-07T08:30");
        añadirFichaje("TRA002", "ED02", "PU05", "2026-10-07T17:00");
        añadirFichaje("TRA002", "ED02", "PU05", "2026-10-08T08:00");
        añadirFichaje("TRA002", "ED02", "PU06", "2026-10-08T16:00");
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-05T08:00");
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-05T16:00");
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-06T08:00");
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-06T16:30");
        añadirFichaje("TRA005", "ED02", "PU05", "2026-10-05T07:00");
        añadirFichaje("TRA005", "ED02", "PU05", "2026-10-05T12:00");
        añadirFichaje("TRA005", "ED02", "PU05", "2026-10-05T16:00");
        añadirFichaje("TRA005", "ED02", "PU06", "2026-10-06T08:00");
        añadirFichaje("TRA005", "ED02", "PU06", "2026-10-06T16:15");
        añadirFichaje("TRA008", "ED03", "PU01", "2026-10-07T08:00");
        añadirFichaje("TRA008", "ED03", "PU01", "2026-10-07T16:00");
    }

    private void añadirUsuario(String nombre, String apellidos, String dni, String idTrabajador,
            String vectorBiometrico, String fecAlta, String fecBaja, boolean estado) {
        usuarioRepository.save(new Usuario(idTrabajador, nombre, apellidos, dni, vectorBiometrico, estado,
                LocalDateTime.parse(fecAlta),
                fecBaja == null ? null : LocalDateTime.parse(fecBaja)));
    }

    // El primer fichaje del día de un trabajador es ENTRADA; los siguientes, SALIDA
    private void añadirFichaje(String idTrabajador, String idEdificio, String idPuerta, String timestamp) {
        LocalDateTime momento = LocalDateTime.parse(timestamp);
        boolean primeroDelDia = fichajeRepository
                .findByIdTrabajadorAndTimestampBetween(idTrabajador, momento.toLocalDate().atStartOfDay(),
                        momento.toLocalDate().atTime(23, 59, 59))
                .isEmpty();
        fichajeRepository.save(new Fichaje(idTrabajador, idEdificio, idPuerta, momento,
                primeroDelDia ? TipoFichaje.ENTRADA : TipoFichaje.SALIDA));
    }
}
