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
@Table(name = "Verificaciones")
@Data
public class Verificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Verificacion")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_Usuario")
    private Usuario usuario;

    @Column(name = "Token")
    private String token;

    @Column(name = "FechaExpiracion")
    private LocalDateTime fechaExpiracion;

    @Column(name = "Verificado")
    private Boolean verificado;
}
