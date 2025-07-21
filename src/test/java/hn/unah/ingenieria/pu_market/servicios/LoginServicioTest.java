package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.service.loginServicio;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServicioTest {

    @Mock
    private usuarioRepositorio usuarioRepo;

    @InjectMocks
    private loginServicio loginServicio;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setCorreoInstitucional("test@unah.hn");
        usuario.setPasswordHash("password123");
        usuario.setVerificado(true);
    }

    @Test
    void login_exito() {
        when(usuarioRepo.findByCorreoInstitucional("test@unah.hn"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = loginServicio.login("test@unah.hn", "password123");

        assertNotNull(resultado);
        assertEquals("test@unah.hn", resultado.getCorreoInstitucional());
    }

    @Test
    void login_usuarioNoEncontrado() {
        when(usuarioRepo.findByCorreoInstitucional("noexiste@unah.hn"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                loginServicio.login("noexiste@unah.hn", "password123")
        );

        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void login_usuarioNoVerificado() {
        usuario.setVerificado(false);
        when(usuarioRepo.findByCorreoInstitucional("test@unah.hn"))
                .thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                loginServicio.login("test@unah.hn", "password123")
        );

        assertEquals("La cuenta no ha sido verificada.", ex.getMessage());
    }

    @Test
    void login_contraseñaIncorrecta() {
        when(usuarioRepo.findByCorreoInstitucional("test@unah.hn"))
                .thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                loginServicio.login("test@unah.hn", "wrongpassword")
        );

        assertEquals("Contraseña incorrecta", ex.getMessage());
    }
}
