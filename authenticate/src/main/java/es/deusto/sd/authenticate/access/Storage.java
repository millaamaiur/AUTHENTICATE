package es.deusto.sd.authenticate.access;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import es.deusto.sd.authenticate.entity.Fichaje;
import es.deusto.sd.authenticate.entity.Usuario;

@Component
public class Storage {

    private Map<String, Usuario> usuarios = new HashMap<>();
    private List<Fichaje> fichajes = new ArrayList<>();

    public Storage() {
        cargarDatosPrueba();
    }

    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Fichaje> getFichajes() {
        return fichajes;
    }

    // ------------------------------------------------------------------
    // DATOS DE PRUEBA
    // Semana de referencia para el informe: lunes 05/10/2026 - viernes 09/10/2026
    // ------------------------------------------------------------------
    private void cargarDatosPrueba() {

        // ---------------- USUARIOS ----------------
        // fecBaja = null -> usuario activo (estado = true)

        // Activa desde antes del rango
        añadirUsuario("Ane", "Etxeberria Lasa", "11111111A", "TRA001", "VB-0001",
                "2026-09-01T09:00", null);

        // Alta DENTRO del rango -> debe salir en el listado de altas
        añadirUsuario("Jon", "Garmendia Arrieta", "22222222B", "TRA002", "VB-0002",
                "2026-10-06T09:00", null);

        // Baja DENTRO del rango -> debe salir en el listado de bajas
        añadirUsuario("Miren", "Aguirre Zubia", "33333333C", "TRA003", "VB-0003",
                "2026-03-01T09:00", "2026-10-08T12:00");

        // Baja ANTES del rango -> no sale en altas, ni bajas, ni fichajes correctos
        añadirUsuario("Iker", "Lasa Otegi", "44444444D", "TRA004", "VB-0004",
                "2025-01-15T09:00", "2026-09-15T12:00");

        // Activa, con un día de 3 eventos (caso a decidir)
        añadirUsuario("Leire", "Otegi Mendizabal", "55555555E", "TRA005", "VB-0005",
                "2026-02-01T09:00", null);

        // ---------------- FICHAJES ----------------

        // TRA001 - Ane
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-05T08:00"); // lunes: 8h00 -> CORRECTO
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-05T16:00");
        añadirFichaje("TRA001", "ED01", "PU02", "2026-10-06T07:45"); // martes: 9h00 -> CORRECTO
        añadirFichaje("TRA001", "ED01", "PU02", "2026-10-06T16:45");
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-07T09:00"); // miércoles: 6h00 -> NO
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-07T15:00");
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-08T08:00"); // jueves: solo entrada -> NO
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-09T08:00"); // viernes: 7h59 -> NO (límite)
        añadirFichaje("TRA001", "ED01", "PU01", "2026-10-09T15:59");

        // TRA002 - Jon (alta el 06/10)
        añadirFichaje("TRA002", "ED02", "PU05", "2026-10-07T08:30"); // miércoles: 8h30 -> CORRECTO
        añadirFichaje("TRA002", "ED02", "PU05", "2026-10-07T17:00");
        añadirFichaje("TRA002", "ED02", "PU05", "2026-10-08T08:00"); // jueves: 8h00 -> CORRECTO
        añadirFichaje("TRA002", "ED02", "PU06", "2026-10-08T16:00");

        // TRA003 - Miren (baja el 08/10)
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-05T08:00"); // lunes: 8h00 -> CORRECTO
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-05T16:00");
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-06T08:00"); // martes: 8h30 -> CORRECTO
        añadirFichaje("TRA003", "ED01", "PU03", "2026-10-06T16:30");

        // TRA004 - Iker: sin fichajes (estaba de baja, la puerta le habría rechazado)

        // TRA005 - Leire
        añadirFichaje("TRA005", "ED02", "PU05", "2026-10-05T07:00"); // lunes: 3 eventos -> ¿?
        añadirFichaje("TRA005", "ED02", "PU05", "2026-10-05T12:00");
        añadirFichaje("TRA005", "ED02", "PU05", "2026-10-05T16:00");
        añadirFichaje("TRA005", "ED02", "PU06", "2026-10-06T08:00"); // martes: 8h15 -> CORRECTO
        añadirFichaje("TRA005", "ED02", "PU06", "2026-10-06T16:15");
    }

    private void añadirUsuario(String nombre, String apellidos, String dni, String idTrabajador,
            String vectorBiometrico, String fecAlta, String fecBaja) {

        boolean activo = (fecBaja == null);

        Usuario usuario = new Usuario(nombre, apellidos, dni, idTrabajador, vectorBiometrico,
                LocalDateTime.parse(fecAlta), activo);

        if (!activo) {
            usuario.setFecBaja(LocalDateTime.parse(fecBaja));
        }

        usuarios.put(idTrabajador, usuario);
    }

    private void añadirFichaje(String idTrabajador, String idEdificio, String idPuerta, String timestamp) {
        fichajes.add(new Fichaje(idTrabajador, idEdificio, idPuerta, LocalDateTime.parse(timestamp)));
    }
}