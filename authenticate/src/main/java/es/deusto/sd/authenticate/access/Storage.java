package es.deusto.sd.authenticate.access;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import es.deusto.sd.authenticate.entity.Fichaje;
import es.deusto.sd.authenticate.entity.MovimientoUsuario;
import es.deusto.sd.authenticate.entity.TipoFichaje;
import es.deusto.sd.authenticate.entity.TipoMovimientoUsuario;
import es.deusto.sd.authenticate.entity.Usuario;

@Component
public class Storage {

    private Map<String, Usuario> usuarios = new HashMap<>();
    private List<Fichaje> fichajes = new ArrayList<>();
    private List<MovimientoUsuario> movimientosUsuarios = new ArrayList<>();

    public Storage() {
        cargarDatosPrueba();
    }

    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Fichaje> getFichajes() {
        return fichajes;
    }

    public List<MovimientoUsuario> getMovimientosUsuarios() {
        return movimientosUsuarios;
    }

    // ------------------------------------------------------------------
    // DATOS DE PRUEBA
    // Semana de referencia para el informe: lunes 05/10/2026 - viernes 09/10/2026
    // ------------------------------------------------------------------
    private void cargarDatosPrueba() {

        // ------------------------------------------------------------
        // USUARIOS ACTIVOS
        // ------------------------------------------------------------

        // TRA001 - Ane
        // Activa desde antes del rango
        añadirUsuario(
                "Ane",
                "Etxeberria Lasa",
                "11111111A",
                "TRA001",
                "VB-0001",
                "2026-09-01T09:00");

        // TRA002 - Jon
        // Alta dentro del rango
        añadirUsuario(
                "Jon",
                "Garmendia Arrieta",
                "22222222B",
                "TRA002",
                "VB-0002",
                "2026-10-06T09:00");

        // TRA003 - Miren
        // No se añade como usuario activo.
        // Fue dada de baja el 08/10/2026.
        // Sus fichajes históricos sí se conservan.

        // TRA004 - Iker
        // No se añade como usuario activo.
        // Fue dado de baja el 15/09/2026.
        // No tiene fichajes en esta semana.

        // TRA005 - Leire
        // Activa desde antes del rango
        añadirUsuario(
                "Leire",
                "Otegi Mendizabal",
                "55555555E",
                "TRA005",
                "VB-0005",
                "2026-02-01T09:00");

        // TRA006 - Maialen
        // Activa desde antes del rango, pero sin fichajes
        // Sirve para comprobar un usuario activo con 0 días correctos
        añadirUsuario(
                "Maialen",
                "Arriola Etxeberria",
                "66666666F",
                "TRA006",
                "VB-0006",
                "2026-01-10T09:00");

        // TRA007 - Unai
        // Alta dentro del rango, pero sin fichajes
        // Sirve para comprobar que aparece en altas aunque no fiche
        añadirUsuario(
                "Unai",
                "Mendizabal Lasa",
                "77777777G",
                "TRA007",
                "VB-0007",
                "2026-10-09T10:00");

        // TRA008 - Nerea
        // Activa desde antes del rango
        // Tiene exactamente 8 horas de fichaje
        añadirUsuario(
                "Nerea",
                "Zubia Aranburu",
                "88888888H",
                "TRA008",
                "VB-0008",
                "2026-04-01T09:00");

        // ------------------------------------------------------------
        // FICHAJES
        // ------------------------------------------------------------

        // TRA001 - Ane

        // 05/10 -> 8h00 -> CORRECTO
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-05T08:00");
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-05T16:00");

        // 06/10 -> 9h00 -> CORRECTO
        añadirFichaje("TRA001", "ED01", "PU02", "2026-10-06T07:45");
        añadirFichaje("TRA001", "ED01", "PU02", "2026-10-06T16:45");

        // 07/10 -> 6h00 -> NO CORRECTO
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-07T09:00");
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-07T15:00");

        // 08/10 -> solo un fichaje -> NO CORRECTO
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-08T08:00");

        // 09/10 -> 7h59 -> NO CORRECTO
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-09T08:00");
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-09T15:59");

        // TRA002 - Jon
        // Alta el 06/10

        // 07/10 -> 8h30 -> CORRECTO
        añadirFichaje("TRA002", "ED02", "PU05", "2026-10-07T08:30");
        añadirFichaje("TRA002", "ED02", "PU05", "2026-10-07T17:00");

        // 08/10 -> 8h00 -> CORRECTO
        añadirFichaje("TRA002", "ED02", "PU05", "2026-10-08T08:00");
        añadirFichaje("TRA002", "ED02", "PU06", "2026-10-08T16:00");

        // TRA003 - Miren
        // Ya no existe como usuario porque fue dada de baja el 08/10.
        // Conservamos sus fichajes históricos.

        // 05/10 -> 8h00 -> CORRECTO
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-05T08:00");
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-05T16:00");

        // 06/10 -> 8h30 -> CORRECTO
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-06T08:00");
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-06T16:30");

        // TRA004 - Iker
        // Ya estaba de baja antes del rango.
        // No tiene fichajes durante esta semana.

        // TRA005 - Leire

        // 05/10 -> 3 fichajes -> NO CORRECTO
        // El requisito dice exactamente 2 eventos
        añadirFichaje("TRA005", "ED02", "PU05", "2026-10-05T07:00");
        añadirFichaje("TRA005", "ED02", "PU05", "2026-10-05T12:00");
        añadirFichaje("TRA005", "ED02", "PU05", "2026-10-05T16:00");

        // 06/10 -> 8h15 -> CORRECTO
        añadirFichaje("TRA005", "ED02", "PU06", "2026-10-06T08:00");
        añadirFichaje("TRA005", "ED02", "PU06", "2026-10-06T16:15");

        // TRA006 - Maialen
        // Sin fichajes.
        // Debe seguir apareciendo como usuario activo con 0 días correctos.

        // TRA007 - Unai
        // Alta dentro del rango, pero sin fichajes.
        // Debe aparecer en altas y tener 0 días correctos.

        // TRA008 - Nerea

        // 07/10 -> exactamente 8h00 -> CORRECTO
        añadirFichaje("TRA008", "ED03", "PU01", "2026-10-07T08:00");
        añadirFichaje("TRA008", "ED03", "PU01", "2026-10-07T16:00");

        // Movimientos de los usuarios ya dados de baja (los activos
        // generan su ALTA en añadirUsuario)
        movimientosUsuarios.add(new MovimientoUsuario(
                "TRA003",
                TipoMovimientoUsuario.ALTA,
                LocalDateTime.parse("2026-03-01T09:00")));

        movimientosUsuarios.add(new MovimientoUsuario(
                "TRA003",
                TipoMovimientoUsuario.BAJA,
                LocalDateTime.parse("2026-10-08T12:00")));

        movimientosUsuarios.add(new MovimientoUsuario(
                "TRA004",
                TipoMovimientoUsuario.ALTA,
                LocalDateTime.parse("2025-01-15T09:00")));

        movimientosUsuarios.add(new MovimientoUsuario(
                "TRA004",
                TipoMovimientoUsuario.BAJA,
                LocalDateTime.parse("2026-09-15T12:00")));
    }

    private void añadirUsuario(String nombre, String apellidos, String dni, String idTrabajador,
            String vectorBiometrico, String fecAlta) {

        usuarios.put(idTrabajador,
                new Usuario(nombre, apellidos, dni, idTrabajador, vectorBiometrico));

        movimientosUsuarios.add(new MovimientoUsuario(
                idTrabajador,
                TipoMovimientoUsuario.ALTA,
                LocalDateTime.parse(fecAlta)));
    }

    // Los fichajes de prueba se añaden en orden cronológico:
    // el 1.º del día es ENTRADA y el siguiente SALIDA.
    private void añadirFichaje(String idTrabajador, String idEdificio, String idPuerta, String timestamp) {
        LocalDateTime momento = LocalDateTime.parse(timestamp);

        boolean primeroDelDia = fichajes.stream().noneMatch(f ->
                f.getIdTrabajador().equals(idTrabajador)
                        && f.getTimestamp().toLocalDate().equals(momento.toLocalDate()));

        fichajes.add(new Fichaje(idTrabajador, idEdificio, idPuerta, momento,
                primeroDelDia ? TipoFichaje.ENTRADA : TipoFichaje.SALIDA));
    }
}