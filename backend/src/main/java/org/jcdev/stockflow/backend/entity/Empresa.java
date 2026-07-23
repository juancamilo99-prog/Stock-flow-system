package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Long id;
    @Column(length = 100, nullable = false)
    private String nombre;
    @Email
    private String email;
    private String telefono;
    private Boolean activo;
    private String direccion;
    @Column(name = "tipo")
    private String tipoEmpresa;

    public Empresa(String nombre, String email, String telefono, String direccion, String tipo_empresa) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.tipoEmpresa = tipo_empresa;
        this.activo = true;
    }
}
