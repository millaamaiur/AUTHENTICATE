package es.deusto.sd.authenticate.access;

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

    public Map<String, Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Fichaje> getFichajes() {
        return fichajes;
    }
}