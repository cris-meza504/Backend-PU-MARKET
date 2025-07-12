package hn.unah.ingenieria.pu_market.entity;

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
@Table(name = "Conversaciones")
@Data
public class Conversacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Conversacion")
    private Integer id;

    @ManyToOne @JoinColumn(name = "ID_Comprador")
    private Usuario comprador;

    @ManyToOne @JoinColumn(name = "ID_Vendedor")
    private Usuario vendedor;

    @ManyToOne @JoinColumn(name = "ID_Producto")
    private Producto producto;
}
