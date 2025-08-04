package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.entity.Rol;
import hn.unah.ingenieria.pu_market.dto.UsuarioEstadisticasDTO;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;
import hn.unah.ingenieria.pu_market.service.usuarioServicio;
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
class UsuarioServicioTest {

    @Mock
    private usuarioRepositorio usuarioRepo;

    @Mock
    private ventaServicio ventaServicio;

    @InjectMocks
    private usuarioServicio usuarioServicio;

    private Usuario estudiante;
    private Usuario admin;
    private Rol rolEstudiante;
    private Rol rolAdmin;

    @BeforeEach
    void setUp() {
        // Estudiante
        rolEstudiante = new Rol();
        rolEstudiante.setNombreRol("ESTUDIANTE");
        estudiante = new Usuario();
        estudiante.setId(1);
        estudiante.setNombre("Pedro");
        estudiante.setApellido("Perez");
        estudiante.setCorreoInstitucional("pedro@unah.hn");
        estudiante.setActivo(true);
        estudiante.getRoles().add(rolEstudiante);

        // Admin
        rolAdmin = new Rol();
        rolAdmin.setNombreRol("ADMIN");
        admin = new Usuario();
        admin.setId(2);
        admin.setNombre("Admin");
        admin.setApellido("Root");
        admin.setCorreoInstitucional("admin@unah.hn");
        admin.setActivo(true);
        admin.getRoles().add(rolAdmin);
    }

    @Test
    void obtenerEstudiantes_deberiaRetornarSoloEstudiantes() {
        when(usuarioRepo.findUsuariosEstudiantes()).thenReturn(Arrays.asList(estudiante));

        List<Usuario> result = usuarioServicio.obtenerEstudiantes();

        assertEquals(1, result.size());
        assertEquals("Pedro", result.get(0).getNombre());
        verify(usuarioRepo).findUsuariosEstudiantes();
    }

    @Test
    void eliminarEstudiante_deberiaDesactivarEstudiante() {
        when(usuarioRepo.findById(estudiante.getId())).thenReturn(Optional.of(estudiante));

        usuarioServicio.eliminarEstudiante(estudiante.getId());

        assertFalse(estudiante.getActivo());
        verify(usuarioRepo).save(estudiante);
    }

    @Test
    void eliminarEstudiante_deberiaLanzarSiNoEsEstudiante() {
        when(usuarioRepo.findById(admin.getId())).thenReturn(Optional.of(admin));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            usuarioServicio.eliminarEstudiante(admin.getId())
        );
        assertEquals("No puedes eliminar este usuario porque no es estudiante.", ex.getMessage());
        verify(usuarioRepo, never()).save(any());
    }

    @Test
    void eliminarEstudiante_deberiaLanzarSiNoExiste() {
        when(usuarioRepo.findById(100)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            usuarioServicio.eliminarEstudiante(100)
        );
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void obtenerEstudiantesConEstadisticas_deberiaRetornarDTOsCorrectos() {
        when(usuarioRepo.findUsuariosEstudiantes()).thenReturn(Arrays.asList(estudiante));
        when(ventaServicio.getVentasPorUsuario(estudiante.getId())).thenReturn(3);
        when(ventaServicio.getComprasPorUsuario(estudiante.getId())).thenReturn(2);

        List<UsuarioEstadisticasDTO> resultado = usuarioServicio.obtenerEstudiantesConEstadisticas();

        assertEquals(1, resultado.size());
        UsuarioEstadisticasDTO dto = resultado.get(0);
        assertEquals(estudiante.getId(), dto.getId());
        assertEquals("Pedro", dto.getNombre());
        assertEquals(3, dto.getVentas());
        assertEquals(2, dto.getCompras());
    }

    @Test
    void contarUsuarios_deberiaRetornarTotal() {
        when(usuarioRepo.count()).thenReturn(5L);

        long total = usuarioServicio.contarUsuarios();
        assertEquals(5L, total);
        verify(usuarioRepo).count();
    }
}
