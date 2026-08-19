package org.jcdev.stockflow.backend.mapper;

import org.jcdev.stockflow.backend.dto.responses.UsuarioResponsesDto;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component // registramos el mapper como un componente de Spring para poder inyectarlo en otros servicios
public class UsuarioMapper {

    public UsuarioResponsesDto toResponsesDto(Usuario usuarios) {

        return new UsuarioResponsesDto(
                usuarios.getId(),
                usuarios.getNombre(),
                usuarios.getEmail(),
                usuarios.getTelefono(),
                usuarios.getRol(),
                usuarios.isActivo()
        );
    }
}
