package org.jcdev.stockflow.backend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jcdev.stockflow.backend.enums.usuario.Rol;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioResponsesDto {

    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private Rol rol;
    private boolean activo;
}
