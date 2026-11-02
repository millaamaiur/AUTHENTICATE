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
import es.deusto.sd.authenticate.entity.MovimientoUsuario;
import es.deusto.sd.authenticate.entity.TipoMovimientoUsuario;
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

        // -------------------------------------------------
        // ALTAS Y BAJAS
        // -------------------------------------------------

        List<MovimientoUsuario> movimientos =
                accesoDatos.obtenerMovimientosEntre(inicio, fin);

        for (MovimientoUsuario movimiento : movimientos) {

            if (movimiento.getTipo() == TipoMovimientoUsuario.ALTA) {
                altas.add(movimiento.getIdTrabajador());
            }

            if (movimiento.getTipo() == TipoMovimientoUsuario.BAJA) {
                bajas.add(movimiento.getIdTrabajador());
            }
        }

        // -------------------------------------------------
        // INICIALIZAR USUARIOS ACTIVOS CON 0 DÍAS CORRECTOS
        // -------------------------------------------------

        for (Usuario usuario : usuariosMap.values()) {
            fichajesCorrectos.put(usuario.getIdTrabajador(), 0);
        }

        // -------------------------------------------------
        // OBTENER FICHAJES DEL RANGO
        // -------------------------------------------------

        List<Fichaje> fichajes =
                accesoDatos.obtenerFichajesEntre(inicio, fin);

        Map<String, Map<LocalDate, List<Fichaje>>> fichajesPorDia =
                new HashMap<>();

        for (Fichaje f : fichajes) {

            String idTrabajador = f.getIdTrabajador();

            LocalDate dia =
                    f.getTimestamp().toLocalDate();

            fichajesPorDia
                    .computeIfAbsent(
                            idTrabajador,
                            k -> new HashMap<>())
                    .computeIfAbsent(
                            dia,
                            k -> new ArrayList<>())
                    .add(f);
        }

        // -------------------------------------------------
        // CALCULAR DÍAS CORRECTOS
        // -------------------------------------------------

        for (Map.Entry<String, Map<LocalDate, List<Fichaje>>> entryTrabajador
                : fichajesPorDia.entrySet()) {

            String idTrabajador =
                    entryTrabajador.getKey();

            int diasCorrectos = 0;

            for (Map.Entry<LocalDate, List<Fichaje>> entryDia
                    : entryTrabajador.getValue().entrySet()) {

                List<Fichaje> fichajesDelDia =
                        entryDia.getValue();

                if (fichajesDelDia.size() == 2) {

                    fichajesDelDia.sort(
                            (f1, f2) ->
                                    f1.getTimestamp()
                                            .compareTo(f2.getTimestamp()));

                    Fichaje entrada =
                            fichajesDelDia.get(0);

                    Fichaje salida =
                            fichajesDelDia.get(1);

                    Duration duracion =
                            Duration.between(
                                    entrada.getTimestamp(),
                                    salida.getTimestamp());

                    long horas =
                            duracion.toHours();

                    if (horas >= 8) {
                        diasCorrectos++;
                    }
                }
            }

            fichajesCorrectos.put(
                    idTrabajador,
                    diasCorrectos);
        }

        return new InformeDTO(
                altas,
                bajas,
                fichajesCorrectos);
    }
}