package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.entity.Rol;
import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;
import hn.unah.ingenieria.pu_market.service.loginServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServicioTest {

    @Mock
    private usuarioRepositorio usuarioRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private loginServicio loginServicio;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setCorreoInstitucional("alumno@unah.hn");
        usuario.setPasswordHash("hashpw");
        usuario.setVerificado(true);
        usuario.setActivo(true);
        usuario.setRoles(Set.of());
    }

    // ------------------------
    // ESTUDIANTE: Casos de éxito y error
    // ------------------------

    @Test
    void login_estudianteExito() {
        when(usuarioRepo.findByCorreoInstitucional("alumno@unah.hn")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("1234", "hashpw")).thenReturn(true);

        Usuario result = loginServicio.login("alumno@unah.hn", "1234");

        assertEquals(usuario, result);
    }

    @Test
    void login_usuarioNoEncontrado() {
        when(usuarioRepo.findByCorreoInstitucional("inexistente@unah.hn")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> loginServicio.login("inexistente@unah.hn", "pw"));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void login_noVerificado() {
        usuario.setVerificado(false);
        when(usuarioRepo.findByCorreoInstitucional("alumno@unah.hn")).thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> loginServicio.login("alumno@unah.hn", "pw"));
        assertEquals("La cuenta no ha sido verificada.", ex.getMessage());
    }

    @Test
    void login_noActivo() {
        usuario.setActivo(false);
        when(usuarioRepo.findByCorreoInstitucional("alumno@unah.hn")).thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> loginServicio.login("alumno@unah.hn", "pw"));
        assertEquals("Esta cuenta ha sido eliminada por un administrador.", ex.getMessage());
    }

    @Test
    void login_passwordIncorrecta() {
        when(usuarioRepo.findByCorreoInstitucional("alumno@unah.hn")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("malapass", "hashpw")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> loginServicio.login("alumno@unah.hn", "malapass"));
        assertEquals("Contraseña incorrecta", ex.getMessage());
    }

    // ------------------------
    // ADMINISTRADOR: Casos de éxito y error
    // ------------------------

    @Test
    void loginAdministrador_exito() {
        Rol adminRol = new Rol();
        adminRol.setNombreRol("ADMIN");
        usuario.setRoles(Set.of(adminRol));

        when(usuarioRepo.findByCorreoInstitucional("admin@unah.hn")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("1234", "hashpw")).thenReturn(true);

        Usuario result = loginServicio.loginAdministrador("admin@unah.hn", "1234");

        assertEquals(usuario, result);
    }

    @Test
    void loginAdministrador_noEncontrado() {
        when(usuarioRepo.findByCorreoInstitucional("nadie@unah.hn")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> loginServicio.loginAdministrador("nadie@unah.hn", "x"));
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void loginAdministrador_noActivo() {
        usuario.setActivo(false);
        Rol adminRol = new Rol(); adminRol.setNombreRol("ADMIN");
        usuario.setRoles(Set.of(adminRol));

        when(usuarioRepo.findByCorreoInstitucional("admin@unah.hn")).thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> loginServicio.loginAdministrador("admin@unah.hn", "x"));
        assertEquals("Esta cuenta ha sido eliminada por un administrador.", ex.getMessage());
    }

    @Test
    void loginAdministrador_passwordIncorrecta() {
        Rol adminRol = new Rol(); adminRol.setNombreRol("ADMIN");
        usuario.setRoles(Set.of(adminRol));

        when(usuarioRepo.findByCorreoInstitucional("admin@unah.hn")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("malapass", "hashpw")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> loginServicio.loginAdministrador("admin@unah.hn", "malapass"));
        assertEquals("Contraseña incorrecta", ex.getMessage());
    }

    @Test
    void loginAdministrador_noEsAdmin() {
        // Usuario sin rol ADMIN
        usuario.setRoles(Set.of());
        when(usuarioRepo.findByCorreoInstitucional("alumno@unah.hn")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("1234", "hashpw")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> loginServicio.loginAdministrador("alumno@unah.hn", "1234"));
        assertEquals("No tienes permisos de administrador.", ex.getMessage());
    }
}
