package org.jcdev.stockflow.backend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarDetallePedidoDto {

    @PositiveOrZero(message = "La cantidad debe ser mayor a 0")
    private Integer cantidadSolicitada;
}
