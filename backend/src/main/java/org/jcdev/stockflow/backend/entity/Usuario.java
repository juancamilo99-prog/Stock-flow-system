package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.Rol;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String nombre;
    @Email
    @Column(nullable = false, unique = true)
    @NotBlank
    private String email;
    @NotBlank
    @Column(length = 100, nullable = false,name = "password_hash")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;
    private boolean activo;

    public Usuario(String nombre, String email, String password, Rol rol, boolean activo) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.activo = activo;
        this.rol = rol;
    }
}
