package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.dto.ProductoConImagenesDTO;
import hn.unah.ingenieria.pu_market.entity.ImagenProducto;
import hn.unah.ingenieria.pu_market.entity.Producto;
import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.repository.imagenproductoRepositorio;
import hn.unah.ingenieria.pu_market.repository.productoRepositorio;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;
import hn.unah.ingenieria.pu_market.service.productoServicio;
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
    private usuarioRepositorio usuarioRepo;

    @Mock
    private productoRepositorio productoRepo;

    @Mock
    private imagenproductoRepositorio imagenRepo;

    @InjectMocks
    private productoServicio productoServicio;

    private Producto producto;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Vendedor");

        producto = new Producto();
        producto.setId(100);
        producto.setNombre("Producto de prueba");
        producto.setDescripcion("Descripción de prueba");
        producto.setPrecio(123.45);
        producto.setActivo(true);
        producto.setVendedor(usuario);
    }

    @Test
    void crear_guardarProductoYAsociarImagenes() {
        ImagenProducto img = new ImagenProducto();
        img.setUrlImagen("url1");
        List<ImagenProducto> imagenes = new ArrayList<>();
        imagenes.add(img);
        producto.setImagenes(imagenes);

        when(productoRepo.save(any(Producto.class))).thenAnswer(i -> i.getArguments()[0]);

        Producto result = productoServicio.crear(producto);

        assertTrue(result.getActivo());
        assertEquals(1, result.getImagenes().size());
        assertEquals(result, result.getImagenes().get(0).getProducto());
        verify(productoRepo).save(producto);
    }

    @Test
    void crearProductoConImagenes_guardaProductoYAsociaImagenes() {
        ProductoConImagenesDTO dto = new ProductoConImagenesDTO();
        Producto prod = new Producto();
        prod.setNombre("Test");
        prod.setActivo(false);
        dto.setProducto(prod);
        dto.setImagenes(List.of("url1", "url2"));

        when(productoRepo.save(any(Producto.class))).thenAnswer(i -> {
            Producto p = (Producto) i.getArguments()[0];
            p.setId(99);
            return p;
        });
        when(imagenRepo.save(any(ImagenProducto.class))).thenAnswer(i -> i.getArguments()[0]);

        Producto result = productoServicio.crearProductoConImagenes(dto);

        assertTrue(result.getActivo());
        assertEquals(2, result.getImagenes().size());
        assertEquals("url1", result.getImagenes().get(0).getUrlImagen());
        assertEquals("url2", result.getImagenes().get(1).getUrlImagen());
        verify(productoRepo, times(1)).save(any(Producto.class));
        verify(imagenRepo, times(2)).save(any(ImagenProducto.class));
    }

    @Test
    void actualizar_productoExistente_actualizaDatos() {
        Producto nuevosDatos = new Producto();
        nuevosDatos.setNombre("Nuevo nombre");
        nuevosDatos.setDescripcion("Nueva desc");
        nuevosDatos.setPrecio(77.0);

        when(productoRepo.findById(producto.getId())).thenReturn(Optional.of(producto));
        when(productoRepo.save(any(Producto.class))).thenAnswer(i -> i.getArguments()[0]);

        Producto actualizado = productoServicio.actualizar(producto.getId(), nuevosDatos);

        assertEquals("Nuevo nombre", actualizado.getNombre());
        assertEquals("Nueva desc", actualizado.getDescripcion());
        assertEquals(77.0, actualizado.getPrecio());
    }

    @Test
    void actualizar_productoNoExiste_lanzaExcepcion() {
        when(productoRepo.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> productoServicio.actualizar(99, new Producto()));
    }

    @Test
    void eliminar_productoExistente_elimina() {
        when(productoRepo.existsById(producto.getId())).thenReturn(true);

        productoServicio.eliminar(producto.getId());
        verify(productoRepo).deleteById(producto.getId());
    }

    @Test
    void eliminar_productoNoExiste_lanzaExcepcion() {
        when(productoRepo.existsById(anyInt())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> productoServicio.eliminar(999));
    }

    @Test
    void obtenerPorId_productoExiste_retornaProducto() {
        when(productoRepo.findById(producto.getId())).thenReturn(Optional.of(producto));
        Producto result = productoServicio.obtenerPorId(producto.getId());
        assertEquals(producto, result);
    }

    @Test
    void obtenerPorId_productoNoExiste_lanzaExcepcion() {
        when(productoRepo.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productoServicio.obtenerPorId(987));
    }

    @Test
    void listarTodos_retornaListaActivos() {
        List<Producto> productos = Arrays.asList(new Producto(), new Producto());
        when(productoRepo.findByActivoTrue()).thenReturn(productos);

        List<Producto> result = productoServicio.listarTodos();
        assertEquals(2, result.size());
    }

    @Test
    void listarPorVendedorCorreo_existente() {
        when(usuarioRepo.findByCorreoInstitucional("mail@mail.com")).thenReturn(Optional.of(usuario));
        List<Producto> lista = Arrays.asList(new Producto(), new Producto());
        when(productoRepo.findByVendedorIdAndActivoTrue(usuario.getId())).thenReturn(lista);

        List<Producto> result = productoServicio.listarPorVendedorCorreo("mail@mail.com");
        assertEquals(2, result.size());
    }

    @Test
    void listarPorVendedorCorreo_noExiste() {
        when(usuarioRepo.findByCorreoInstitucional("mail@mail.com")).thenReturn(Optional.empty());
        List<Producto> result = productoServicio.listarPorVendedorCorreo("mail@mail.com");
        assertTrue(result.isEmpty());
    }

    @Test
    void contarProductos_disponibles() {
        when(productoRepo.count()).thenReturn(9L);
        when(productoRepo.countByActivoTrue()).thenReturn(6L);
        assertEquals(9, productoServicio.contarProductos());
        assertEquals(6, productoServicio.contarProductosDisponibles());
    }

    @Test
    void obtenerProductosDisponibles_llamaRepo() {
        List<Producto> productos = Arrays.asList(new Producto(), new Producto());
        when(productoRepo.findByActivoTrue()).thenReturn(productos);

        List<Producto> result = productoServicio.obtenerProductosDisponibles();
        assertEquals(2, result.size());
        verify(productoRepo).findByActivoTrue();
    }
}
