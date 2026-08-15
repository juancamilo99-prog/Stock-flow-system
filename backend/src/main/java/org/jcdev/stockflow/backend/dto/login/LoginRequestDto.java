package org.jcdev.stockflow.backend.dto.login;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class LoginRequestDto {

    @Email(message = "El formato del email no es valido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @NotBlank(message = "El password es obligatorio")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String password;
}
