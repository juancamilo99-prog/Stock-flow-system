package org.jcdev.stockflow.backend.dto.creardto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearDetalleRecepcionDto {

    @NotNull(message = "La recepcion es obligatoria")
    private Long idRecepcion;
}
