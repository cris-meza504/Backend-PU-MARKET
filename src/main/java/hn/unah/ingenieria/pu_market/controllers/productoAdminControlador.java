package hn.unah.ingenieria.pu_market.controllers;

import hn.unah.ingenieria.pu_market.entity.Producto;
import hn.unah.ingenieria.pu_market.service.productoServicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/productos")
@CrossOrigin(origins = "http://localhost:3000")
public class productoAdminControlador {

    @Autowired
    private productoServicio productoServicio;

    @GetMapping("/total")
    public long totalProductosDisponibles() {
        return productoServicio.contarProductosDisponibles();
    }

    // Todos los productos disponibles
    @GetMapping("/disponibles")
    public ResponseEntity<List<Producto>> productosDisponibles() {
        return ResponseEntity.ok(productoServicio.obtenerProductosDisponibles());
    }
}
