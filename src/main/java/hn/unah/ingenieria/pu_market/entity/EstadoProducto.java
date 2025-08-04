package hn.unah.ingenieria.pu_market.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "EstadosProducto")
@Data
public class EstadoProducto {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", unique = true, nullable = false )
    private String nombre;
}
