package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.entity.Categoria;
import hn.unah.ingenieria.pu_market.service.categoriaServicio;
import hn.unah.ingenieria.pu_market.repository.categoriaRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServicioTest {

    @Mock
    private categoriaRepositorio categoriaRepo;

    @InjectMocks
    private categoriaServicio categoriaServicio;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Tecnología");
    }

    @Test
    void crear_categoria() {
        when(categoriaRepo.save(any(Categoria.class))).thenReturn(categoria);

        Categoria creada = categoriaServicio.crear(new Categoria());

        assertNotNull(creada);
        assertEquals("Tecnología", creada.getNombre());
        verify(categoriaRepo).save(any(Categoria.class));
    }

    @Test
    void actualizar_categoria_existente() {
        Categoria datosActualizados = new Categoria();
        datosActualizados.setNombre("Electrónica");

        when(categoriaRepo.findById(1)).thenReturn(Optional.of(categoria));
        when(categoriaRepo.save(any(Categoria.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Categoria actualizada = categoriaServicio.actualizar(1, datosActualizados);

        assertEquals("Electrónica", actualizada.getNombre());
        verify(categoriaRepo).findById(1);
        verify(categoriaRepo).save(categoria);
    }

    @Test
    void actualizar_categoria_noEncontrada() {
        when(categoriaRepo.findById(2)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                categoriaServicio.actualizar(2, new Categoria())
        );
        assertEquals("Categoría no encontrada", ex.getMessage());
    }

    @Test
    void eliminar_categoria_existente() {
        when(categoriaRepo.existsById(1)).thenReturn(true);
        doNothing().when(categoriaRepo).deleteById(1);

        categoriaServicio.eliminar(1);

        verify(categoriaRepo).existsById(1);
        verify(categoriaRepo).deleteById(1);
    }

    @Test
    void eliminar_categoria_noEncontrada() {
        when(categoriaRepo.existsById(2)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                categoriaServicio.eliminar(2)
        );
        assertEquals("Categoría no encontrada", ex.getMessage());
    }

    @Test
    void obtenerPorId_existente() {
        when(categoriaRepo.findById(1)).thenReturn(Optional.of(categoria));

        Categoria encontrada = categoriaServicio.obtenerPorId(1);

        assertNotNull(encontrada);
        assertEquals("Tecnología", encontrada.getNombre());
    }

    @Test
    void obtenerPorId_noEncontrada() {
        when(categoriaRepo.findById(2)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                categoriaServicio.obtenerPorId(2)
        );
        assertEquals("Categoría no encontrada", ex.getMessage());
    }

    @Test
    void listarTodas_categorias() {
        List<Categoria> lista = Arrays.asList(
                categoria,
                new Categoria() {{
                    setId(2);
                    setNombre("Ropa");
                }}
        );

        when(categoriaRepo.findAll()).thenReturn(lista);

        List<Categoria> resultado = categoriaServicio.listarTodas();

        assertEquals(2, resultado.size());
        verify(categoriaRepo).findAll();
    }
}
