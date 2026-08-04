package org.jcdev.stockflow.backend.dto.actualizardto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.incidencia.EstadoIncidencia;

@Getter
@Setter
public class ActualizarIncidenciaDto {

    @Size(max = 255, message = "La descripcion no debe superar los 255 caracteres")
    private String descripcion;
    private EstadoIncidencia estadoIncidencia;
}
