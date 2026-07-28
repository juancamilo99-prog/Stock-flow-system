package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.EstadoPedido;

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
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoPedido estadoPedido;
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Pedido(String observaciones, Empresa empresa, Usuario usuario) {
        this.fechaPedido = LocalDate.now();
        this.estadoPedido = EstadoPedido.PENDIENTE;
        this.observaciones = observaciones;
        this.empresa = empresa;
        this.usuario = usuario;
    }
}
