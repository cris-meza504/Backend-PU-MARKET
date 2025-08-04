package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.entity.Rol;
import hn.unah.ingenieria.pu_market.entity.Verificacion;
import hn.unah.ingenieria.pu_market.repository.rolRepositorio;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;
import hn.unah.ingenieria.pu_market.repository.verificacionesRepositorio;
import hn.unah.ingenieria.pu_market.service.registroServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistroServicioTest {

    @Mock
    private usuarioRepositorio usuarioRepo;

    @Mock
    private rolRepositorio rolRepo;

    @Mock
    private verificacionesRepositorio verificacionRepo;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private registroServicio registroServicio;

    private Usuario usuario;
    private Rol rolEstudiante;
    private Verificacion verificacion;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setCorreoInstitucional("juan@unah.hn");
        usuario.setMatricula("2019xxxx");
        usuario.setVerificado(false);

        rolEstudiante = new Rol();
        rolEstudiante.setNombreRol("ESTUDIANTE");

        verificacion = new Verificacion();
        verificacion.setUsuario(usuario);
        verificacion.setToken("tokentest");
        verificacion.setFechaExpiracion(LocalDateTime.now().plusHours(24));
        verificacion.setVerificado(false);
    }

    @Test
    void registrarNuevoUsuario_exito() {
        when(usuarioRepo.findByCorreoInstitucional(usuario.getCorreoInstitucional()))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashsecreta");
        when(rolRepo.findByNombreRol("ESTUDIANTE")).thenReturn(Optional.of(rolEstudiante));
        when(usuarioRepo.save(any(Usuario.class))).thenAnswer(i -> i.getArguments()[0]);
        when(verificacionRepo.save(any(Verificacion.class))).thenAnswer(i -> i.getArguments()[0]);

        assertDoesNotThrow(() ->
            registroServicio.registrarNuevoUsuario(
                usuario.getNombre(), usuario.getApellido(),
                usuario.getCorreoInstitucional(), usuario.getMatricula(), "secreta"
            )
        );
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void registrarNuevoUsuario_correoYaRegistrado() {
        when(usuarioRepo.findByCorreoInstitucional(usuario.getCorreoInstitucional()))
                .thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            registroServicio.registrarNuevoUsuario(
                usuario.getNombre(), usuario.getApellido(),
                usuario.getCorreoInstitucional(), usuario.getMatricula(), "secreta"
            )
        );
        assertEquals("Correo ya registrado.", ex.getMessage());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void registrarNuevoUsuario_rolNoExiste() {
        when(usuarioRepo.findByCorreoInstitucional(usuario.getCorreoInstitucional()))
                .thenReturn(Optional.empty());
        when(rolRepo.findByNombreRol("ESTUDIANTE")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                registroServicio.registrarNuevoUsuario(
                        usuario.getNombre(), usuario.getApellido(),
                        usuario.getCorreoInstitucional(), usuario.getMatricula(), "secreta"
                )
        );
        assertEquals("Rol estudiante no existe", ex.getMessage());
    }

    @Test
    void verificarCorreo_exito() {
        verificacion.setVerificado(false);
        verificacion.setFechaExpiracion(LocalDateTime.now().plusMinutes(5));
        when(verificacionRepo.findByToken("tokentest"))
                .thenReturn(Optional.of(verificacion));
        when(verificacionRepo.save(any(Verificacion.class))).thenReturn(verificacion);

        assertDoesNotThrow(() -> registroServicio.verificarCorreo("tokentest"));
        assertTrue(verificacion.getVerificado());
        assertTrue(verificacion.getUsuario().getVerificado());
        verify(usuarioRepo).save(verificacion.getUsuario());
    }

    @Test
    void verificarCorreo_tokenInvalido() {
        when(verificacionRepo.findByToken("tokennovalido")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                registroServicio.verificarCorreo("tokennovalido")
        );
        assertEquals("Token inválido", ex.getMessage());
    }

    @Test
    void verificarCorreo_tokenYaUsado() {
        verificacion.setVerificado(true);
        when(verificacionRepo.findByToken("tokentest")).thenReturn(Optional.of(verificacion));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                registroServicio.verificarCorreo("tokentest")
        );
        assertEquals("Este token ya fue usado.", ex.getMessage());
    }

    @Test
    void verificarCorreo_tokenExpirado() {
        verificacion.setVerificado(false);
        verificacion.setFechaExpiracion(LocalDateTime.now().minusMinutes(5));
        when(verificacionRepo.findByToken("tokentest")).thenReturn(Optional.of(verificacion));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                registroServicio.verificarCorreo("tokentest")
        );
        assertEquals("El token ha expirado.", ex.getMessage());
    }

    @Test
    void reenviarVerificacion_exito() {
        when(usuarioRepo.findByCorreoInstitucional(usuario.getCorreoInstitucional()))
                .thenReturn(Optional.of(usuario));
        when(verificacionRepo.findByUsuario(usuario)).thenReturn(Optional.of(verificacion));
        when(verificacionRepo.save(any(Verificacion.class))).thenReturn(verificacion);

        assertDoesNotThrow(() ->
            registroServicio.reenviarVerificacion(usuario.getCorreoInstitucional())
        );
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void reenviarVerificacion_usuarioNoEncontrado() {
        when(usuarioRepo.findByCorreoInstitucional("noexiste@unah.hn")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                registroServicio.reenviarVerificacion("noexiste@unah.hn")
        );
        assertEquals("Usuario no encontrado", ex.getMessage());
    }

    @Test
    void reenviarVerificacion_yaVerificado() {
        usuario.setVerificado(true);
        when(usuarioRepo.findByCorreoInstitucional(usuario.getCorreoInstitucional()))
                .thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                registroServicio.reenviarVerificacion(usuario.getCorreoInstitucional())
        );
        assertEquals("Esta cuenta ya fue verificada", ex.getMessage());
    }
}
