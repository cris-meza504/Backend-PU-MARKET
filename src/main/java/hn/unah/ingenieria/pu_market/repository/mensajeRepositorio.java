package hn.unah.ingenieria.pu_market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hn.unah.ingenieria.pu_market.entity.Mensaje;

@Repository
public interface mensajeRepositorio extends JpaRepository<Mensaje, Integer>{
    List<Mensaje> findByConversacionIdOrderByFechaEnvioAsc(Integer conversacionId);
}
