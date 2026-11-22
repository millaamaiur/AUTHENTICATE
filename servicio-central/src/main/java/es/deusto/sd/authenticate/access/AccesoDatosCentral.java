package es.deusto.sd.authenticate.access;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import es.deusto.sd.authenticate.access.dto.FichajeAlmacenamientoDTO;
import es.deusto.sd.authenticate.access.dto.UsuarioAlmacenamientoDTO;
import es.deusto.sd.authenticate.entity.Fichaje;
import es.deusto.sd.authenticate.entity.Usuario;

/**
 * Acceso a los datos del servicio central. Ya no guarda nada en memoria: todas las
 * operaciones se hacen por REST contra el servicio de almacenamiento.
 */
@Repository
public class AccesoDatosCentral {

    private final RestClient restClient;

    public AccesoDatosCentral(@Value("${almacenamiento.url}") String urlAlmacenamiento) {
        this.restClient = RestClient.create(urlAlmacenamiento + "/almacenamiento");
    }

    // ------------------------------------------------------------------
    // USUARIOS
    // ------------------------------------------------------------------

    public void guardarUsuario(Usuario usuario) {
        restClient.post()
                .uri("/usuarios")
                .body(usuarioADTO(usuario))
                .retrieve()
                .toBodilessEntity();
    }

    public void eliminarUsuario(String idTrabajador) {
        // El 404 (usuario inexistente) no es un error aquí: el service ya lo ha comprobado
        restClient.delete()
                .uri("/usuarios/{idTrabajador}", idTrabajador)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                })
                .toBodilessEntity();
    }

    public Optional<Usuario> obtenerUsuario(String idTrabajador) {
        UsuarioAlmacenamientoDTO dto = restClient.get()
                .uri("/usuarios/{idTrabajador}", idTrabajador)
                .exchange((request, response) -> {
                    if (response.getStatusCode().value() == 404) {
                        return null;
                    }
                    return response.bodyTo(UsuarioAlmacenamientoDTO.class);
                });
        return Optional.ofNullable(dto).map(this::dtoAUsuario);
    }

    public Optional<Map<String, Usuario>> obtenerUsuarios() {
        List<UsuarioAlmacenamientoDTO> lista = restClient.get()
                .uri("/usuarios")
                .retrieve()
                .body(new ParameterizedTypeReference<List<UsuarioAlmacenamientoDTO>>() {
                });

        Map<String, Usuario> usuarios = new LinkedHashMap<>();
        if (lista != null) {
            for (UsuarioAlmacenamientoDTO dto : lista) {
                usuarios.put(dto.getIdTrabajador(), dtoAUsuario(dto));
            }
        }
        return Optional.of(usuarios);
    }

    // ------------------------------------------------------------------
    // FICHAJES
    // ------------------------------------------------------------------

    public void guardarFichaje(Fichaje fichaje) {
        restClient.post()
                .uri("/fichajes")
                .body(fichajeADTO(fichaje))
                .retrieve()
                .toBodilessEntity();
    }

    public List<Fichaje> obtenerFichajes(String idTrabajador, LocalDate dia) {
        List<FichajeAlmacenamientoDTO> lista = restClient.get()
                .uri("/fichajes/{idTrabajador}?dia={dia}", idTrabajador, dia)
                .retrieve()
                .body(new ParameterizedTypeReference<List<FichajeAlmacenamientoDTO>>() {
                });
        return aFichajes(lista);
    }

    public List<Fichaje> obtenerFichajesEntre(LocalDate inicio, LocalDate fin) {
        List<FichajeAlmacenamientoDTO> lista = restClient.get()
                .uri("/fichajes?inicio={inicio}&fin={fin}", inicio, fin)
                .retrieve()
                .body(new ParameterizedTypeReference<List<FichajeAlmacenamientoDTO>>() {
                });
        return aFichajes(lista);
    }

    // ------------------------------------------------------------------
    // CONVERSIONES entidad <-> DTO del almacenamiento
    // ------------------------------------------------------------------

    private UsuarioAlmacenamientoDTO usuarioADTO(Usuario u) {
        return new UsuarioAlmacenamientoDTO(u.getIdTrabajador(), u.getNombre(), u.getApellidos(), u.getDni(),
                u.getVectorBiometrico(), u.isEstado(), u.getFecAlta(), u.getFecBaja());
    }

    private Usuario dtoAUsuario(UsuarioAlmacenamientoDTO dto) {
        return new Usuario(dto.getNombre(), dto.getApellidos(), dto.getDni(), dto.getIdTrabajador(),
                dto.getVectorBiometrico(), dto.getFecAlta(), dto.getFecBaja(), dto.isEstado());
    }

    private FichajeAlmacenamientoDTO fichajeADTO(Fichaje f) {
        return new FichajeAlmacenamientoDTO(f.getIdTrabajador(), f.getIdEdificio(), f.getIdPuerta(),
                f.getTimestamp(), f.getTipo());
    }

    private List<Fichaje> aFichajes(List<FichajeAlmacenamientoDTO> lista) {
        if (lista == null) {
            return List.of();
        }
        return lista.stream()
                .map(dto -> new Fichaje(dto.getIdTrabajador(), dto.getIdEdificio(), dto.getIdPuerta(),
                        dto.getTimestamp(), dto.getTipo()))
                .toList();
    }
}
