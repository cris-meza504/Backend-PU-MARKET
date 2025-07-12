package hn.unah.ingenieria.pu_market.entity;

import java.time.LocalDateTime;

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
@Table(name = "Compras")
@Data
public class Compra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Compra")
    private Integer id;

    @ManyToOne @JoinColumn(name = "ID_Producto")
    private Producto producto;

    @ManyToOne @JoinColumn(name = "ID_Comprador")
    private Usuario comprador;

    @Column(name = "FechaCompra")
    private LocalDateTime fechaCompra;
}
