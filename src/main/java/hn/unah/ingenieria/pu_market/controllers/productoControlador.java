package hn.unah.ingenieria.pu_market.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hn.unah.ingenieria.pu_market.entity.Producto;
import hn.unah.ingenieria.pu_market.service.productoServicio;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:3000")
public class productoControlador {
    
    @Autowired
    private productoServicio productoServicio;

    @PostMapping
    public Producto crear(@RequestBody Producto p) {
        return productoServicio.crear(p);
    }

    @GetMapping("/{id}")
    public Producto getById(@PathVariable Integer id) {
        return productoServicio.obtenerPorId(id);
    }

    @GetMapping
    public List<Producto> listar() {
        return productoServicio.listarTodos();
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Integer id, @RequestBody Producto p) {
        return productoServicio.actualizar(id, p);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        productoServicio.eliminar(id);
    }

    @GetMapping("/vendedor/{vendedorId}")
    public List<Producto> porVendedor(@PathVariable Integer vendedorId) {
        return productoServicio.listarPorVendedor(vendedorId);
    }

    @GetMapping("/categoria/{categoriaId}")
    public List<Producto> porCategoria(@PathVariable Integer categoriaId) {
        return productoServicio.listarPorCategoria(categoriaId);
    }

}
