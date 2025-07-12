package hn.unah.ingenieria.pu_market.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hn.unah.ingenieria.pu_market.entity.Usuario;
import hn.unah.ingenieria.pu_market.entity.Verificacion;

@Repository
public interface verificacionesRepositorio extends JpaRepository<Verificacion, Integer> {
    Optional<Verificacion> findByToken(String token);
    Optional<Verificacion> findByUsuario(Usuario usuario);

}
