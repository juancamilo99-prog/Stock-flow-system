package org.jcdev.stockflow.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.entity.Recepcion;

@Getter
@Setter
public class CrearDetalleRecepcionDto {

    @NotNull(message = "La recepcion es obligatoria")
    private Long idRecepcion;
}
