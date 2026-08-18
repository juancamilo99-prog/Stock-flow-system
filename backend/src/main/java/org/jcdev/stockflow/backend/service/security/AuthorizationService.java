package org.jcdev.stockflow.backend.service.security;

import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.enums.usuario.Rol;
import org.jcdev.stockflow.backend.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final UsuarioRepository usuarioRepository;

    public AuthorizationService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    //metodo para obtener el email autenticado
    public String obtenerEmailUsuarioAutenticado() {
        //objeto que repreenta la autenticacion actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public boolean esUsuarioActual(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        return usuario.getEmail().equals(obtenerEmailUsuarioAutenticado());
    }

    public boolean esOperario(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        return usuario.getRol() == Rol.OPERARIO;
    }
}
