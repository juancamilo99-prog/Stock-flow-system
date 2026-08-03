package org.jcdev.stockflow.backend.dto.actualizardto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarUbicacionDto {

    @Size(max = 20, message = "El codigo no puede superar los 20 caracteres")
    @Pattern(
            regexp = "^[A-Z]-\\d{2}-\\d{2}$",
            message = "El código debe tener el formato A-01-01"
    )
    private String codigo;
    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
    private String descripcion;
}
