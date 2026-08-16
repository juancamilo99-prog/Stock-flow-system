package org.jcdev.stockflow.backend.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.usuario.Rol;


@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private String email;
    private Rol role;
}
