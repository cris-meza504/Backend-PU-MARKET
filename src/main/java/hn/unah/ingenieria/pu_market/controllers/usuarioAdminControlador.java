package hn.unah.ingenieria.pu_market.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hn.unah.ingenieria.pu_market.dto.UsuarioEstadisticasDTO;
import hn.unah.ingenieria.pu_market.service.usuarioServicio;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class usuarioAdminControlador {

    @Autowired
    private usuarioServicio usuarioServicio;
    
    @GetMapping("/estudiantes")
    public ResponseEntity<List<UsuarioEstadisticasDTO>> listarEstudiantes() {
        return ResponseEntity.ok(usuarioServicio.obtenerEstudiantesConEstadisticas());
    }

    @DeleteMapping("/estudiantes/{id}")
    public ResponseEntity<?> eliminarEstudiante(@PathVariable Integer id) {
        try {
            usuarioServicio.eliminarEstudiante(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuarios/total")
        public long totalUsuarios() {
        return usuarioServicio.contarUsuarios();
    }


}
