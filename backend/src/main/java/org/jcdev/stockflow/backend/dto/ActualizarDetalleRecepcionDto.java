package org.jcdev.stockflow.backend.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarDetalleRecepcionDto {

    @PositiveOrZero(message = "la cantidad no puede ser menor que 0")
    private Integer cantidadRecibida;
}
