package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.tarea.EstadoTarea;
import org.jcdev.stockflow.backend.enums.tarea.PrioridadTarea;
import org.jcdev.stockflow.backend.enums.tarea.TipoTarea;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tarea{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tarea", nullable = false)
    private TipoTarea tipoTarea;
    private String descripcion;
    @Column(name = "fecha_tarea")
    private LocalDate fechaCreacion;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTarea estadoTarea;
    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", nullable = false)
    private PrioridadTarea prioridadTarea;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "id_recepcion")
    private Recepcion recepcion;

    public Tarea(TipoTarea tipoTarea, String descripcion, Usuario usuario, Pedido pedido, Recepcion recepcion, PrioridadTarea prioridadTarea) {
        this.tipoTarea = tipoTarea;
        this.descripcion = descripcion;
        this.fechaCreacion = LocalDate.now();
        this.estadoTarea = EstadoTarea.pendiente;
        this.usuario = usuario;
        this.pedido = pedido;
        this.recepcion = recepcion;
        this.prioridadTarea = prioridadTarea;
    }
}
