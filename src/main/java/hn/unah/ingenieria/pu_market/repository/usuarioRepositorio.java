package hn.unah.ingenieria.pu_market.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import hn.unah.ingenieria.pu_market.entity.Usuario;


@Repository
public interface usuarioRepositorio extends JpaRepository<Usuario, Integer>{
    
    Optional<Usuario> findByCorreoInstitucional(String correoInstitucional);

    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombreRol = 'ESTUDIANTE' AND u.activo = true")
    List<Usuario> findUsuariosEstudiantes();

}