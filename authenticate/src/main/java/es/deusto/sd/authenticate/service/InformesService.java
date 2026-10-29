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

        Map<String, Usuario> usuariosMap = accesoDatos.obtenerUsuarios().orElse(new HashMap<>());
        
        for (Usuario u : usuariosMap.values()) {
            // Comprobamos fecAlta (pasándolo a LocalDate para comparar)
            if (u.getFecAlta() != null) {
                LocalDate fechaAlta = u.getFecAlta().toLocalDate();
                if (!fechaAlta.isBefore(inicio) && !fechaAlta.isAfter(fin)) {
                    altas.add(u.getIdTrabajador());
                }
            }
            
            if (u.getFecBaja() != null) {
                LocalDate fechaBaja = u.getFecBaja().toLocalDate();
                if (!fechaBaja.isBefore(inicio) && !fechaBaja.isAfter(fin)) {
                    bajas.add(u.getIdTrabajador());
                }
            }
        }

        List<Fichaje> fichajes = accesoDatos.obtenerFichajesEntre(inicio, fin);


        Map<String, Map<LocalDate, List<Fichaje>>> fichajesPorDia = new HashMap<>();

        for (Fichaje f : fichajes) {
            String idTrabajador = f.getIdTrabajador(); 
            LocalDate dia = f.getTimestamp().toLocalDate();

            fichajesPorDia
                .computeIfAbsent(idTrabajador, k -> new HashMap<>())
                .computeIfAbsent(dia, k -> new ArrayList<>())
                .add(f);
        }

        for (Map.Entry<String, Map<LocalDate, List<Fichaje>>> entryTrabajador : fichajesPorDia.entrySet()) {
            String idTrabajador = entryTrabajador.getKey();
            int diasCorrectos = 0;

            for (Map.Entry<LocalDate, List<Fichaje>> entryDia : entryTrabajador.getValue().entrySet()) {
                List<Fichaje> fichajesDelDia = entryDia.getValue();

                if (fichajesDelDia.size() == 2) {
                    fichajesDelDia.sort((f1, f2) -> f1.getTimestamp().compareTo(f2.getTimestamp()));
                    
                    Fichaje entrada = fichajesDelDia.get(0);
                    Fichaje salida = fichajesDelDia.get(1);

                    Duration duracion = Duration.between(entrada.getTimestamp(), salida.getTimestamp());
                    long horas = duracion.toHours();

                    if (horas >= 8) {
                        diasCorrectos++;
                    }
                }
            }

            if (diasCorrectos > 0) {
                fichajesCorrectos.put(idTrabajador, diasCorrectos);
            }
        }

        return new InformeDTO(altas, bajas, fichajesCorrectos);
    }
}