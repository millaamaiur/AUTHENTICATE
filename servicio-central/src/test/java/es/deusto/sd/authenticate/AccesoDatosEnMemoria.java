package es.deusto.sd.authenticate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import es.deusto.sd.authenticate.access.AccesoDatosCentral;
import es.deusto.sd.authenticate.entity.Fichaje;
import es.deusto.sd.authenticate.entity.Fichaje.TipoFichaje;
import es.deusto.sd.authenticate.entity.Usuario;

/**
 * AccesoDatosCentral para los tests: guarda los datos en memoria, sin llamar al
 * servicio de almacenamiento. Contiene los mismos datos de prueba que el almacenamiento.
 */
class AccesoDatosEnMemoria extends AccesoDatosCentral {

    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();
    private final List<Fichaje> fichajes = new ArrayList<>();

    AccesoDatosEnMemoria() {
        super("http://localhost:0");
        añadirUsuario("Ane", "Etxeberria Lasa", "11111111A", "TRA001", "VB-0001", "2026-09-01T09:00", null, true);
        añadirUsuario("Jon", "Garmendia Arrieta", "22222222B", "TRA002", "VB-0002", "2026-10-06T09:00", null, true);
        añadirUsuario("Miren", "Lasa Etxeberria", "33333333C", "TRA003", "VB-0003", "2026-03-01T09:00", "2026-10-08T12:00", false);
        añadirUsuario("Iker", "Aranburu Lasa", "44444444D", "TRA004", "VB-0004", "2025-01-15T09:00", "2026-09-15T12:00", false);
        añadirUsuario("Leire", "Otegi Mendizabal", "55555555E", "TRA005", "VB-0005", "2026-02-01T09:00", null, true);
        añadirUsuario("Maialen", "Arriola Etxeberria", "66666666F", "TRA006", "VB-0006", "2026-01-10T09:00", null, true);
        añadirUsuario("Unai", "Mendizabal Lasa", "77777777G", "TRA007", "VB-0007", "2026-10-09T10:00", null, true);
        añadirUsuario("Nerea", "Zubia Aranburu", "88888888H", "TRA008", "VB-0008", "2026-04-01T09:00", null, true);

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

    List<Fichaje> getFichajes() {
        return fichajes;
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        usuarios.put(usuario.getIdTrabajador(), usuario);
    }

    @Override
    public void eliminarUsuario(String idTrabajador) {
        usuarios.remove(idTrabajador);
        fichajes.removeIf(f -> f.getIdTrabajador().equals(idTrabajador));
    }

    @Override
    public Optional<Usuario> obtenerUsuario(String idTrabajador) {
        return Optional.ofNullable(usuarios.get(idTrabajador));
    }

    @Override
    public Optional<Map<String, Usuario>> obtenerUsuarios() {
        return Optional.of(usuarios);
    }

    @Override
    public void guardarFichaje(Fichaje fichaje) {
        fichajes.add(fichaje);
    }

    @Override
    public List<Fichaje> obtenerFichajes(String idTrabajador, LocalDate dia) {
        return fichajes.stream()
                .filter(f -> f.getIdTrabajador().equals(idTrabajador)
                        && f.getTimestamp().toLocalDate().equals(dia))
                .toList();
    }

    @Override
    public List<Fichaje> obtenerFichajesEntre(LocalDate inicio, LocalDate fin) {
        return fichajes.stream()
                .filter(f -> !f.getTimestamp().toLocalDate().isBefore(inicio)
                        && !f.getTimestamp().toLocalDate().isAfter(fin))
                .toList();
    }

    private void añadirUsuario(String nombre, String apellidos, String dni, String idTrabajador,
            String vectorBiometrico, String fecAlta, String fecBaja, boolean estado) {
        usuarios.put(idTrabajador, new Usuario(nombre, apellidos, dni, idTrabajador, vectorBiometrico,
                LocalDateTime.parse(fecAlta), fecBaja == null ? null : LocalDateTime.parse(fecBaja), estado));
    }

    private void añadirFichaje(String idTrabajador, String idEdificio, String idPuerta, String timestamp) {
        LocalDateTime momento = LocalDateTime.parse(timestamp);
        boolean primeroDelDia = fichajes.stream().noneMatch(f -> f.getIdTrabajador().equals(idTrabajador)
                && f.getTimestamp().toLocalDate().equals(momento.toLocalDate()));
        fichajes.add(new Fichaje(idTrabajador, idEdificio, idPuerta, momento,
                primeroDelDia ? TipoFichaje.ENTRADA : TipoFichaje.SALIDA));
    }
}
