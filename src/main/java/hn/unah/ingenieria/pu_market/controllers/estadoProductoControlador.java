package hn.unah.ingenieria.pu_market.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import hn.unah.ingenieria.pu_market.entity.EstadoProducto;
import hn.unah.ingenieria.pu_market.service.estadoProductoServicio;

@RestController
@RequestMapping("/api/estadoproducto")
@CrossOrigin(origins = "http://localhost:3000")
public class estadoProductoControlador {

    @Autowired
    private estadoProductoServicio estadoProductoService;

    @GetMapping
    public List<EstadoProducto> listarEstados() {
        return estadoProductoService.listarEstados();
    }

    @GetMapping("/{id}")
    public Optional<EstadoProducto> obtenerPorId(@PathVariable Integer id) {
        return estadoProductoService.buscarPorId(id);
    }

    @PostMapping
    public EstadoProducto crearEstado(@RequestBody EstadoProducto estado) {
        return estadoProductoService.crear(estado);
    }

    @PutMapping("/{id}")
    public EstadoProducto actualizarEstado(@PathVariable Integer id, @RequestBody EstadoProducto estado) {
        return estadoProductoService.actualizar(id, estado);
    }

    @DeleteMapping("/{id}")
    public void eliminarEstado(@PathVariable Integer id) {
        estadoProductoService.eliminar(id);
    }
}
