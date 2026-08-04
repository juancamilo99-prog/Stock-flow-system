package org.jcdev.stockflow.backend.dto.creardto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.incidencia.TipoIncidencia;

@Getter
@Setter
public class CrearIncidenciaDto {

    /*reglas de negocio
    * 1. la fecha la generara el sistema
    * 2. el estado inicial siempre sera pendiente
    * 3. debe tener descripcion
    * 4. debe tener tipo
    * 5. debe tener registrado quien la crea
    * 6. solo se podra actualizar la descripcion y el estado
    * 7. una incidencia no se puede borrar.*/

    @Enumerated(EnumType.STRING)
    @NotNull(message = "El tipo de incidencia es obligatorio")
    private TipoIncidencia tipoIncidencia;
    @Size(max = 255, message = "La descripcion no debe superar los 255 caracteres")
    @NotBlank(message = "la descripcion es obligatoria")
    private String descripcion;

    private Long idProducto;
    @NotNull(message = "El usuario es obligatorio")
    private Long idUsuario;
    private Long idRecepcion;
}
