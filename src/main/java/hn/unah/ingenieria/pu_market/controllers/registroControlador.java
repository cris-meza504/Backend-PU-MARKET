package hn.unah.ingenieria.pu_market.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hn.unah.ingenieria.pu_market.service.registroServicio;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class registroControlador {

    @Autowired
    private registroServicio registroService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody Map<String, String> datos) {
        try {
            registroService.registrarNuevoUsuario(
                datos.get("nombre"),
                datos.get("apellido"),
                datos.get("correo"),
                datos.get("matricula"),
                datos.get("password")
            );
            return ResponseEntity.ok("Usuario registrado. Revisa tu correo para verificar tu cuenta.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/verificar")
    public ResponseEntity<?> verificarCorreo(@RequestParam String token) {
        try {
            registroService.verificarCorreo(token);
            return ResponseEntity.ok("Cuenta verificada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reenviar-verificacion")
        public ResponseEntity<String> reenviar(@RequestParam String correo) {
            try {
                registroService.reenviarVerificacion(correo);
                return ResponseEntity.ok("Correo de verificación reenviado.");
            } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
                }
    }

    
}
