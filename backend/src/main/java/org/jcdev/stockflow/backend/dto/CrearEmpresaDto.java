package org.jcdev.stockflow.backend.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.TipoEmpresa;

@Getter
@Setter
public class CrearEmpresaDto {

    /*REGLAS DE NEGOCIO:
    * 1. Nombre obligatorio
    * 2. Email obligatorio y valido
    * 3. Telefono opcional debe tener 9 digitos (español)
    * 4. tipo obligatorio (PROVEEDOR O CLIENTE)
    * 5. Activo por defecto
    * 6. No puede eliminarse si tiene productos, pedidos o recepciones asociadas
    * 7. se recomienda desactivarla antes que eliminarla*/

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
}
