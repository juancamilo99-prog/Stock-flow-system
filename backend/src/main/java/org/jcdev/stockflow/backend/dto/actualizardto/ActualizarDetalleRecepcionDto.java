package org.jcdev.stockflow.backend.dto.actualizardto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarDetalleRecepcionDto {

    @PositiveOrZero(message = "la cantidad no puede ser menor que 0")
    @NotNull(message = "La cantidad recibida es obligatoria")
    private Integer cantidadRecibida;
}
