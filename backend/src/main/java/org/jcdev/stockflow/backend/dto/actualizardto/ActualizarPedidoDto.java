package org.jcdev.stockflow.backend.dto.actualizardto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.EstadoPedido;

@Getter
@Setter
public class ActualizarPedidoDto {

    private EstadoPedido estadoPedido;
    @Size(max = 250, message = "Las observacion no puede superar los 250 caracteres")
    private String observaciones;

}
