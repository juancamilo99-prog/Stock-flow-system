package org.jcdev.stockflow.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearUsuarioDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(message = "El nombre no puede superar los 100 caracteres", max = 100)
    private String nombre;
    @Email(message = "El formato del email no es valido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @NotBlank(message = "El telefono es obligatorio")
    @Size(max = 9)
    @Pattern(
            regexp = "^[0-9]{9}$",
            message = "El telefono debe tener 9 digitos"
    )
    private String telefono;
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8,max = 100, message = "La contraseña adebe tener entre 8 y 100 caracteres")
    private String password;
}
