package hn.unah.ingenieria.pu_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hn.unah.ingenieria.pu_market.entity.EstadoProducto;

@Repository
public interface estadoProductoRepositorio extends JpaRepository<EstadoProducto, Integer>{
    
}
