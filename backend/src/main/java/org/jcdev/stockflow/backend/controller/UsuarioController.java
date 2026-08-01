package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarUsuarioDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearUsuarioDto;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> obtenerTodosUsuarios(){
        return usuarioService.obtenerTodosUsuarios();
    }

    @GetMapping(path = "/{idUsuario}")
    public Usuario obtenerUsuarioPorId(@PathVariable Long idUsuario){
        return usuarioService.obtenerUsuarioPorId(idUsuario);
    }

    @GetMapping(path = "/desactivos")
    public List<Usuario> obtenerUsuariosDesactivados(){
        return usuarioService.obtenerUsuariosDesactivados();
    }

    @GetMapping(path = "/activos")
    public List<Usuario> obtenerUsuariosActivos(){
        return usuarioService.obtenerUsuariosActivos();
    }

    @PostMapping
    public Usuario crearUsuario(@Valid @RequestBody CrearUsuarioDto crearUsuarioDto){
        return usuarioService.crearUsuario(crearUsuarioDto);
    }

    @PatchMapping(path = "/{idUsuario}")
    public Usuario actualizarUsuaio(@PathVariable Long idUsuario, @Valid @RequestBody ActualizarUsuarioDto actualizarUsuarioDto){
        return usuarioService.actualizarUsuario(idUsuario,actualizarUsuarioDto);
    }

    @DeleteMapping(path = "/{idUsuario}")
    public Usuario eliminarUsuario(@PathVariable Long idUsuario){
        return usuarioService.eliminarUsuario(idUsuario);
    }
}
