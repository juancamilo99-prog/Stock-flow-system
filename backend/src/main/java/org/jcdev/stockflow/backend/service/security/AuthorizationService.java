package org.jcdev.stockflow.backend.service.security;

import org.jcdev.stockflow.backend.entity.Recepcion;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.enums.usuario.Rol;
import org.jcdev.stockflow.backend.repository.RecepcionRepository;
import org.jcdev.stockflow.backend.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final UsuarioRepository usuarioRepository;
    private final RecepcionRepository recepcionRepository;

    public AuthorizationService(UsuarioRepository usuarioRepository, RecepcionRepository recepcionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.recepcionRepository = recepcionRepository;
    }

    //metodo para obtener el email autenticado
    public String obtenerEmailUsuarioAutenticado() {
        //objeto que repreenta la autenticacion actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public Usuario obtenerUsuarioAutenticado() {
        String email = obtenerEmailUsuarioAutenticado();
        return usuarioRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
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

    public boolean esRecepcionDelUsuarioActual(Long idRecepcion) {
        Recepcion recepcion = recepcionRepository.findById(idRecepcion)
                .orElseThrow(() -> new IllegalArgumentException("El recepcion no existe"));
        return recepcion.getUsuario().getId().equals(obtenerUsuarioAutenticado().getId());
    }
}
