package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}
