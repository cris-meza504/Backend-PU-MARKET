package hn.unah.ingenieria.pu_market.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.ingenieria.pu_market.entity.Rol;


public interface rolRepositorio extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombreRol(String nombreRol);
}
