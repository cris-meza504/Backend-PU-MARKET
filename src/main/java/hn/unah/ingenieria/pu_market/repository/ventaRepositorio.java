package hn.unah.ingenieria.pu_market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hn.unah.ingenieria.pu_market.entity.Venta;

@Repository
public interface ventaRepositorio extends JpaRepository< Venta, Integer>{
    List<Venta> findByVendedorId(Integer idVendedor);
    List<Venta> findByCompradorId(Integer idComprador);
    List<Venta> findByProductoId(Integer idProducto);
}
