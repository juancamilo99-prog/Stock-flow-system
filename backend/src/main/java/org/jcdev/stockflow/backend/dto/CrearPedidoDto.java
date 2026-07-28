package org.jcdev.stockflow.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.entity.Empresa;

@Getter
@Setter
public class CrearPedidoDto {

    /* REGLAS DEL NEGOCIO
    * 1. todos los pedidos pertenece a una empresa.
    * 2. todos los pedidos nace en estado PENDIENTE.
    * 3. El estado inicial nunca puede enviarlo el cliente.
    * 4. La fecha del pedido la genera el sistema.
    * 5. Un pedido debe tener al menos un una observacion antes de poder confirmarse.
    * 6. Un pedido recibido no puede volver a estado pendiente.
    * 7. Un pedido cancelado no puede recibirse. */

    @NotBlank(message = "La observacion es obligatoria")
    @Size(max = 250, message = "Las observacion no puede superar los 250 caracteres")
    private String observaciones;
    @NotNull(message = "La empresa es obligatoria")
    private Long idEmpresa;
    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;
}
