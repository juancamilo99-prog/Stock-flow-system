package org.jcdev.stockflow.backend.dto.creardto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearUbicacionDto {

    /*
     * REGLAS DE NEGOCIO:
     * 1. El código de ubicación es obligatorio y único.
     * 2. El código debe seguir el formato definido para el almacén.
     * 3. La descripción es obligatoria.
     * 4. El código se almacena sin espacios y en mayúsculas.
     * 5. No se puede eliminar una ubicación con productos asociados.
     * 6. Al actualizar, el nuevo código debe ser diferente y no estar repetido.
     */

    @NotBlank(message = "El codgio es obligatorio.")
    @Size(max = 20, message = "El codigo no puede superar los 20 caracteres")
    @Pattern(
            regexp = "^[A-Z]-\\d{2}-\\d{2}$",
            message = "El código debe tener el formato A-01-01"
    )
    private String codigo;
    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
    private String descripcion;
}
