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
@Table(name = "Productos")
@Data
public class Producto {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Producto")
    private Integer id;                       

    @ManyToOne @JoinColumn(name = "ID_Usuario", nullable = false)
    private Usuario vendedor;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Descripcion")
    private String descripcion;

    @Column(name = "Precio")
    private Double precio;

    @ManyToOne @JoinColumn(name = "ID_Categoria")
    private Categoria categoria;

    @Column(name = "Activo")
    private Boolean activo;
}
