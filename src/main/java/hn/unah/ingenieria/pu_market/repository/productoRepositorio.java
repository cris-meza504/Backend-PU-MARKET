package hn.unah.ingenieria.pu_market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hn.unah.ingenieria.pu_market.entity.Producto;

@Repository
public interface productoRepositorio extends JpaRepository<Producto, Integer>{
     // Listar solo los activos
    List<Producto> findByActivoTrue();
    // Buscar por vendedor
    List<Producto> findByVendedorIdAndActivoTrue(Integer vendedorId);
    // Buscar por categoría
    List<Producto> findByCategoriaIdAndActivoTrue(Integer categoriaId);
    //listar todos los productos menos los del usuario logiado 
    List<Producto> findByActivoTrueAndVendedorIdNot(Integer vendedorId);
}
