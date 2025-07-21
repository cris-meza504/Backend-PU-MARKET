package hn.unah.ingenieria.pu_market.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "ImagenesProducto")
@Data
public class ImagenProducto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Imagen")
    private Integer id;

    
    @ManyToOne 
    @JoinColumn(name = "ID_Producto")
    @JsonBackReference
    private Producto producto;

    @Column(name = "URL_Imagen")
    private String urlImagen;
    
    public ImagenProducto() {
        
    }
}
