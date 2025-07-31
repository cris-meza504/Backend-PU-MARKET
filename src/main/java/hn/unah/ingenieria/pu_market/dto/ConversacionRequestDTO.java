package hn.unah.ingenieria.pu_market.dto;

import lombok.Data;

@Data
public class ConversacionRequestDTO {
    
    private Integer compradorId;

    private String vendedorEmail;

    private Integer productoId;
}
