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
@Table(name = "Reseñas")
@Data
public class Resena {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Reseña")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_Usuario_Vendedor")
    private Usuario vendedor;

    @ManyToOne
    @JoinColumn(name = "ID_Usuario_Reseñador")
    private Usuario resenador;
    
    @Column(name = "Comentario")
    private String comentario;

    @Column(name = "Fecha")
    private LocalDateTime fecha = LocalDateTime.now();
}
