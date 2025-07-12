package hn.unah.ingenieria.pu_market.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hn.unah.ingenieria.pu_market.entity.Usuario;


@Repository
public interface usuarioRepositorio extends JpaRepository<Usuario, Integer>{
    Optional<Usuario> findByCorreoInstitucional(String correoInstitucional);
}