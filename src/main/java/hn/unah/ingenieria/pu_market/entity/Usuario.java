package hn.unah.ingenieria.pu_market.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name ="Usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Usuario")
    private Integer id;

    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "Apellido")
    private String apellido;

    @Column(name = "CorreoInstitucional")
    private String correoInstitucional;

    @Column(name = "Matricula")
    private String matricula;

    @Column(name = "Verificado")
    private Boolean verificado;

    @JsonIgnore
    @Column(name = "PasswordHash")
    private String passwordHash;

    @Column(name = "Activo")
    private Boolean activo = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "UsuarioRoles", joinColumns = @JoinColumn(name = "ID_Usuario"),
        inverseJoinColumns = @JoinColumn(name = "ID_Rol")
    )
    private Set<Rol> roles = new HashSet<>();

}
