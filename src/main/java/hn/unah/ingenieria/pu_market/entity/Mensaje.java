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
@Table(name = "Mensajes")
@Data
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Mensaje")
    private Integer id;

    @ManyToOne @JoinColumn(name = "ID_Conversacion")
    private Conversacion conversacion;

    @ManyToOne @JoinColumn(name = "ID_Emisor")
    private Usuario emisor;

    @Column(name = "Contenido")
    private String contenido;

    @Column(name = "FechaEnvio")
    private LocalDateTime fechaEnvio = LocalDateTime.now();
}
