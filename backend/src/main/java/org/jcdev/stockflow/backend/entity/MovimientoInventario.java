package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.TipoMovimiento;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento_inventario")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_movimiento")
    private TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    @Column(name = "fecha_movimiento")
    private LocalDate fechaMovimiento;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
    @ManyToOne
    @JoinColumn(name = "id_recepcion")
    private Recepcion recepcion;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    public MovimientoInventario(TipoMovimiento tipoMovimiento, Integer cantidad, LocalDate fechaMovimiento, String descripcion, Usuario usuario, Producto producto, Recepcion recepcion, Pedido pedido) {
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fechaMovimiento = fechaMovimiento;
        this.descripcion = descripcion;
        this.usuario = usuario;
        this.producto = producto;
        this.recepcion = recepcion;
        this.pedido = pedido;
    }
}
