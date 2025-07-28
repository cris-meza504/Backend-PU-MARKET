package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.entity.EstadoProducto;
import hn.unah.ingenieria.pu_market.repository.estadoProductoRepositorio;
import hn.unah.ingenieria.pu_market.service.estadoProductoServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadoProductoServicioTest {

    @Mock
    private estadoProductoRepositorio estadoRepo;

    @InjectMocks
    private estadoProductoServicio estadoServicio;

    private EstadoProducto estado;

    @BeforeEach
    void setUp() {
        estado = new EstadoProducto();
        estado.setId(1);
        estado.setNombre("Nuevo");
    }

    @Test
    void listarEstados_debeRetornarLista() {
        List<EstadoProducto> lista = Arrays.asList(estado);
        when(estadoRepo.findAll()).thenReturn(lista);

        List<EstadoProducto> resultado = estadoServicio.listarEstados();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Nuevo", resultado.get(0).getNombre());
        verify(estadoRepo).findAll();
    }

    @Test
    void buscarPorId_encontrado() {
        when(estadoRepo.findById(1)).thenReturn(Optional.of(estado));

        Optional<EstadoProducto> encontrado = estadoServicio.buscarPorId(1);

        assertTrue(encontrado.isPresent());
        assertEquals("Nuevo", encontrado.get().getNombre());
        verify(estadoRepo).findById(1);
    }

    @Test
    void buscarPorId_noEncontrado() {
        when(estadoRepo.findById(99)).thenReturn(Optional.empty());

        Optional<EstadoProducto> encontrado = estadoServicio.buscarPorId(99);

        assertFalse(encontrado.isPresent());
        verify(estadoRepo).findById(99);
    }

    @Test
    void crear_debeGuardarEstado() {
        when(estadoRepo.save(estado)).thenReturn(estado);

        EstadoProducto creado = estadoServicio.crear(estado);

        assertNotNull(creado);
        assertEquals("Nuevo", creado.getNombre());
        verify(estadoRepo).save(estado);
    }

    @Test
    void actualizar_estadoExistente() {
        EstadoProducto nuevo = new EstadoProducto();
        nuevo.setNombre("Usado");

        when(estadoRepo.findById(1)).thenReturn(Optional.of(estado));
        when(estadoRepo.save(any(EstadoProducto.class))).thenAnswer(i -> i.getArgument(0));

        EstadoProducto actualizado = estadoServicio.actualizar(1, nuevo);

        assertEquals("Usado", actualizado.getNombre());
        verify(estadoRepo).findById(1);
        verify(estadoRepo).save(estado);
    }

    @Test
    void actualizar_estadoNoExistente() {
        when(estadoRepo.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                estadoServicio.actualizar(99, new EstadoProducto())
        );
        assertEquals("Estado no encontrado", ex.getMessage());
        verify(estadoRepo).findById(99);
    }

    @Test
    void eliminar_debeBorrarPorId() {
        doNothing().when(estadoRepo).deleteById(1);

        assertDoesNotThrow(() -> estadoServicio.eliminar(1));
        verify(estadoRepo).deleteById(1);
    }
}
