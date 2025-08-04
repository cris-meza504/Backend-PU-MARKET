package hn.unah.ingenieria.pu_market.servicios;

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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private VentasDTO ventasDTO;

    @BeforeEach
    void setUp() {
        producto = new Producto();
        producto.setId(1);
        producto.setActivo(true);

        vendedor = new Usuario();
        vendedor.setId(10);

        comprador = new Usuario();
        comprador.setId(20);

        ventasDTO = new VentasDTO();
        ventasDTO.setIdProducto(1);
        ventasDTO.setIdVendedor(10);
        ventasDTO.setIdComprador(20);
        ventasDTO.setPrecioVenta(500.0);
    }

    @Test
    void registrarVenta_exito() {
        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));
        when(usuarioRepo.findById(10)).thenReturn(Optional.of(vendedor));
        when(usuarioRepo.findById(20)).thenReturn(Optional.of(comprador));
        when(productoRepo.save(any(Producto.class))).thenAnswer(i -> i.getArguments()[0]);
        when(ventaRepo.save(any(Venta.class))).thenAnswer(i -> {
            Venta v = (Venta) i.getArguments()[0];
            v.setIdVenta(1);
            return v;
        });

        Venta venta = ventaServicio.registrarVenta(ventasDTO);

        assertNotNull(venta);
        assertEquals(producto, venta.getProducto());
        assertEquals(vendedor, venta.getVendedor());
        assertEquals(comprador, venta.getComprador());
        assertEquals(500.0, venta.getPrecioVenta());
        assertFalse(producto.getActivo()); // Producto se desactiva
        verify(productoRepo).save(producto);
        verify(ventaRepo).save(any(Venta.class));
    }

    @Test
    void registrarVenta_productoNoEncontrado() {
        when(productoRepo.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                ventaServicio.registrarVenta(ventasDTO)
        );
        assertTrue(ex.getMessage().contains("Producto no encontrado"));
        verify(productoRepo, never()).save(any());
        verify(ventaRepo, never()).save(any());
    }

    @Test
    void registrarVenta_vendedorNoEncontrado() {
        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));
        when(usuarioRepo.findById(10)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                ventaServicio.registrarVenta(ventasDTO)
        );
        assertTrue(ex.getMessage().contains("Vendedor no encontrado"));
        verify(ventaRepo, never()).save(any());
    }

    @Test
    void registrarVenta_compradorNoEncontrado() {
        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));
        when(usuarioRepo.findById(10)).thenReturn(Optional.of(vendedor));
        when(usuarioRepo.findById(20)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                ventaServicio.registrarVenta(ventasDTO)
        );
        assertTrue(ex.getMessage().contains("Comprador no encontrado"));
        verify(ventaRepo, never()).save(any());
    }

    @Test
    void obtenerVentasPorVendedor() {
        List<Venta> ventas = Arrays.asList(new Venta(), new Venta());
        when(ventaRepo.findByVendedorId(10)).thenReturn(ventas);

        List<Venta> resultado = ventaServicio.obtenerVentasPorVendedor(10);

        assertEquals(2, resultado.size());
        verify(ventaRepo).findByVendedorId(10);
    }

    @Test
    void obtenerComprasPorComprador() {
        List<Venta> compras = Arrays.asList(new Venta());
        when(ventaRepo.findByCompradorId(20)).thenReturn(compras);

        List<Venta> resultado = ventaServicio.obtenerComprasPorComprador(20);

        assertEquals(1, resultado.size());
        verify(ventaRepo).findByCompradorId(20);
    }

    @Test
    void getVentasPorUsuario() {
        when(ventaRepo.countByVendedorId(10)).thenReturn(5);

        int ventas = ventaServicio.getVentasPorUsuario(10);

        assertEquals(5, ventas);
        verify(ventaRepo).countByVendedorId(10);
    }

    @Test
    void getComprasPorUsuario() {
        when(ventaRepo.countByCompradorId(20)).thenReturn(7);

        int compras = ventaServicio.getComprasPorUsuario(20);

        assertEquals(7, compras);
        verify(ventaRepo).countByCompradorId(20);
    }

    @Test
    void obtenerTodasLasVentas() {
        List<Venta> ventas = Arrays.asList(new Venta(), new Venta(), new Venta());
        when(ventaRepo.findAll()).thenReturn(ventas);

        List<Venta> resultado = ventaServicio.obtenerTodasLasVentas();

        assertEquals(3, resultado.size());
        verify(ventaRepo).findAll();
    }

    @Test
    void contarVentas() {
        when(ventaRepo.count()).thenReturn(99L);

        long total = ventaServicio.contarVentas();

        assertEquals(99L, total);
        verify(ventaRepo).count();
    }
}
