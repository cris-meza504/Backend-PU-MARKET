package hn.unah.ingenieria.pu_market.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.service.loginServicio;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class LoginControlador {

    @Autowired
    private loginServicio loginServicio;

    @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
            try {
                String correo = loginRequest.get("correo");
                String password = loginRequest.get("password");

                Usuario usuario = loginServicio.login(correo, password);

        return ResponseEntity.ok(usuario); 
            } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
        @PostMapping("/login/admin")
        public ResponseEntity<?> loginAdmin(@RequestBody Map<String, String> loginRequest) {
            try {
                String correo = loginRequest.get("correo");
                String password = loginRequest.get("password");

                Usuario usuario = loginServicio.loginAdministrador(correo, password);

                return ResponseEntity.ok(usuario);
             } catch (RuntimeException e) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
                }
        }
}
