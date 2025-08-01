package hn.unah.ingenieria.pu_market.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.entity.Verificacion;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;
import hn.unah.ingenieria.pu_market.repository.verificacionesRepositorio;


@Service
public class registroServicio {
    
    @Autowired
    private usuarioRepositorio usuarioRepo;

    @Autowired
    private verificacionesRepositorio verificacionRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    public void registrarNuevoUsuario(String nombre, String apellido, String correo, String matricula, String password) {
        if (usuarioRepo.findByCorreoInstitucional(correo).isPresent()) {
            throw new RuntimeException("Correo ya registrado.");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setCorreoInstitucional(correo);
        usuario.setMatricula(matricula);
        usuario.setVerificado(false);
        usuario.setPasswordHash(password);

        //encriptacion de la contraseña 
        usuario.setPasswordHash(passwordEncoder.encode(password));


        usuarioRepo.save(usuario);

        // Crear token de verificación
        String token = UUID.randomUUID().toString();

        Verificacion verificacion = new Verificacion();
        verificacion.setUsuario(usuario);
        verificacion.setToken(token);
        verificacion.setFechaExpiracion(LocalDateTime.now().plusHours(24));
        verificacion.setVerificado(false);

        verificacionRepo.save(verificacion);

        enviarCorreoVerificacion(correo, token);
    }

    public void verificarCorreo(String token) {
        Verificacion verificacion = verificacionRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (verificacion.getVerificado())
            throw new RuntimeException("Este token ya fue usado.");

        if (verificacion.getFechaExpiracion().isBefore(LocalDateTime.now()))
            throw new RuntimeException("El token ha expirado.");

        verificacion.getUsuario().setVerificado(true);
        verificacion.setVerificado(true);
        verificacionRepo.save(verificacion);
        usuarioRepo.save(verificacion.getUsuario());
    }

    private void enviarCorreoVerificacion(String correo, String token) {
        String enlace = "http://localhost:8080/api/verificar?token=" + token;
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(correo);
        mensaje.setSubject("Verificación de correo de PU-Market");
        mensaje.setText("Haz clic en el siguiente enlace para verificar tu cuenta:\n\n" + enlace);
        mailSender.send(mensaje);
    }

    public void reenviarVerificacion(String correo) {
    Optional<Usuario> usuarioOpt = usuarioRepo.findByCorreoInstitucional(correo);

    if (usuarioOpt.isEmpty()) {
        throw new RuntimeException("Usuario no encontrado");
    }

    Usuario usuario = usuarioOpt.get();

    if (usuario.getVerificado()) {
        throw new RuntimeException("Esta cuenta ya fue verificada");
    }

    String nuevoToken = UUID.randomUUID().toString();

    Verificacion verificacion = verificacionRepo.findByUsuario(usuario)
        .orElse(new Verificacion());

    verificacion.setUsuario(usuario);
    verificacion.setToken(nuevoToken);
    verificacion.setFechaExpiracion(LocalDateTime.now().plusHours(24));
    verificacion.setVerificado(false);

    verificacionRepo.save(verificacion);

    enviarCorreoVerificacion(correo, nuevoToken);
    }
}
