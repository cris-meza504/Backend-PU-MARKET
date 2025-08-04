package hn.unah.ingenieria.pu_market.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.ingenieria.pu_market.dto.UsuarioEstadisticasDTO;
import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.repository.usuarioRepositorio;

@Service
public class usuarioServicio {
    
    @Autowired
    private usuarioRepositorio usuarioRepo;

    @Autowired
    private ventaServicio ventaServicio;

    public List<Usuario> obtenerEstudiantes() {
        return usuarioRepo.findUsuariosEstudiantes();
    }

    public void eliminarEstudiante(Integer id) {
        Usuario usuario = usuarioRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean esEstudiante = usuario.getRoles().stream()
            .anyMatch(rol -> rol.getNombreRol().equalsIgnoreCase("ESTUDIANTE"));

        if (!esEstudiante) {
            throw new RuntimeException("No puedes eliminar este usuario porque no es estudiante.");
        }
        //eliminacion logica no fisica
        usuario.setActivo(false);
        usuarioRepo.save(usuario);
    }

    public List<UsuarioEstadisticasDTO> obtenerEstudiantesConEstadisticas() {
        List<Usuario> estudiantes = usuarioRepo.findUsuariosEstudiantes();
        List<UsuarioEstadisticasDTO> resultado = new ArrayList<>();
        for (Usuario u : estudiantes) {
            int ventas = ventaServicio.getVentasPorUsuario(u.getId());
            int compras = ventaServicio.getComprasPorUsuario(u.getId());
            resultado.add(new UsuarioEstadisticasDTO(
                u.getId(),
                u.getNombre(),
                u.getApellido(),
                u.getCorreoInstitucional(),
                ventas,
                compras
            ));
        }
        return resultado;
    }

    public long contarUsuarios() {
    return usuarioRepo.count();
}


}
