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
        if (categoriaRepo.existsByNombre(c.getNombre())) {
        throw new RuntimeException("Ya existe una categoría con ese nombre");
            }
        return categoriaRepo.save(c);
    }

    public Categoria actualizar(Integer id, Categoria datos) {
        Categoria c = categoriaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            if (categoriaRepo.existsByNombre(datos.getNombre()) && !c.getNombre().equals(datos.getNombre())) {
        throw new RuntimeException("Ya existe una categoría con ese nombre");
                }
        c.setNombre(datos.getNombre());
        return categoriaRepo.save(c);
    }

    public void eliminar(Integer id) {
        if (!categoriaRepo.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        try {
        categoriaRepo.deleteById(id);
    } catch (Exception e) {
        throw new RuntimeException("No puedes eliminar esta categoría porque hay productos asociados.");
    }
    }

    public Categoria obtenerPorId(Integer id) {
        return categoriaRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public List<Categoria> listarTodas() {
        return categoriaRepo.findAll();
    }
}
