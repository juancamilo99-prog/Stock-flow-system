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
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;
    @Column(name = "fecha_pedido")
    private LocalDate fechaPedido;
    @Column(name = "estado")
    private String estadoPedido;
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    public Pedido(LocalDate fechaPedido, String observaciones) {
        this.fechaPedido = fechaPedido;
        this.observaciones = observaciones;
        this.estadoPedido = "pendiente";
    }
}
