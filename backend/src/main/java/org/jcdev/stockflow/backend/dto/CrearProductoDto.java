package org.jcdev.stockflow.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class CrearProductoDto {

    @NotBlank(message = "El nombre es obligatorio") // -> anotaciones jakarta bean
    @Size(max = 100, message = "El nombre debe tener maximo 100 caracteres")
    private String nombre;
    @NotBlank(message = "La descripcion es obligatoria")
    @Size(min = 5, max = 255, message = "la descripcion debe tener maximo 255 caracteres")
    private String descripcion;
    @NotNull(message = "La cantidad debe ser obligatoria")
    @PositiveOrZero(message = "La cantidad no puede ser negativa")
    private Integer stock;
    @NotNull(message = "La fecha de produccion es obligatoria")
    private LocalDate fechaProduccion;
    @NotNull(message = "La fecha de caducidad es obligatoria")
    private LocalDate fechaCaducidad;
    @NotNull(message = "La categoria es obligatoria")
    private Long idCategoria;
    @NotNull(message = "La empresa es obligatoria")
    private Long idEmpresa;
    @NotNull(message = "La ubicacion es obligatoria")
    private Long idUbicacion;
}
