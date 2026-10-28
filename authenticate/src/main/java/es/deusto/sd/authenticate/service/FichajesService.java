package es.deusto.sd.authenticate.service;
import es.deusto.sd.authenticate.access.AccesoDatosCentral;
import es.deusto.sd.authenticate.dto.FichajeDTO;
import es.deusto.sd.authenticate.entity.Usuario;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class FichajesService {
    private final AccesoDatosCentral accesoDatos;

    public FichajesService(AccesoDatosCentral accesoDatos) {
        this.accesoDatos = accesoDatos;
    }

    public boolean registrarFichaje(FichajeDTO fichaje){
        Optional<Usuario> usuario = accesoDatos.obtenerUsuario(fichaje.getIdTrabajador());
        if(usuario.isPresent()){
            localDateTime
        }
        return false;
    }
}
