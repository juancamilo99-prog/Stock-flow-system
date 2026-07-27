package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long id;
    @Column(name = "tipo_accion")
    private String descripcionAccion;
    @Column(name = "fecha_accion")
    private LocalDate fechaAccion;


    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Auditoria(String descripcionAccion, LocalDate fechaAccion, Usuario usuario) {
        this.descripcionAccion = descripcionAccion;
        this.fechaAccion = fechaAccion;
        this.usuario = usuario;
    }
}
