package org.jcdev.stockflow.backend.dto.actualizardto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.EstadoRecepcion;

@Getter
@Setter
public class ActualizarRecepcionDto {

    @Size(max = 255, message = "La observacion no puede superar los 255 caracteres")
    private String observaciones;
    private EstadoRecepcion estadoRecepcion;
}
