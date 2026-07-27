package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.EstadoTarea;
import org.jcdev.stockflow.backend.enums.TipoTarea;

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
    @Column(name = "tipo_tarea")
    private TipoTarea tipoTarea;
    private String descripcion;
    @Column(name = "fecha_tarea")
    private LocalDate fechaTarea;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoTarea estadoTarea;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;
    @ManyToOne
    @JoinColumn(name = "id_recepcion")
    private Recepcion recepcion;

    public Tarea(TipoTarea tipoTarea, String descripcion, LocalDate fechaTarea, EstadoTarea estadoTarea, Usuario usuario, Pedido pedido, Recepcion recepcion) {
        this.tipoTarea = tipoTarea;
        this.descripcion = descripcion;
        this.fechaTarea = fechaTarea;
        this.estadoTarea = estadoTarea;
        this.usuario = usuario;
        this.pedido = pedido;
        this.recepcion = recepcion;
    }
}
