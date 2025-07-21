package hn.unah.ingenieria.pu_market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.ingenieria.pu_market.entity.ImagenProducto;
import hn.unah.ingenieria.pu_market.entity.Producto;

public interface imagenproductoRepositorio extends JpaRepository<ImagenProducto, Integer>{
    
    List<ImagenProducto> findByProducto(Producto producto);

    List<ImagenProducto> findByProductoId(Integer productoId);

}
