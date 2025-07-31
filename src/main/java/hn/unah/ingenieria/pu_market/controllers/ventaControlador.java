package hn.unah.ingenieria.pu_market.controllers;

import hn.unah.ingenieria.pu_market.dto.VentasDTO;
import hn.unah.ingenieria.pu_market.entity.Venta;
import hn.unah.ingenieria.pu_market.service.ventaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "http://localhost:3000")
public class ventaControlador {

    @Autowired
    private ventaServicio ventaServicio;

    // Registrar una venta
    @PostMapping
    public Venta registrarVenta(@RequestBody VentasDTO dto) {
        return ventaServicio.registrarVenta(dto);
    }

    // Historial ventas de un vendedor
    @GetMapping("/vendedor/{idVendedor}")
    public List<Venta> ventasPorVendedor(@PathVariable Integer idVendedor) {
        return ventaServicio.obtenerVentasPorVendedor(idVendedor);
    }

    // Historial compras de un comprador
    @GetMapping("/comprador/{idComprador}")
    public List<Venta> comprasPorComprador(@PathVariable Integer idComprador) {
        return ventaServicio.obtenerComprasPorComprador(idComprador);
    }
}
