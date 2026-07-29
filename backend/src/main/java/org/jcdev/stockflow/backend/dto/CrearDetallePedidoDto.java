package org.jcdev.stockflow.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearDetallePedidoDto {

    /*REGLAS DE NEGOCIO
    * 1. un detalle pertenece a un pedido-
    * 2. un detalle pertenece a un producto-
    * 3. la cantidad solicitada no puede ser menor que 0-
    * 4. no se puede añadir un detalle a un pedido cancelado-
    * 5. no se puede añadir un detalle a un pedido recibido-
    * 6. no puede existir el mismo producto dos veces en un mismo pedido
    * 7. la eliminacion de un detalle modifica unicamente el contenido del pedido, no el stock*/

    @PositiveOrZero(message = "La cantidad debe ser mayor a 0")
    private Integer cantidadSolicitada;
    @NotNull(message = "El pedido es obligatorio")
    private Long idPedido;
    @NotNull(message = "El producto es obligatorio")
    private Long idProducto;
}
