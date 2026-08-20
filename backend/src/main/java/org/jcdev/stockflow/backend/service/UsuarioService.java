package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.config.SecurityConfig;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarUsuarioDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearUsuarioDto;
import org.jcdev.stockflow.backend.dto.responses.UsuarioResponsesDto;
import org.jcdev.stockflow.backend.entity.Auditoria;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;
import org.jcdev.stockflow.backend.enums.usuario.Rol;
import org.jcdev.stockflow.backend.mapper.UsuarioMapper;
import org.jcdev.stockflow.backend.repository.AuditoriaRepository;
import org.jcdev.stockflow.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoriaService;

    //mapper
    private final UsuarioMapper usuarioMapper;

    //configuracion security
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, AuditoriaService auditoriaService, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    //obtener todos los usuarios
    public List<UsuarioResponsesDto> obtenerTodosUsuarios(){
        List<UsuarioResponsesDto> listaUsuarios = new ArrayList<>();
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            listaUsuarios.add(usuarioMapper.toResponsesDto(usuario));
        }
        return listaUsuarios;
    }

    //obtener usuario por identificador
    public UsuarioResponsesDto obtenerUsuarioPorId(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(
                ()-> new IllegalArgumentException("El usuario no existe")
        );

        return usuarioMapper.toResponsesDto(usuario);
    }

    //obtener usuarios desactivados
    public List<UsuarioResponsesDto> obtenerUsuariosDesactivados(){
        List <Usuario> usuarios = usuarioRepository.findByActivoFalse(false);
        List<UsuarioResponsesDto> listaUsuarios = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            listaUsuarios.add(usuarioMapper.toResponsesDto(usuario));
        }
        return listaUsuarios;
    }

    //obtener usuarios activos
    public List<UsuarioResponsesDto> obtenerUsuariosActivos(){
        List<Usuario> usuarios = usuarioRepository.findByActivoTrue(true);
        List<UsuarioResponsesDto> listaUsuarios = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            listaUsuarios.add(usuarioMapper.toResponsesDto(usuario));
        }
        return listaUsuarios;
    }

    //crear un usuario
    @Transactional
    public UsuarioResponsesDto crearUsuario(CrearUsuarioDto crearUsuarioDto){

        if (usuarioRepository.existsByEmail(crearUsuarioDto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        //usamos el metodo inyectado por spring y guardamos un hash
        String passwordHash = passwordEncoder.encode(crearUsuarioDto.getPassword());

        Usuario usuario = new Usuario();

        usuario.setNombre(crearUsuarioDto.getNombre());
        usuario.setEmail(crearUsuarioDto.getEmail());
        usuario.setTelefono(crearUsuarioDto.getTelefono());
        usuario.setPassword(passwordHash);
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

        return usuarioMapper.toResponsesDto(usuario);
    }

    //actualizar un usuario
    //aqui implementaremos la regla de negocio, para activar o desactivar un usuario
    @Transactional
    public UsuarioResponsesDto actualizarUsuario(Long idUsuario, ActualizarUsuarioDto actualizarUsuarioDto){
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
            //matches compara el password en texto plano recibido con el hash ya guardado en la bd,
            //sin desencriptar el hash, para saber si es la misma contraseña o una distinta
            boolean passwordNothash = passwordEncoder.matches(actualizarUsuarioDto.getPassword(), usuario.getPassword());
            if (!passwordNothash){
                String passwordHash = passwordEncoder.encode(actualizarUsuarioDto.getPassword());
                usuario.setPassword(passwordHash);
                detectarCambio = true;
            }
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
        return usuarioMapper.toResponsesDto(usuario);
    }

    // eliminar un usuario
    /* Los usuarios se desactivan en lugar de eliminarse, para preservar la integridad y trazabilidad historicas */
    @Transactional
    public Usuario eliminarUsuario(Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(
                () -> new IllegalArgumentException("El usuario no existe")
        );
        usuarioRepository.delete(usuario);
        //TODO se refactorizara cuando implementemos Spring Security
        auditoriaService.registrarAuditoria(
                TipoAccion.ELIMINAR,
                EntidadAuditoria.USUARIO,
                "Se ha eliminado el usuario",
                usuario.getId(),
                usuario
        );
        return usuario;
    }
}
