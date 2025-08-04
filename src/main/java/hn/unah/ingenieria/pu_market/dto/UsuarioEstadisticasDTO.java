package hn.unah.ingenieria.pu_market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioEstadisticasDTO {
    private Integer id;
    private String nombre;
    private String apellido;
    private String correoInstitucional;
    private int ventas;
    private int compras;
}
