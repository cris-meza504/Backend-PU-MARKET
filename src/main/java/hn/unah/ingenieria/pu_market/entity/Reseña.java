package hn.unah.ingenieria.pu_market.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Reseñas")
@Data
public class Reseña {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Reseña")
    private Integer id;

    @OneToOne @JoinColumn(name = "ID_Compra")
    private Compra compra;

    @Column(name = "Calificacion")
    private Integer calificacion;

    @Column(name = "Comentario")
    private String comentario;
}
