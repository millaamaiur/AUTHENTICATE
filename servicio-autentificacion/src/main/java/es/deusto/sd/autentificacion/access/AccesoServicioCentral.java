package es.deusto.sd.autentificacion.access;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import es.deusto.sd.autentificacion.dto.FichajeCentralDTO;

@Repository
public class AccesoServicioCentral {

    private final String centralUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccesoServicioCentral(@Value("${central.url}") String centralUrl) {
        this.centralUrl = centralUrl;
    }

    public boolean registrarFichaje(FichajeCentralDTO fichaje) {

        try {
            ResponseEntity<String> respuesta = restTemplate.postForEntity(
                    centralUrl + "/fichajes",
                    fichaje,
                    String.class);

            String cuerpo = respuesta.getBody();

            if (cuerpo != null && !cuerpo.isBlank()) {
                return Boolean.parseBoolean(cuerpo.trim());
            }

            return respuesta.getStatusCode().is2xxSuccessful();

        } catch (RestClientException e) {
            System.err.println(" # AccesoServicioCentral: REST error: " + e.getMessage());
            return false;
        }
    }
}
