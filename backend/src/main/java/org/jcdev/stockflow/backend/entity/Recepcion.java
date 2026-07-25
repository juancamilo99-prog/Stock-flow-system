package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.EstadoRecepcion;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Recepcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recepcion")
    private Long id;
    @Column(name = "fecha_recepcion")
    private LocalDate fechaRecepcion;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoRecepcion estadoRecepcion;
    @Column(nullable = false, length = 100)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Recepcion(LocalDate fechaRecepcion, EstadoRecepcion estadoRecepcion, String observaciones, Empresa empresa, Usuario usuario) {
        this.fechaRecepcion = fechaRecepcion;
        this.estadoRecepcion = estadoRecepcion;
        this.observaciones = observaciones;
        this.empresa = empresa;
        this.usuario = usuario;
    }
}
