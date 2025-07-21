package hn.unah.ingenieria.pu_market.dto;

import java.util.List;

import hn.unah.ingenieria.pu_market.entity.Producto;
import lombok.Data;

@Data
public class ProductoConImagenesDTO {
    private Producto producto;
    private List<String> imagenes; // lista de URLs
}
