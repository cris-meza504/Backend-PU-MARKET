package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.dto.ProductoConImagenesDTO;
import hn.unah.ingenieria.pu_market.entity.Categoria;
import hn.unah.ingenieria.pu_market.entity.ImagenProducto;
import hn.unah.ingenieria.pu_market.entity.Producto;
import hn.unah.ingenieria.pu_market.service.productoServicio;
import hn.unah.ingenieria.pu_market.repository.imagenproductoRepositorio;
import hn.unah.ingenieria.pu_market.repository.productoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServicioTest {

    @Mock
    private productoRepositorio productoRepo;

    @Mock
    private imagenproductoRepositorio imagenRepo;

    @InjectMocks
    private productoServicio productoServicio;

    private Producto producto;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Tecnología");

        producto = new Producto();
        producto.setId(1);
        producto.setNombre("Laptop");
        producto.setDescripcion("Laptop gamer");
        producto.setPrecio(15000.0);
        producto.setActivo(true);
        producto.setCategoria(categoria);
        producto.setImagenes(new ArrayList<>());
    }

    // ------------------------
    // CREAR PRODUCTO (Entidad)
    // ------------------------
    @Test
    void crear_producto_exito() {
        when(productoRepo.save(any(Producto.class))).thenReturn(producto);

        Producto creado = productoServicio.crear(producto);

        assertNotNull(creado);
        assertTrue(creado.getActivo());
        verify(productoRepo).save(producto);
    }

    // ------------------------
    // CREAR PRODUCTO (DTO)
    // ------------------------
    @Test
    void crearProductoConImagenes_exito() {
        ProductoConImagenesDTO dto = new ProductoConImagenesDTO();
        dto.setProducto(producto);
        dto.setImagenes(Arrays.asList("url1.png", "url2.png"));

        when(productoRepo.save(any(Producto.class))).thenReturn(producto);
        when(imagenRepo.save(any(ImagenProducto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Producto creado = productoServicio.crearProductoConImagenes(dto);

        assertNotNull(creado);
        assertEquals(2, creado.getImagenes().size());
        verify(productoRepo, times(1)).save(producto);
        verify(imagenRepo, times(2)).save(any(ImagenProducto.class));
    }

    // ------------------------
    // ACTUALIZAR PRODUCTO
    // ------------------------
    @Test
    void actualizar_producto_exito() {
        Producto datos = new Producto();
        datos.setNombre("Laptop Pro");
        datos.setDescripcion("Mejor laptop");
        datos.setPrecio(20000.0);
        datos.setCategoria(categoria);
        datos.setActivo(true);

        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepo.save(any(Producto.class))).thenReturn(producto);

        Producto actualizado = productoServicio.actualizar(1, datos);

        assertEquals("Laptop Pro", actualizado.getNombre());
        verify(productoRepo).findById(1);
        verify(productoRepo).save(producto);
    }

    @Test
    void actualizar_producto_noEncontrado() {
        when(productoRepo.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                productoServicio.actualizar(99, new Producto())
        );
        assertEquals("Producto no encontrado", ex.getMessage());
    }

    // ------------------------
    // ELIMINAR (lógico)
    // ------------------------
    @Test
    void eliminar_producto_exito() {
        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepo.save(any(Producto.class))).thenReturn(producto);

        productoServicio.eliminar(1);

        assertFalse(producto.getActivo());
        verify(productoRepo).save(producto);
    }

    @Test
    void eliminar_producto_noEncontrado() {
        when(productoRepo.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                productoServicio.eliminar(99)
        );
        assertEquals("Producto no encontrado", ex.getMessage());
    }

    // ------------------------
    // OBTENER POR ID
    // ------------------------
    @Test
    void obtenerPorId_exito() {
        when(productoRepo.findById(1)).thenReturn(Optional.of(producto));

        Producto encontrado = productoServicio.obtenerPorId(1);

        assertNotNull(encontrado);
        assertEquals("Laptop", encontrado.getNombre());
    }

    @Test
    void obtenerPorId_noEncontrado() {
        when(productoRepo.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                productoServicio.obtenerPorId(99)
        );
        assertEquals("Producto no encontrado", ex.getMessage());
    }

    // ------------------------
    // LISTAR
    // ------------------------
    @Test
    void listarTodos_exito() {
        when(productoRepo.findByActivoTrue()).thenReturn(Arrays.asList(producto));

        List<Producto> lista = productoServicio.listarTodos();

        assertEquals(1, lista.size());
        verify(productoRepo).findByActivoTrue();
    }

    @Test
    void listarTodosExcluyendoUsuarioLogueado_exito() {
        when(productoRepo.findByActivoTrueAndVendedorIdNot(5)).thenReturn(Arrays.asList(producto));

        List<Producto> lista = productoServicio.listarTodosExcluyendoUsuarioLogueado(5);

        assertEquals(1, lista.size());
        verify(productoRepo).findByActivoTrueAndVendedorIdNot(5);
    }

    @Test
    void listarPorVendedor_exito() {
        when(productoRepo.findByVendedorIdAndActivoTrue(3)).thenReturn(Arrays.asList(producto));

        List<Producto> lista = productoServicio.listarPorVendedor(3);

        assertEquals(1, lista.size());
        verify(productoRepo).findByVendedorIdAndActivoTrue(3);
    }

    @Test
    void listarPorCategoria_exito() {
        when(productoRepo.findByCategoriaIdAndActivoTrue(1)).thenReturn(Arrays.asList(producto));

        List<Producto> lista = productoServicio.listarPorCategoria(1);

        assertEquals(1, lista.size());
        verify(productoRepo).findByCategoriaIdAndActivoTrue(1);
    }
}
