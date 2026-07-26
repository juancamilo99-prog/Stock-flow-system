package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DetalleRecepcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_recepcion")
    private Long id;
    @Column(nullable = false,  name = "cantidad_esperada")
    private Integer cantidadEsperada;
    @Column(nullable = false, name = "cantidad_recibida")
    private Integer cantidadRecibida;

    @ManyToOne
    @JoinColumn(name = "id_recepcion")
    private Recepcion recepcion;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    public DetalleRecepcion(Integer cantidadEsperada, Integer cantidadRecibida, Recepcion recepcion, Producto producto) {
        this.cantidadEsperada = cantidadEsperada;
        this.cantidadRecibida = cantidadRecibida;
        this.recepcion = recepcion;
        this.producto = producto;
    }
}
