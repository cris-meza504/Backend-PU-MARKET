package hn.unah.ingenieria.pu_market.servicios;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import hn.unah.ingenieria.pu_market.entity.Conversacion;
import hn.unah.ingenieria.pu_market.entity.Producto;
import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.repository.conversacionRepositorio;
import hn.unah.ingenieria.pu_market.repository.productoRepositorio;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;
import hn.unah.ingenieria.pu_market.service.conversacionServicio;

public class ConversacionServicioTest {

    @InjectMocks
    private conversacionServicio conversacionServicio;

    @Mock
    private conversacionRepositorio conversacionRepo;

    @Mock
    private usuarioRepositorio usuarioRepo;

    @Mock
    private productoRepositorio productoRepo;

    private Usuario comprador;
    private Usuario vendedor;
    private Producto producto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        comprador = new Usuario();
        comprador.setId(1);
        comprador.setCorreoInstitucional("comprador@unah.hn");

        vendedor = new Usuario();
        vendedor.setId(2);
        vendedor.setCorreoInstitucional("vendedor@unah.hn");

        producto = new Producto();
        producto.setId(10);
    }

    @Test
    void crearConversacionNueva_conExito() {
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(comprador));
        when(usuarioRepo.findByCorreoInstitucional("vendedor@unah.hn")).thenReturn(Optional.of(vendedor));
        when(productoRepo.findById(10)).thenReturn(Optional.of(producto));
        when(conversacionRepo.findByCompradorAndVendedorAndProducto(comprador, vendedor, producto))
                .thenReturn(Optional.empty());
        when(conversacionRepo.findByCompradorAndVendedorAndProducto(vendedor, comprador, producto))
                .thenReturn(Optional.empty());

        Conversacion conversacionGuardada = new Conversacion();
        conversacionGuardada.setId(1);
        when(conversacionRepo.save(any(Conversacion.class))).thenReturn(conversacionGuardada);

        Conversacion resultado = conversacionServicio.crearOBuscar(1, "vendedor@unah.hn", 10);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        verify(conversacionRepo, times(1)).save(any(Conversacion.class));
    }

    @Test
    void retornaConversacionExistente() {
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(comprador));
        when(usuarioRepo.findByCorreoInstitucional("vendedor@unah.hn")).thenReturn(Optional.of(vendedor));
        when(productoRepo.findById(10)).thenReturn(Optional.of(producto));

        Conversacion existente = new Conversacion();
        existente.setId(99);

        when(conversacionRepo.findByCompradorAndVendedorAndProducto(comprador, vendedor, producto))
                .thenReturn(Optional.of(existente));

        Conversacion resultado = conversacionServicio.crearOBuscar(1, "vendedor@unah.hn", 10);

        assertNotNull(resultado);
        assertEquals(99, resultado.getId());
        verify(conversacionRepo, never()).save(any());
    }

    @Test
    void lanzaExcepcionMismoUsuario() {
        vendedor.setId(1); // Igual al comprador

        when(usuarioRepo.findById(1)).thenReturn(Optional.of(comprador));
        when(usuarioRepo.findByCorreoInstitucional("vendedor@unah.hn")).thenReturn(Optional.of(vendedor));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                conversacionServicio.crearOBuscar(1, "vendedor@unah.hn", 10));

        assertEquals("No puedes iniciar conversación contigo mismo.", exception.getMessage());
    }

    @Test
    void lanzaExcepcionProductoNoEncontrado() {
        when(usuarioRepo.findById(1)).thenReturn(Optional.of(comprador));
        when(usuarioRepo.findByCorreoInstitucional("vendedor@unah.hn")).thenReturn(Optional.of(vendedor));
        when(productoRepo.findById(10)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                conversacionServicio.crearOBuscar(1, "vendedor@unah.hn", 10));

        assertEquals("Producto no encontrado", exception.getMessage());
    }

    @Test
    void lanzaExcepcionSiProductoEsNull() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                conversacionServicio.crearOBuscar(1, "vendedor@unah.hn", null));

        assertEquals("Debes seleccionar un producto para iniciar la conversación.", exception.getMessage());
    }
}
