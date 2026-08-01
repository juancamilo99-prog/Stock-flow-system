package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarUsuarioDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearUsuarioDto;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.enums.Rol;
import org.jcdev.stockflow.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    //obtener todos los usuarios
    public List<Usuario> obtenerTodosUsuarios(){
        return usuarioRepository.findAll();
    }

    //obtener usuario por identificador
    public Usuario obtenerUsuarioPorId(Long idUsuario){
        return usuarioRepository.findById(idUsuario).orElseThrow(
                ()-> new IllegalArgumentException("El usuario no existe")
        );
    }

    //obtener usuarios desactivados
    public List<Usuario> obtenerUsuariosDesactivados(){
        return usuarioRepository.findByActivoFalse(false);
    }

    //obtener usuarios activos
    public List<Usuario> obtenerUsuariosActivos(){
        return usuarioRepository.findByActivoTrue(true);
    }

    //crear un usuario
    public Usuario crearUsuario(CrearUsuarioDto crearUsuarioDto){

        if (usuarioRepository.existsByEmail(crearUsuarioDto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(crearUsuarioDto.getNombre());
        usuario.setEmail(crearUsuarioDto.getEmail());
        usuario.setTelefono(crearUsuarioDto.getTelefono());
        usuario.setPassword(crearUsuarioDto.getPassword());
        usuario.setRol(Rol.OPERARIO);
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    //actualizar un usuario
    //aqui implementaremos la regla de negocio, para activar o desactivar un usuario
    public Usuario actualizarUsuario(Long idUsuario, ActualizarUsuarioDto actualizarUsuarioDto){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new IllegalArgumentException("El usuario no existe"));

        if (actualizarUsuarioDto.getNombre() != null){
            usuario.setNombre(actualizarUsuarioDto.getNombre());
        }
        if (actualizarUsuarioDto.getEmail() != null){
            usuario.setEmail(actualizarUsuarioDto.getEmail());
        }
        if (actualizarUsuarioDto.getTelefono() != null){
            usuario.setTelefono(actualizarUsuarioDto.getTelefono());
        }
        if (actualizarUsuarioDto.getPassword() != null){
            usuario.setPassword(actualizarUsuarioDto.getPassword());
        }
        if (actualizarUsuarioDto.getRol() != null){
            usuario.setRol(actualizarUsuarioDto.getRol());
        }
        if (actualizarUsuarioDto.getActivo() != null){
            usuario.setActivo(actualizarUsuarioDto.getActivo());
        }

        return usuarioRepository.save(usuario);
    }

    // eliminar un usuario
    public Usuario eliminarUsuario(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new IllegalArgumentException("El usuario no existe")
        );
        usuarioRepository.delete(usuario);
        return usuario;
    }
}
