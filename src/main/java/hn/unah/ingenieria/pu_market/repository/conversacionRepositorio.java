package hn.unah.ingenieria.pu_market.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hn.unah.ingenieria.pu_market.entity.Conversacion;
import hn.unah.ingenieria.pu_market.entity.Producto;
import hn.unah.ingenieria.pu_market.entity.Usuario;

@Repository
public interface conversacionRepositorio extends JpaRepository<Conversacion, Integer> {
    
    Optional<Conversacion> findByCompradorIdAndVendedorIdAndProductoId(Integer compradorId, Integer vendedorID, Integer productoId);

    Optional<Conversacion> findByCompradorAndVendedorAndProducto(Usuario comprador, Usuario vendedor, Producto producto);
    
     // Encuentra todas las conversaciones donde el usuario es comprador o vendedor
    List<Conversacion> findByComprador_IdOrVendedor_Id(Integer compradorId, Integer vendedorId);

    // Todas las conversaciones de un producto específico, filtradas por vendedor
    List<Conversacion> findByProducto_IdAndVendedor_Id(Integer productoId, Integer vendedorId);
}
