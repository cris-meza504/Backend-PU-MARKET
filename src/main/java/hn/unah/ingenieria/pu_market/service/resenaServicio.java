package hn.unah.ingenieria.pu_market.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.ingenieria.pu_market.entity.Resena;
import hn.unah.ingenieria.pu_market.repository.resenaRepositorio;

@Service
public class resenaServicio {
    
    @Autowired
    private resenaRepositorio resenaRepo;

    public Resena crear(Resena r) {
        return resenaRepo.save(r);
    }

    public List<Resena> listarPorVendedorCorreo(String correo) {
        return resenaRepo.findByVendedor_CorreoInstitucional(correo);
    }
}
