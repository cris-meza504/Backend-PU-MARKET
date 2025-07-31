package hn.unah.ingenieria.pu_market.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.ingenieria.pu_market.entity.Mensaje;
import hn.unah.ingenieria.pu_market.repository.mensajeRepositorio;

@Service
public class mensajeServicio {
    
    @Autowired
    private mensajeRepositorio repo;

    public Mensaje enviarMensaje(Mensaje mensaje) {
        mensaje.setFechaEnvio(LocalDateTime.now());
        return repo.save(mensaje);
    }

    public List<Mensaje> listarMensajesPorConversacion(Integer conversacionId) {
        return repo.findByConversacionIdOrderByFechaEnvioAsc(conversacionId);
    }
}
