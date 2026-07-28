package org.jcdev.stockflow.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.TipoEmpresa;

@Getter
@Setter
public class ActualizarEmpresaDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2 , max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;
    @Email(message = "El formato del email no es valido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @Pattern(
            regexp = "^[0-9]{9}$",
            message = "El telefono debe tener 9 digitos"
    )
    private String telefono;
    @NotBlank(message = "La direccion es obligatoria")
    private String direccion;
    @NotNull(message = "El tipo de empresa es obligatorio")
    private TipoEmpresa tipoEmpresa;
    private Boolean activo;
}
