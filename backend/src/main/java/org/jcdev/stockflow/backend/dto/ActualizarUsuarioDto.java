package org.jcdev.stockflow.backend.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.Rol;

@Getter
@Setter
public class ActualizarUsuarioDto {

    @Size(message = "El nombre no puede superar los 100 caracteres", max = 100)
    private String nombre;
    @Email(message = "El formato del email no es valido")
    private String email;
    @Pattern(
            regexp = "^[0-9]{9}$",
            message = "El telefono debe tener 9 digitos"
    )
    private String telefono;
    @Size(min = 8,max = 100, message = "La contraseña adebe tener entre 8 y 100 caracteres")
    private String password;
    @Column(nullable = false)
    private Boolean activo;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;
}
