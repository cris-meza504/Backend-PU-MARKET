package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.entity.Resena;
import hn.unah.ingenieria.pu_market.repository.resenaRepositorio;
import hn.unah.ingenieria.pu_market.service.resenaServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResenaServicioTest {

    @Mock
    private resenaRepositorio resenaRepo;

    @InjectMocks
    private resenaServicio resenaServicio;

    private Resena resena;

    @BeforeEach
    void setUp() {
        resena = new Resena();
        resena.setId(1);
        resena.setComentario("Muy buen vendedor");
        // Puedes setear otros campos (vendedor, resenador, etc.) si deseas.
    }

    @Test
    void crear_debeGuardarYRetornarResena() {
        when(resenaRepo.save(resena)).thenReturn(resena);

        Resena resultado = resenaServicio.crear(resena);

        assertNotNull(resultado);
        assertEquals("Muy buen vendedor", resultado.getComentario());
        verify(resenaRepo).save(resena);
    }

    @Test
    void listarPorVendedorCorreo_debeRetornarLista() {
        String correo = "vendedor@unah.hn";
        List<Resena> lista = Arrays.asList(resena);

        when(resenaRepo.findByVendedor_CorreoInstitucional(correo)).thenReturn(lista);

        List<Resena> resultado = resenaServicio.listarPorVendedorCorreo(correo);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(resena.getComentario(), resultado.get(0).getComentario());
        verify(resenaRepo).findByVendedor_CorreoInstitucional(correo);
    }
}
