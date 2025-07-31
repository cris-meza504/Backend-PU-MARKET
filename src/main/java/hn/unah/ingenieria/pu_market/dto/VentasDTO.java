package hn.unah.ingenieria.pu_market.dto;

import lombok.Data;

@Data
public class VentasDTO {
    private Integer idProducto;
    private Integer idVendedor;
    private Integer idComprador;
    private Double precioVenta;
}
