package hn.unah.ingenieria.pu_market.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import hn.unah.ingenieria.pu_market.entity.Resena;
import hn.unah.ingenieria.pu_market.service.resenaServicio;

@RestController
@RequestMapping("/api/resenas")
@CrossOrigin(origins = "http://localhost:3000")
public class resenaControlador {
    
    @Autowired
    private resenaServicio resenaServicio;

    @PostMapping
    public Resena crear(@RequestBody Resena r) {
        // if para que un mismo usuario no pueda hacer su propia reseña
        if (r.getVendedor().getId().equals(r.getResenador().getId())) {
            throw new RuntimeException("No puedes dejar una reseña a ti mismo.");
        }
        return resenaServicio.crear(r);
    }

    @GetMapping("/vendedor/{correo}")
    public List<Resena> listarPorVendedor(@PathVariable String correo) {
        return resenaServicio.listarPorVendedorCorreo(correo);
    }
}
