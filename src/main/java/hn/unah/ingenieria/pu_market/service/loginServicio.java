package hn.unah.ingenieria.pu_market.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;

@Service
public class loginServicio {

    @Autowired
    private usuarioRepositorio usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //validacion para estudiantes
    public Usuario login(String correo, String password) {
    Usuario usuario = usuarioRepo.findByCorreoInstitucional(correo)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // Validar que esté verificado
    if (!usuario.getVerificado()) {
        throw new RuntimeException("La cuenta no ha sido verificada.");
    }

    // Validar que el usuario esté activo
    if (usuario.getActivo() != null && !usuario.getActivo()) {
       throw new RuntimeException("Esta cuenta ha sido eliminada por un administrador.");
    }


    // Comparar contraseñas encriptadas 
    if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
        throw new RuntimeException("Contraseña incorrecta");
    }

    return usuario;
}
    //validacion para administradores
    public Usuario loginAdministrador(String correo, String password) {
        Usuario usuario = usuarioRepo.findByCorreoInstitucional(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Validar que el usuario esté activo
        if (usuario.getActivo() != null && !usuario.getActivo()) {
            throw new RuntimeException("Esta cuenta ha sido eliminada por un administrador.");
        }

        // Validar contraseña
        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Validar que el usuario tenga el rol ADMIN
        boolean esAdmin = usuario.getRoles().stream()
            .anyMatch(rol -> rol.getNombreRol().equalsIgnoreCase("ADMIN"));

        if (!esAdmin) {
            throw new RuntimeException("No tienes permisos de administrador.");
        }

        return usuario;
    }


}
