package hn.unah.ingenieria.pu_market.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.ingenieria.pu_market.entity.EstadoProducto;
import hn.unah.ingenieria.pu_market.repository.estadoProductoRepositorio;

@Service
public class estadoProductoServicio {
    
    @Autowired
    private estadoProductoRepositorio estadosProductoRepo;

    // Listar todos los estados
    public List<EstadoProducto> listarEstados() {
        return estadosProductoRepo.findAll();
    }

    // Buscar por id
    public Optional<EstadoProducto> buscarPorId(Integer id) {
        return estadosProductoRepo.findById(id);
    }

    // Crear un nuevo estado
    public EstadoProducto crear(EstadoProducto estado) {
        if (estadosProductoRepo.existsByNombre(estado.getNombre())) {
            throw new RuntimeException("Ya existe un estado con ese nombre");
        }
        return estadosProductoRepo.save(estado);
    }

    // Actualizar un estado existente
    public EstadoProducto actualizar(Integer id, EstadoProducto nuevoEstado) {
        EstadoProducto estado = estadosProductoRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
            if (estadosProductoRepo.existsByNombre(nuevoEstado.getNombre()) &&
            !estado.getNombre().equals(nuevoEstado.getNombre())) {
            throw new RuntimeException("Ya existe un estado con ese nombre");
        }
        estado.setNombre(nuevoEstado.getNombre());
        return estadosProductoRepo.save(estado);
    }

    // Eliminar estado por id
    public void eliminar(Integer id) {
        if (!estadosProductoRepo.existsById(id)) {
            throw new RuntimeException("Estado no encontrado");
        }
        try {
        estadosProductoRepo.deleteById(id);
    } catch (Exception e) {
        throw new RuntimeException("No puedes eliminar este estado porque hay productos asociados.");
    }
    }
}
