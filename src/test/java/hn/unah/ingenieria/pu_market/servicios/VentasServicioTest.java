package hn.unah.ingenieria.pu_market.servicios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import hn.unah.ingenieria.pu_market.dto.VentasDTO;
import hn.unah.ingenieria.pu_market.entity.Producto;
import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.entity.Venta;
import hn.unah.ingenieria.pu_market.repository.productoRepositorio;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;
import hn.unah.ingenieria.pu_market.repository.ventaRepositorio;
import hn.unah.ingenieria.pu_market.service.ventaServicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VentaServicioTest {

    @Mock
    private ventaRepositorio ventaRepo;

    @Mock
    private productoRepositorio productoRepo;

    @Mock
    private usuarioRepositorio usuarioRepo;

    @InjectMocks
    private ventaServicio ventaServicio;

    private Producto producto;
    private Usuario vendedor;
    private Usuario comprador;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1);
        producto.setActivo(true);

        vendedor = new Usuario();
        vendedor.setId(100);
        vendedor.setNombre("Juan");

        comprador = new Usuario();
        comprador.setId(200);
        comprador.setNombre("Ana");
    }

    // -----------------------------
    // REGISTRAR VENTA - ÉXITO
    // -----------------------------
    @Test
    void registrarVenta_exito() {
        VentasDTO dto = new VentasDTO();
        dto.setIdProducto(1);
        dto.setIdVendedor(100);
        dto.setIdComprador(200);
        dto.setPrecioVenta(150.0);

        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));
        when(usuarioRepo.findById(100)).thenReturn(Optional.of(vendedor));
        when(usuarioRepo.findById(200)).thenReturn(Optional.of(comprador));
        when(productoRepo.save(any(Producto.class))).thenReturn(producto);
        when(ventaRepo.save(any(Venta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Venta venta = ventaServicio.registrarVenta(dto);

        assertNotNull(venta);
        assertEquals(vendedor, venta.getVendedor());
        assertEquals(comprador, venta.getComprador());
        assertEquals(producto, venta.getProducto());
        assertEquals(150.0, venta.getPrecioVenta());
        assertFalse(producto.getActivo());

        verify(productoRepo).save(producto);
        verify(ventaRepo).save(any(Venta.class));
    }

    // -----------------------------
    // REGISTRAR VENTA - PRODUCTO NO EXISTE
    // -----------------------------
    @Test
    void registrarVenta_productoNoEncontrado() {
        VentasDTO dto = new VentasDTO();
        dto.setIdProducto(999);

        when(productoRepo.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ventaServicio.registrarVenta(dto);
        });

        assertTrue(ex.getMessage().contains("Producto no encontrado"));
    }

    // -----------------------------
    // REGISTRAR VENTA - VENDEDOR NO EXISTE
    // -----------------------------
    @Test
    void registrarVenta_vendedorNoEncontrado() {
        VentasDTO dto = new VentasDTO();
        dto.setIdProducto(1);
        dto.setIdVendedor(999);
        dto.setIdComprador(200);

        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));
        when(usuarioRepo.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ventaServicio.registrarVenta(dto);
        });

        assertTrue(ex.getMessage().contains("Vendedor no encontrado"));
    }

    // -----------------------------
    // REGISTRAR VENTA - COMPRADOR NO EXISTE
    // -----------------------------
    @Test
    void registrarVenta_compradorNoEncontrado() {
        VentasDTO dto = new VentasDTO();
        dto.setIdProducto(1);
        dto.setIdVendedor(100);
        dto.setIdComprador(999);

        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));
        when(usuarioRepo.findById(100)).thenReturn(Optional.of(vendedor));
        when(usuarioRepo.findById(999)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            ventaServicio.registrarVenta(dto);
        });

        assertTrue(ex.getMessage().contains("Comprador no encontrado"));
    }

    // -----------------------------
    // OBTENER VENTAS POR VENDEDOR
    // -----------------------------
    @Test
    void obtenerVentasPorVendedor_exito() {
        Venta venta = new Venta();
        venta.setVendedor(vendedor);

        when(ventaRepo.findByVendedorId(100)).thenReturn(List.of(venta));

        List<Venta> ventas = ventaServicio.obtenerVentasPorVendedor(100);

        assertEquals(1, ventas.size());
        verify(ventaRepo).findByVendedorId(100);
    }

    // -----------------------------
    // OBTENER COMPRAS POR COMPRADOR
    // -----------------------------
    @Test
    void obtenerComprasPorComprador_exito() {
        Venta venta = new Venta();
        venta.setComprador(comprador);

        when(ventaRepo.findByCompradorId(200)).thenReturn(List.of(venta));

        List<Venta> compras = ventaServicio.obtenerComprasPorComprador(200);

        assertEquals(1, compras.size());
        verify(ventaRepo).findByCompradorId(200);
    }
}
