package hn.unah.ingenieria.pu_market.servicios;

import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.entity.Verificacion;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;
import hn.unah.ingenieria.pu_market.repository.verificacionesRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistroServicioTest {

    @Mock
    private usuarioRepositorio usuarioRepo;

    @Mock
    private verificacionesRepositorio verificacionRepo;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private hn.unah.ingenieria.pu_market.service.registroServicio registroServicio;

    private Usuario usuario;
    private Verificacion verificacion;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setNombre("Cristofer");
        usuario.setApellido("Meza");
        usuario.setCorreoInstitucional("test@unah.hn");
        usuario.setMatricula("2021-001");
        usuario.setPasswordHash("1234");
        usuario.setVerificado(false);

        verificacion = new Verificacion();
        verificacion.setUsuario(usuario);
        verificacion.setToken(UUID.randomUUID().toString());
        verificacion.setFechaExpiracion(LocalDateTime.now().plusHours(24));
        verificacion.setVerificado(false);
    }

    // -----------------------
    // registrarNuevoUsuario()
    // -----------------------
    @Test
    void registrarNuevoUsuario_exito() {
        when(usuarioRepo.findByCorreoInstitucional("test@unah.hn")).thenReturn(Optional.empty());
        when(usuarioRepo.save(any(Usuario.class))).thenReturn(usuario);
        when(verificacionRepo.save(any(Verificacion.class))).thenReturn(verificacion);

        registroServicio.registrarNuevoUsuario(
                "Cristofer", "Meza", "test@unah.hn", "2021-001", "1234"
        );

        verify(usuarioRepo, times(1)).save(any(Usuario.class));
        verify(verificacionRepo, times(1)).save(any(Verificacion.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void registrarNuevoUsuario_correoDuplicado() {
        when(usuarioRepo.findByCorreoInstitucional("test@unah.hn"))
                .thenReturn(Optional.of(usuario));

        assertThrows(RuntimeException.class, () ->
                registroServicio.registrarNuevoUsuario(
                        "Cristofer", "Meza", "test@unah.hn", "2021-001", "1234"
                )
        );

        verify(usuarioRepo, never()).save(any());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    // -----------------------
    // verificarCorreo()
    // -----------------------
    @Test
    void verificarCorreo_tokenValido() {
        when(verificacionRepo.findByToken(verificacion.getToken()))
                .thenReturn(Optional.of(verificacion));

        registroServicio.verificarCorreo(verificacion.getToken());

        assertTrue(verificacion.getUsuario().getVerificado());
        assertTrue(verificacion.getVerificado());
        verify(verificacionRepo).save(verificacion);
        verify(usuarioRepo).save(verificacion.getUsuario());
    }

    @Test
    void verificarCorreo_tokenInvalido() {
        when(verificacionRepo.findByToken("invalido")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> registroServicio.verificarCorreo("invalido")
        );
    }

    @Test
    void verificarCorreo_tokenExpirado() {
        verificacion.setFechaExpiracion(LocalDateTime.now().minusHours(1));
        when(verificacionRepo.findByToken(verificacion.getToken()))
                .thenReturn(Optional.of(verificacion));

        assertThrows(RuntimeException.class,
                () -> registroServicio.verificarCorreo(verificacion.getToken())
        );
    }

    @Test
    void verificarCorreo_tokenYaUsado() {
        verificacion.setVerificado(true);
        when(verificacionRepo.findByToken(verificacion.getToken()))
                .thenReturn(Optional.of(verificacion));

        assertThrows(RuntimeException.class,
                () -> registroServicio.verificarCorreo(verificacion.getToken())
        );
    }

    // -----------------------
    // reenviarVerificacion()
    // -----------------------
    @Test
    void reenviarVerificacion_exito() {
        when(usuarioRepo.findByCorreoInstitucional("test@unah.hn"))
                .thenReturn(Optional.of(usuario));
        when(verificacionRepo.findByUsuario(usuario)).thenReturn(Optional.of(verificacion));

        registroServicio.reenviarVerificacion("test@unah.hn");

        verify(verificacionRepo).save(any(Verificacion.class));
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void reenviarVerificacion_usuarioNoExiste() {
        when(usuarioRepo.findByCorreoInstitucional("test@unah.hn"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> registroServicio.reenviarVerificacion("test@unah.hn")
        );
    }

    @Test
    void reenviarVerificacion_usuarioYaVerificado() {
        usuario.setVerificado(true);
        when(usuarioRepo.findByCorreoInstitucional("test@unah.hn"))
                .thenReturn(Optional.of(usuario));

        assertThrows(RuntimeException.class,
                () -> registroServicio.reenviarVerificacion("test@unah.hn")
        );
    }
}
