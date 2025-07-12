package hn.unah.ingenieria.pu_market.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.ingenieria.pu_market.entity.Categoria;
import hn.unah.ingenieria.pu_market.repository.categoriaRepositorio;

@Service
public class categoriaServicio {
    
    @Autowired
    private categoriaRepositorio categoriaRepo;

    public Categoria crear(Categoria c) {
        return categoriaRepo.save(c);
    }

    public Categoria actualizar(Integer id, Categoria datos) {
        Categoria c = categoriaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        c.setNombre(datos.getNombre());
        return categoriaRepo.save(c);
    }

    public void eliminar(Integer id) {
        if (!categoriaRepo.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepo.deleteById(id);
    }

    public Categoria obtenerPorId(Integer id) {
        return categoriaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public List<Categoria> listarTodas() {
        return categoriaRepo.findAll();
    }
}
