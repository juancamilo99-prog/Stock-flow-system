package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.recepcion.EstadoRecepcion;

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
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    public Recepcion(String observaciones, Empresa empresa, Usuario usuario, Pedido pedido) {
        this.fechaRecepcion = LocalDate.now();
        this.estadoRecepcion = EstadoRecepcion.pendiente;
        this.observaciones = observaciones;
        this.empresa = empresa;
        this.usuario = usuario;
        this.pedido = pedido;
    }
}
