package hn.unah.ingenieria.pu_market.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.ingenieria.pu_market.dto.VentasDTO;
import hn.unah.ingenieria.pu_market.entity.Producto;
import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.entity.Venta;
import hn.unah.ingenieria.pu_market.repository.productoRepositorio;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;
import hn.unah.ingenieria.pu_market.repository.ventaRepositorio;

@Service
public class ventaServicio {

    @Autowired
    private ventaRepositorio ventaRepo;

    @Autowired
    private productoRepositorio productoRepo;

    @Autowired
    private usuarioRepositorio usuarioRepo;

    public Venta registrarVenta(VentasDTO dto) {
        Producto producto = productoRepo.findById(dto.getIdProducto())
            .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + dto.getIdProducto()));
        Usuario vendedor = usuarioRepo.findById(dto.getIdVendedor())
            .orElseThrow(() -> new RuntimeException("Vendedor no encontrado: " + dto.getIdVendedor()));
        Usuario comprador = usuarioRepo.findById(dto.getIdComprador())
            .orElseThrow(() -> new RuntimeException("Comprador no encontrado: " + dto.getIdComprador()));

        if (Boolean.TRUE.equals(producto.getActivo())) {
            producto.setActivo(false);
            productoRepo.save(producto);
        }

        Venta venta = new Venta();
        venta.setProducto(producto);
        venta.setVendedor(vendedor);
        venta.setComprador(comprador);
        venta.setFechaVenta(new Date());
        venta.setPrecioVenta(dto.getPrecioVenta());

        return ventaRepo.save(venta);
    }

    public List<Venta> obtenerVentasPorVendedor(Integer idVendedor) {
        return ventaRepo.findByVendedorId(idVendedor);
    }

    public List<Venta> obtenerComprasPorComprador(Integer idComprador) {
        return ventaRepo.findByCompradorId(idComprador);
    }

    public int getVentasPorUsuario(Integer idUsuario) {
    return ventaRepo.countByVendedorId(idUsuario);
    }

    public int getComprasPorUsuario(Integer idUsuario) {
    return ventaRepo.countByCompradorId(idUsuario);
    }

    public List<Venta> obtenerTodasLasVentas() {
    return ventaRepo.findAll();
    }

    public long contarVentas() {
    return ventaRepo.count();
    }

}
