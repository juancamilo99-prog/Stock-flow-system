package org.jcdev.stockflow.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@NoArgsConstructor
@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer stock;
    @Column(name = "fecha_produccion")
    private LocalDate fechaProduccion;
    @Column(name = "fecha_caducidad")
    private LocalDate fechaCaducidad;
    @Column(name = "codigo_barras")
    private String codigoBarras;
    private boolean activo;

    //relacion con la entidad categoria
    //muchos a uno
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    @ManyToOne
    @JoinColumn(name = "id_ubicacion")
    private Ubicacion ubicacion;
    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;


    public Producto(String nombre, String descripcion, LocalDate fechaProduccion, LocalDate fechaCaducidad, String codigoBarras) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = 0;
        this.fechaProduccion = fechaProduccion;
        this.fechaCaducidad = fechaCaducidad;
        this.codigoBarras = codigoBarras;
        this.activo = true;
    }
}
