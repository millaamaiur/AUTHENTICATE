package es.deusto.sd.authenticate.service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import es.deusto.sd.authenticate.access.AccesoDatosCentral;
import es.deusto.sd.authenticate.dto.InformeDTO;
import es.deusto.sd.authenticate.entity.Fichaje;
import es.deusto.sd.authenticate.entity.Usuario;

@Service
public class InformesService {

    private final AccesoDatosCentral accesoDatos;

    public InformesService(AccesoDatosCentral accesoDatos) {
        this.accesoDatos = accesoDatos;
    }

    public InformeDTO generarInforme(LocalDate inicio, LocalDate fin) {

        List<String> altas = new ArrayList<>();
        List<String> bajas = new ArrayList<>();
        Map<String, Integer> fichajesCorrectos = new HashMap<>();

        Map<String, Usuario> usuariosMap =
                accesoDatos.obtenerUsuarios().orElse(new HashMap<>());

        // Obtener altas y bajas dentro del intervalo, incluidos sus extremos.
        for (Usuario usuario : usuariosMap.values()) {

            if (usuario.getFecAlta() != null) {
                LocalDate fechaAlta = usuario.getFecAlta().toLocalDate();

                if (!fechaAlta.isBefore(inicio) && !fechaAlta.isAfter(fin)) {
                    altas.add(usuario.getIdTrabajador());
                }
            }

            if (usuario.getFecBaja() != null) {
                LocalDate fechaBaja = usuario.getFecBaja().toLocalDate();

                if (!fechaBaja.isBefore(inicio) && !fechaBaja.isAfter(fin)) {
                    bajas.add(usuario.getIdTrabajador());
                }
            }

            // Los activos aparecen incluso si no tienen fichajes correctos.
            if (usuario.isEstado()) {
                fichajesCorrectos.put(usuario.getIdTrabajador(), 0);
            }
        }

        List<Fichaje> fichajes =
                accesoDatos.obtenerFichajesEntre(inicio, fin);

        Map<String, Map<LocalDate, List<Fichaje>>> fichajesPorDia =
                new HashMap<>();

        // Agrupar los fichajes de usuarios activos por trabajador y día.
        for (Fichaje fichaje : fichajes) {
            String idTrabajador = fichaje.getIdTrabajador();

            if (!fichajesCorrectos.containsKey(idTrabajador)) {
                continue;
            }

            LocalDate dia = fichaje.getTimestamp().toLocalDate();

            fichajesPorDia
                    .computeIfAbsent(idTrabajador, clave -> new HashMap<>())
                    .computeIfAbsent(dia, clave -> new ArrayList<>())
                    .add(fichaje);
        }

        // Contar días con exactamente dos eventos y al menos ocho horas.
        for (Map.Entry<String, Map<LocalDate, List<Fichaje>>> trabajador
                : fichajesPorDia.entrySet()) {

            int diasCorrectos = 0;

            for (List<Fichaje> fichajesDelDia : trabajador.getValue().values()) {

                if (fichajesDelDia.size() != 2) {
                    continue;
                }

                fichajesDelDia.sort(
                        (primero, segundo) -> primero.getTimestamp()
                                .compareTo(segundo.getTimestamp()));

                Fichaje entrada = fichajesDelDia.get(0);
                Fichaje salida = fichajesDelDia.get(1);

                Duration duracion = Duration.between(
                        entrada.getTimestamp(),
                        salida.getTimestamp());

                if (duracion.compareTo(Duration.ofHours(8)) >= 0) {
                    diasCorrectos++;
                }
            }

            fichajesCorrectos.put(trabajador.getKey(), diasCorrectos);
        }

        return new InformeDTO(altas, bajas, fichajesCorrectos);
    }
}