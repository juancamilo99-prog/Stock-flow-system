package org.jcdev.stockflow.backend.dto.creardto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AjusteInventarioDto {

    @NotNull(message = "La cantidad es obligatoria")
    @PositiveOrZero(message = "La cantidad debe ser mayor de 0")
    private Integer cantidad;
    @Size(message = "La descripcion no puede superar los 255 caracteres", max = 255)
    private String descripcion;

    @NotNull(message = "El producto es obligatio")
    private Long idProducto;
    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;

}
