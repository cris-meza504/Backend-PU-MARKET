package hn.unah.ingenieria.pu_market.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hn.unah.ingenieria.pu_market.entity.Mensaje;
import hn.unah.ingenieria.pu_market.service.mensajeServicio;

@RestController
@RequestMapping("/api/mensajes")
@CrossOrigin(origins = "http://localhost:3000")
public class mensajeControlador {
    @Autowired
    private mensajeServicio servicio;

    @PostMapping
    public Mensaje enviar(@RequestBody Mensaje m) {
        return servicio.enviarMensaje(m);
    }

    @GetMapping("/conversacion/{id}")
    public List<Mensaje> listarPorConversacion(@PathVariable Integer id) {
        return servicio.listarMensajesPorConversacion(id);
    }
}
