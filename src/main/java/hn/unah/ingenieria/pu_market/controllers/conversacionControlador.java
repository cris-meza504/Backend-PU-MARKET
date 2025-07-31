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

import hn.unah.ingenieria.pu_market.dto.ConversacionRequestDTO;
import hn.unah.ingenieria.pu_market.entity.Conversacion;
import hn.unah.ingenieria.pu_market.service.conversacionServicio;

@RestController
@RequestMapping("/api/conversaciones")
@CrossOrigin(origins = "http://localhost:3000")
public class conversacionControlador {

    @Autowired
    private  conversacionServicio conversacionServicio;


    @PostMapping("/crear-o-buscar")
    public Conversacion crearOBuscarConversacion(@RequestBody ConversacionRequestDTO req) {
        return conversacionServicio.crearOBuscar(req.getCompradorId(), req.getVendedorEmail(), req.getProductoId());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Conversacion> listarConversacionesPorUsuario(@PathVariable Integer usuarioId) {
        return conversacionServicio.listarPorUsuario(usuarioId);
    }

    @GetMapping("/producto/{idProducto}/vendedor/{idVendedor}")
public List<Conversacion> getConversacionesDeProducto(
    @PathVariable Integer idProducto,
    @PathVariable Integer idVendedor
) {
    return conversacionServicio.buscarPorProductoYVendedor(idProducto, idVendedor);
}



}
