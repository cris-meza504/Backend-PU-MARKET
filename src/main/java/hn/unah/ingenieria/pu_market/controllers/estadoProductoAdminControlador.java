package hn.unah.ingenieria.pu_market.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import hn.unah.ingenieria.pu_market.entity.EstadoProducto;
import hn.unah.ingenieria.pu_market.service.estadoProductoServicio;

@RestController
@RequestMapping("/api/admin/estados-producto")
@CrossOrigin(origins = "http://localhost:3000")
public class estadoProductoAdminControlador {

    @Autowired
    private estadoProductoServicio estadoProductoService;

    @GetMapping
    public ResponseEntity<List<EstadoProducto>> listarEstados() {
        return ResponseEntity.ok(estadoProductoService.listarEstados());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(
                estadoProductoService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Estado no encontrado"))
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crearEstado(@RequestBody EstadoProducto estado) {
        try {
            EstadoProducto nuevo = estadoProductoService.crear(estado);
            return ResponseEntity.ok(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstado(@PathVariable Integer id, @RequestBody EstadoProducto estado) {
        try {
            EstadoProducto actualizado = estadoProductoService.actualizar(id, estado);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstado(@PathVariable Integer id) {
        try {
            estadoProductoService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
