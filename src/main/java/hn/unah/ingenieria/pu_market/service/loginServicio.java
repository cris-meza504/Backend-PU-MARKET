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

    public Usuario login(String correo, String password) {
    Usuario usuario = usuarioRepo.findByCorreoInstitucional(correo)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // Validar que esté verificado
    if (!usuario.getVerificado()) {
        throw new RuntimeException("La cuenta no ha sido verificada.");
    }

    // Comparar contraseñas encriptadas 
    if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
        throw new RuntimeException("Contraseña incorrecta");
    }

    return usuario;
}

}
