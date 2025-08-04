package hn.unah.ingenieria.pu_market.controllers;

import hn.unah.ingenieria.pu_market.entity.Venta;
import hn.unah.ingenieria.pu_market.service.ventaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/reportes")
@CrossOrigin(origins = "http://localhost:3000")
public class reporteAdminControlador {

    @Autowired
    private ventaServicio ventaServicio;

    // 1. Historial global de ventas (todas las ventas del marketplace)
    @GetMapping("/ventas")
    public ResponseEntity<List<Venta>> historialVentas() {
        return ResponseEntity.ok(ventaServicio.obtenerTodasLasVentas());
    }

    // 2. Total de ventas (contador general)
    @GetMapping("/total-ventas")
    public ResponseEntity<Long> totalVentas() {
        return ResponseEntity.ok(ventaServicio.contarVentas());
    }

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> resumenVentas() {
        List<Venta> ventas = ventaServicio.obtenerTodasLasVentas();
        Map<String, Object> res = new HashMap<>();
        res.put("ventas", ventas);
        res.put("totalVentas", ventas.size());
        return ResponseEntity.ok(res);
    }
}
