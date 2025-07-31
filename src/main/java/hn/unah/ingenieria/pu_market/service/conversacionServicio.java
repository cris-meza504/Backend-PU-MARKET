package hn.unah.ingenieria.pu_market.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import hn.unah.ingenieria.pu_market.entity.Conversacion;
import hn.unah.ingenieria.pu_market.entity.Producto;
import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.repository.conversacionRepositorio;
import hn.unah.ingenieria.pu_market.repository.productoRepositorio;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;

@Service
public class conversacionServicio {

    @Autowired
    private conversacionRepositorio conversacionRepo;
    @Autowired
    private usuarioRepositorio usuarioRepo;

    @Autowired
    private productoRepositorio productoRepo;

   public Conversacion crearOBuscar(Integer compradorId, String vendedorEmail, Integer productoId) {
    if (productoId == null) {
        throw new RuntimeException("Debes seleccionar un producto para iniciar la conversación.");
    }

    Usuario comprador = usuarioRepo.findById(compradorId)
        .orElseThrow(() -> new RuntimeException("Comprador no encontrado"));
    Usuario vendedor = usuarioRepo.findByCorreoInstitucional(vendedorEmail)
        .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

    if (comprador.getId().equals(vendedor.getId())) {
        throw new RuntimeException("No puedes iniciar conversación contigo mismo.");
    }

    Producto producto = productoRepo.findById(productoId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    // Busca (comprador, vendedor, producto)
    Optional<Conversacion> existente = conversacionRepo.findByCompradorAndVendedorAndProducto(comprador, vendedor, producto);
    // Si no existe, busca (vendedor, comprador, producto) (por si están invertidos)
    if (!existente.isPresent()) {
        existente = conversacionRepo.findByCompradorAndVendedorAndProducto(vendedor, comprador, producto);
    }

    if (existente.isPresent()) {
        return existente.get();
    }

    Conversacion nueva = new Conversacion();
    nueva.setComprador(comprador);
    nueva.setVendedor(vendedor);
    nueva.setProducto(producto);

    try {
        return conversacionRepo.save(nueva);
    } catch (DataIntegrityViolationException e) {
        // Si ocurre duplicidad, busca en ambos sentidos antes de lanzar excepción
        Optional<Conversacion> yaExiste = conversacionRepo.findByCompradorAndVendedorAndProducto(comprador, vendedor, producto);
        if (!yaExiste.isPresent()) {
            yaExiste = conversacionRepo.findByCompradorAndVendedorAndProducto(vendedor, comprador, producto);
        }
        if (yaExiste.isPresent()) {
            return yaExiste.get();
        }
        throw e; // Si es otro error, re-lanza
    }
}



    public List<Conversacion> listarPorUsuario(Integer usuarioId) {
        // Busca donde el usuario es comprador o vendedor
         return conversacionRepo.findByComprador_IdOrVendedor_Id(usuarioId, usuarioId);
    }

    public List<Conversacion> buscarPorProductoYVendedor(Integer productoId, Integer vendedorId) {
    return conversacionRepo.findByProducto_IdAndVendedor_Id(productoId, vendedorId);
}

}
