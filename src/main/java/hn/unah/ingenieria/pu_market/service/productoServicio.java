package hn.unah.ingenieria.pu_market.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.ingenieria.pu_market.entity.Producto;
import hn.unah.ingenieria.pu_market.repository.productoRepositorio;

@Service
public class productoServicio {
    
    @Autowired
    private productoRepositorio productoRepo;

    public Producto crear(Producto p) {
        p.setActivo(true);
        return productoRepo.save(p);
    }

    public Producto actualizar(Integer id, Producto pDatos) {
        Producto p = productoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        p.setNombre(pDatos.getNombre());
        p.setDescripcion(pDatos.getDescripcion());
        p.setPrecio(pDatos.getPrecio());
        p.setCategoria(pDatos.getCategoria());
        p.setActivo(pDatos.getActivo());
        return productoRepo.save(p);
    }

    public void eliminar(Integer id) {
        Producto p = productoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        p.setActivo(false);
        productoRepo.save(p);
    }

    public Producto obtenerPorId(Integer id) {
        return productoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public List<Producto> listarTodos() {
        return productoRepo.findByActivoTrue();
    }

    public List<Producto> listarPorVendedor(Integer vendedorId) {
        return productoRepo.findByVendedorIdAndActivoTrue(vendedorId);
    }

    public List<Producto> listarPorCategoria(Integer categoriaId) {
        return productoRepo.findByCategoriaIdAndActivoTrue(categoriaId);
    }
}
