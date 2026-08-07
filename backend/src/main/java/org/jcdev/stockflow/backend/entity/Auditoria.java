package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_accion")
    private TipoAccion tipoAccion;
    @Column(name = "fecha_accion")
    private LocalDateTime fechaAccion;
    @Enumerated(EnumType.STRING)
    @Column(name = "entidad")
    private EntidadAuditoria entidad;
    private String descripcion;
    @Column(name = "id_entidad")
    private Long idEntidad;



    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Auditoria(TipoAccion tipoAccion,EntidadAuditoria entidad, String descripcion, Long idEntidad, Usuario usuario) {
        this.tipoAccion = tipoAccion;
        this.fechaAccion = LocalDateTime.now();
        this.entidad = entidad;
        this.descripcion = descripcion;
        this.idEntidad = idEntidad;
        this.usuario = usuario;
    }
}
