package org.jcdev.stockflow.backend.dto.creardto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.tarea.PrioridadTarea;
import org.jcdev.stockflow.backend.enums.tarea.TipoTarea;

@Getter
@Setter
public class CrearTareaDto {
    /*
     * REGLAS DE NEGOCIO:
     * 1. Toda tarea debe tener tipo, descripción, prioridad y usuario.
     * 2. La fecha de creación la genera el sistema.
     * 3. Toda tarea nace en estado pendiente.
     * 4. Debe estar asociada a un pedido o una recepción.
     * 5. Si tiene recepción, el pedido se obtiene automáticamente.
     * 6. Una tarea completada no puede cambiar de estado.
     * 7. Una tarea en proceso no puede volver a pendiente.
     * 9. Las tareas completadas no se eliminan.
     */

    @NotNull(message = "El tipo de tarea es obligatoria")
    private TipoTarea tipoTarea;
    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
    private String descripcion;
    @NotNull(message = "La prioridad de la tarea es obligatoria")
    private PrioridadTarea prioridadTarea;

    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;
    private Long idRecepcion;
    private Long idPedido;
}
