package hn.unah.ingenieria.pu_market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hn.unah.ingenieria.pu_market.entity.Categoria;

@Repository
public interface categoriaRepositorio extends JpaRepository<Categoria, Integer>{
    boolean existsByNombre(String nombre);

}
