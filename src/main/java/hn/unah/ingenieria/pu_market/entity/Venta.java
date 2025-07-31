package hn.unah.ingenieria.pu_market.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Ventas")
@Data
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Venta")
    private Integer idVenta;

    @ManyToOne
    @JoinColumn(name = "ID_Producto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "ID_Vendedor", nullable = false)
    private Usuario vendedor;

    @ManyToOne
    @JoinColumn(name = "ID_Comprador", nullable = false)
    private Usuario comprador;

    @Column(name = "FechaVenta")
    private Date fechaVenta;

    @Column(name = "PrecioVenta")
    private Double precioVenta;

}
