package org.jcdev.stockflow.backend.dto.actualizardto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.tarea.EstadoTarea;
import org.jcdev.stockflow.backend.enums.tarea.PrioridadTarea;

@Getter
@Setter
public class ActualizarTareaDto {

    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
    private String descripcion;
    private PrioridadTarea prioridad;
    private EstadoTarea estado;
}
