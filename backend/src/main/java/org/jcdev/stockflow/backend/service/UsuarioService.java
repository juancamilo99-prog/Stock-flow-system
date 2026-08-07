package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarUsuarioDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearUsuarioDto;
import org.jcdev.stockflow.backend.entity.Auditoria;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;
import org.jcdev.stockflow.backend.enums.usuario.Rol;
import org.jcdev.stockflow.backend.repository.AuditoriaRepository;
import org.jcdev.stockflow.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaService auditoriaService;

    public UsuarioService(UsuarioRepository usuarioRepository, AuditoriaRepository auditoriaRepository, AuditoriaService auditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.auditoriaService = auditoriaService;
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
    @Transactional
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
        usuario =  usuarioRepository.save(usuario);

        //crear el servicio de auditoria
        //TODO refactorizar cuando implementemos Spring Security
        auditoriaService.registrarAuditoria(
                TipoAccion.CREAR,
                EntidadAuditoria.USUARIO,
                "Se ha creado un usuario nuevo: "+usuario.getNombre(),
                usuario.getId(),
                usuario

        );
        return usuario;
    }

    //actualizar un usuario
    //aqui implementaremos la regla de negocio, para activar o desactivar un usuario
    @Transactional
    public Usuario actualizarUsuario(Long idUsuario, ActualizarUsuarioDto actualizarUsuarioDto){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new IllegalArgumentException("El usuario no existe"));

        boolean detectarCambio = false;

        if (actualizarUsuarioDto.getNombre() != null){
            String nombreActual = usuario.getNombre().trim();
            String nombreNuevo = actualizarUsuarioDto.getNombre().trim();
            if (!nombreActual.equalsIgnoreCase(nombreNuevo)){
                usuario.setNombre(nombreNuevo);
                detectarCambio = true;
            }
        }
        if (actualizarUsuarioDto.getEmail() != null){
            String emailActual = usuario.getEmail().trim();
            String emailNuevo = actualizarUsuarioDto.getEmail().trim();
            if (!emailActual.equalsIgnoreCase(emailNuevo)){
                if (usuarioRepository.existsByEmailIgnoreCaseAndIdNot(emailNuevo, idUsuario)){
                    throw new IllegalArgumentException("Ya existe un usuario con ese email");
                }
                usuario.setEmail(emailNuevo);
                detectarCambio = true;
            }
        }
        if (actualizarUsuarioDto.getTelefono() != null){
            String telefonoActual = usuario.getTelefono().trim();
            String telefonoNuevo = actualizarUsuarioDto.getTelefono().trim();
            if (!telefonoActual.equalsIgnoreCase(telefonoNuevo)){
                usuario.setTelefono(telefonoNuevo);
                detectarCambio = true;
            }
        }
        if (actualizarUsuarioDto.getPassword() != null){
            //TODO implementar validacion con spring secutiry + bcrypt integrados
            usuario.setPassword(actualizarUsuarioDto.getPassword());
        }
        if (actualizarUsuarioDto.getRol() != null){
            Rol rolActual = usuario.getRol();
            Rol rolNuevo = actualizarUsuarioDto.getRol();
            if (rolActual != rolNuevo){
                usuario.setRol(rolNuevo);
                detectarCambio = true;
            }
        }
        if (actualizarUsuarioDto.getActivo() != null){
            boolean activoActual = usuario.isActivo();
            boolean activoNuevo = actualizarUsuarioDto.getActivo();
            if (activoActual != activoNuevo){
                usuario.setActivo(activoNuevo);
                detectarCambio = true;
            }
        }
        if (detectarCambio){
            usuario = usuarioRepository.save(usuario);
            //crear el servicio de auditoria
            //TODO refactorizar cuando implementemos Spring Security
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.USUARIO,
                    "Se ha modificador un usuario ",
                    usuario.getId(),
                    usuario
            );
        }else {
            throw new IllegalArgumentException("No se detecto ningun cambio");
        }
        return usuario;
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
