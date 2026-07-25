package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_pedido")
    private Long id;
    @Column(name = "cantidad_solicitada", nullable = false)
    private Integer cantidadSolicitada;
    @Column(name = "cantidad_preparada", nullable = false)
    private Integer cantidadPreparada;


    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    public DetallePedido(Integer cantidadSolicitada, Pedido pedido, Producto producto) {
        this.cantidadSolicitada = cantidadSolicitada;
        this.cantidadPreparada = 0;
        this.pedido = pedido;
        this.producto = producto;
    }
}
