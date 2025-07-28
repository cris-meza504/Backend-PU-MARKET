package hn.unah.ingenieria.pu_market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import hn.unah.ingenieria.pu_market.entity.Resena;

@Repository
public interface resenaRepositorio extends JpaRepository<Resena, Integer> {
    List<Resena> findByVendedor_Id(Integer idvendedor);
    List<Resena> findByVendedor_CorreoInstitucional(String correoVendedor);
}
