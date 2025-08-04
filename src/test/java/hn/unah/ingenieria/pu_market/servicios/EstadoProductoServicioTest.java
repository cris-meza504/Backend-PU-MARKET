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
    private estadoProductoRepositorio repo;

    @InjectMocks
    private estadoProductoServicio servicio;

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
        when(repo.findAll()).thenReturn(lista);

        List<EstadoProducto> result = servicio.listarEstados();

        assertEquals(1, result.size());
        assertEquals("Nuevo", result.get(0).getNombre());
    }

    @Test
    void buscarPorId_existente() {
        when(repo.findById(1)).thenReturn(Optional.of(estado));
        Optional<EstadoProducto> result = servicio.buscarPorId(1);

        assertTrue(result.isPresent());
        assertEquals("Nuevo", result.get().getNombre());
    }

    @Test
    void buscarPorId_noExistente() {
        when(repo.findById(99)).thenReturn(Optional.empty());
        Optional<EstadoProducto> result = servicio.buscarPorId(99);

        assertFalse(result.isPresent());
    }

    @Test
    void crear_estadoNuevo_exito() {
        when(repo.existsByNombre("Nuevo")).thenReturn(false);
        when(repo.save(any(EstadoProducto.class))).thenAnswer(i -> i.getArguments()[0]);

        EstadoProducto nuevo = new EstadoProducto();
        nuevo.setNombre("Nuevo");

        EstadoProducto result = servicio.crear(nuevo);
        assertEquals("Nuevo", result.getNombre());
        verify(repo).save(nuevo);
    }

    @Test
    void crear_estadoExistente_lanzaError() {
        when(repo.existsByNombre("Nuevo")).thenReturn(true);

        EstadoProducto nuevo = new EstadoProducto();
        nuevo.setNombre("Nuevo");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicio.crear(nuevo));
        assertEquals("Ya existe un estado con ese nombre", ex.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void actualizar_estado_exito() {
        EstadoProducto actual = new EstadoProducto();
        actual.setId(1);
        actual.setNombre("Viejo");

        EstadoProducto cambios = new EstadoProducto();
        cambios.setNombre("Actualizado");

        when(repo.findById(1)).thenReturn(Optional.of(actual));
        when(repo.existsByNombre("Actualizado")).thenReturn(false);
        when(repo.save(any(EstadoProducto.class))).thenAnswer(i -> i.getArguments()[0]);

        EstadoProducto result = servicio.actualizar(1, cambios);

        assertEquals("Actualizado", result.getNombre());
        verify(repo).save(actual);
    }

    @Test
    void actualizar_estado_nombreExistente_lanzaError() {
        EstadoProducto actual = new EstadoProducto();
        actual.setId(1);
        actual.setNombre("Viejo");

        EstadoProducto cambios = new EstadoProducto();
        cambios.setNombre("Existente");

        when(repo.findById(1)).thenReturn(Optional.of(actual));
        when(repo.existsByNombre("Existente")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicio.actualizar(1, cambios));
        assertEquals("Ya existe un estado con ese nombre", ex.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void actualizar_estado_noEncontrado_lanzaError() {
        EstadoProducto cambios = new EstadoProducto();
        cambios.setNombre("Cualquiera");

        when(repo.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicio.actualizar(99, cambios));
        assertEquals("Estado no encontrado", ex.getMessage());
    }

    @Test
    void eliminar_estado_exito() {
        when(repo.existsById(1)).thenReturn(true);
        doNothing().when(repo).deleteById(1);

        assertDoesNotThrow(() -> servicio.eliminar(1));
        verify(repo).deleteById(1);
    }

    @Test
    void eliminar_estado_noExistente_lanzaError() {
        when(repo.existsById(99)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicio.eliminar(99));
        assertEquals("Estado no encontrado", ex.getMessage());
        verify(repo, never()).deleteById(anyInt());
    }

    @Test
    void eliminar_estado_conProductosAsociados_lanzaError() {
        when(repo.existsById(1)).thenReturn(true);
        doThrow(new RuntimeException("Constraint")).when(repo).deleteById(1);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> servicio.eliminar(1));
        assertEquals("No puedes eliminar este estado porque hay productos asociados.", ex.getMessage());
    }
}
