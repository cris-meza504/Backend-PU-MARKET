package hn.unah.ingenieria.pu_market.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Inventario")
@Data
public class Inventario {
    
    @Id
    @Column(name = "ID_Producto")
    private Integer productoId;

    @Column(name = "Stock")
    private Integer stock;

    @OneToOne @MapsId
    @JoinColumn(name = "ID_Producto")
    private Producto producto;
}
