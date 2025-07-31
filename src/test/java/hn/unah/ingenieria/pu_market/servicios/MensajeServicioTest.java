package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.entity.Conversacion;
import hn.unah.ingenieria.pu_market.entity.Mensaje;
import hn.unah.ingenieria.pu_market.repository.mensajeRepositorio;
import hn.unah.ingenieria.pu_market.service.mensajeServicio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MensajeServicioTest {

    @Mock
    private mensajeRepositorio mensajeRepo;

    @InjectMocks
    private mensajeServicio mensajeServicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void enviarMensaje_debeGuardarConFecha() {
        // Arrange
        Mensaje mensaje = new Mensaje();
        mensaje.setContenido("Hola mundo");

        // Simular una conversacion ya existente
        Conversacion conversacion = new Conversacion();
        conversacion.setId(1);
        mensaje.setConversacion(conversacion);

        Mensaje mensajeGuardado = new Mensaje();
        mensajeGuardado.setId(10);
        mensajeGuardado.setContenido("Hola mundo");
        mensajeGuardado.setConversacion(conversacion);
        mensajeGuardado.setFechaEnvio(LocalDateTime.now());

        when(mensajeRepo.save(any(Mensaje.class))).thenReturn(mensajeGuardado);

        // Act
        Mensaje resultado = mensajeServicio.enviarMensaje(mensaje);

        // Assert
        assertNotNull(resultado);
        assertEquals(10, resultado.getId());
        assertEquals("Hola mundo", resultado.getContenido());
        assertNotNull(resultado.getFechaEnvio());
        verify(mensajeRepo).save(any(Mensaje.class));
    }

    @Test
    void listarMensajesPorConversacion_debeDevolverMensajesOrdenados() {
        // Arrange
        Integer conversacionId = 5;

        Mensaje m1 = new Mensaje();
        m1.setId(1);
        m1.setContenido("Primero");
        m1.setFechaEnvio(LocalDateTime.now().minusMinutes(2));

        Mensaje m2 = new Mensaje();
        m2.setId(2);
        m2.setContenido("Segundo");
        m2.setFechaEnvio(LocalDateTime.now());

        List<Mensaje> mensajes = Arrays.asList(m1, m2);

        when(mensajeRepo.findByConversacionIdOrderByFechaEnvioAsc(conversacionId)).thenReturn(mensajes);

        // Act
        List<Mensaje> resultado = mensajeServicio.listarMensajesPorConversacion(conversacionId);

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Primero", resultado.get(0).getContenido());
        assertEquals("Segundo", resultado.get(1).getContenido());
        verify(mensajeRepo).findByConversacionIdOrderByFechaEnvioAsc(conversacionId);
    }
}
