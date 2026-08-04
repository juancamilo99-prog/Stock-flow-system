package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.incidencia.EstadoIncidencia;
import org.jcdev.stockflow.backend.enums.incidencia.TipoIncidencia;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_incidencia")
    private Long id;
    @Column(name = "tipo_incidencia")
    @Enumerated(EnumType.STRING)
    private TipoIncidencia tipoIncidencia;
    private String descripcion;
    @Column(name = "fecha_incidencia")
    private LocalDate fechaIncidencia;
    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoIncidencia estadoIncidencia;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "id_recepcion")
    private Recepcion recepcion;


    public Incidencia(TipoIncidencia tipoIncidencia, String descripcion, Usuario usuario, Producto producto, Pedido pedido, Recepcion recepcion) {
        this.tipoIncidencia = tipoIncidencia;
        this.descripcion = descripcion;
        this.fechaIncidencia = LocalDate.now();
        this.estadoIncidencia = EstadoIncidencia.pendiente;
        this.usuario = usuario;
        this.producto = producto;
        this.pedido = pedido;
        this.recepcion = recepcion;
    }
}
