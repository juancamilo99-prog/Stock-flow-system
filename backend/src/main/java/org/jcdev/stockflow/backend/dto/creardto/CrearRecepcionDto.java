package org.jcdev.stockflow.backend.dto.creardto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearRecepcionDto {

    /* REGLAS DE NEGOCIO
    * 1. toda recepcion pertenece a un pedido
    * 2. solo puede existir una recepcion para pedidos pendientes o parciales
    * 3. la fecha la genera el sistema
    * 4. el estado inicial siempre es pendiente
    */

    @Size(max = 255, message = "La observacion no debe superar los 255 caracteres")
    private String observaciones;

    @NotNull(message = "La empresa es obligatoria")
    private Long idEmpresa;
    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;
    @NotNull(message = "El pedido es obligatorio")
    private Long idPedido;
}
