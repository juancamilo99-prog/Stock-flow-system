package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Long id;
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;
    private String descripcion;
    private Boolean activo;

    public Ubicacion(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.activo = true;
    }
}
