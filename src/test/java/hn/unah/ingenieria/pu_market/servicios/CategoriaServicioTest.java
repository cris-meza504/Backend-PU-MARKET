package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.entity.Categoria;
import hn.unah.ingenieria.pu_market.repository.categoriaRepositorio;
import hn.unah.ingenieria.pu_market.service.categoriaServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServicioTest {

    @Mock
    private categoriaRepositorio repo;

    @InjectMocks
    private categoriaServicio servicio;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Electrónica");
    }

    @Test
    void crear_categoriaNueva_exito() {
        when(repo.existsByNombre("Electrónica")).thenReturn(false);
        when(repo.save(any(Categoria.class))).thenAnswer(i -> i.getArguments()[0]);
        Categoria c = new Categoria();
        c.setNombre("Electrónica");

        Categoria result = servicio.crear(c);

        assertEquals("Electrónica", result.getNombre());
        verify(repo).save(c);
    }

    @Test
    void crear_categoriaDuplicada_lanzaError() {
        when(repo.existsByNombre("Electrónica")).thenReturn(true);
        Categoria c = new Categoria();
        c.setNombre("Electrónica");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicio.crear(c));
        assertEquals("Ya existe una categoría con ese nombre", ex.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void actualizar_categoria_exito() {
        Categoria existente = new Categoria();
        existente.setId(1);
        existente.setNombre("Ropa");
        Categoria cambios = new Categoria();
        cambios.setNombre("Libros");

        when(repo.findById(1)).thenReturn(Optional.of(existente));
        when(repo.existsByNombre("Libros")).thenReturn(false);
        when(repo.save(any(Categoria.class))).thenAnswer(i -> i.getArguments()[0]);

        Categoria result = servicio.actualizar(1, cambios);

        assertEquals("Libros", result.getNombre());
        verify(repo).save(existente);
    }

    @Test
    void actualizar_categoria_nombreDuplicado_lanzaError() {
        Categoria existente = new Categoria();
        existente.setId(1);
        existente.setNombre("Electrodomésticos");
        Categoria cambios = new Categoria();
        cambios.setNombre("Ropa");

        when(repo.findById(1)).thenReturn(Optional.of(existente));
        when(repo.existsByNombre("Ropa")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicio.actualizar(1, cambios));
        assertEquals("Ya existe una categoría con ese nombre", ex.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void actualizar_categoria_noEncontrada_lanzaError() {
        when(repo.findById(99)).thenReturn(Optional.empty());
        Categoria cambios = new Categoria();
        cambios.setNombre("Cualquiera");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicio.actualizar(99, cambios));
        assertEquals("Categoría no encontrada", ex.getMessage());
    }

    @Test
    void eliminar_categoria_exito() {
        when(repo.existsById(1)).thenReturn(true);
        doNothing().when(repo).deleteById(1);

        assertDoesNotThrow(() -> servicio.eliminar(1));
        verify(repo).deleteById(1);
    }

    @Test
    void eliminar_categoria_noExistente_lanzaError() {
        when(repo.existsById(99)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicio.eliminar(99));
        assertEquals("Categoría no encontrada", ex.getMessage());
        verify(repo, never()).deleteById(anyInt());
    }

    @Test
    void eliminar_categoria_conProductosAsociados_lanzaError() {
        when(repo.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Constraint")).when(repo).deleteById(1);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicio.eliminar(1));
        assertEquals("No puedes eliminar esta categoría porque hay productos asociados.", ex.getMessage());
    }

    @Test
    void obtenerPorId_existente() {
        when(repo.findById(1)).thenReturn(Optional.of(categoria));
        Categoria result = servicio.obtenerPorId(1);

        assertNotNull(result);
        assertEquals("Electrónica", result.getNombre());
    }

    @Test
    void obtenerPorId_noExistente_lanzaError() {
        when(repo.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicio.obtenerPorId(99));
        assertEquals("Categoría no encontrada", ex.getMessage());
    }

    @Test
    void listarTodas_devuelveLista() {
        List<Categoria> lista = Arrays.asList(categoria);
        when(repo.findAll()).thenReturn(lista);

        List<Categoria> result = servicio.listarTodas();

        assertEquals(1, result.size());
        assertEquals("Electrónica", result.get(0).getNombre());
    }
}
