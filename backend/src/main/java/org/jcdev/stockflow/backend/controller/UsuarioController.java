package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarUsuarioDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearUsuarioDto;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<List<Usuario>> obtenerTodosUsuarios(){
        List<Usuario> usuario = usuarioService.obtenerTodosUsuarios();
        return ResponseEntity.ok(usuario);
    }

    @PreAuthorize("hasRole('COORDINADOR') " +
            "OR @authorizationService.esUsuarioActual(#idUsuario) " +
            "OR (hasRole('ENCARGADO') AND @authorizationService.esOperario(#idUsuario))")
    @GetMapping(path = "/{idUsuario}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(@PathVariable Long idUsuario){
        Usuario usuario = usuarioService.obtenerUsuarioPorId(idUsuario);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping(path = "/desactivos")
    public ResponseEntity<List<Usuario>> obtenerUsuariosDesactivados(){
        List<Usuario> usuarios = usuarioService.obtenerUsuariosDesactivados();
        return ResponseEntity.ok(usuarios);

    }

    @GetMapping(path = "/activos")
    public ResponseEntity<List<Usuario>> obtenerUsuariosActivos(){
        List<Usuario> usuarios = usuarioService.obtenerUsuariosActivos();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@Valid @RequestBody CrearUsuarioDto crearUsuarioDto){
        Usuario usuario = usuarioService.crearUsuario(crearUsuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuario);
    }

    @PatchMapping(path = "/{idUsuario}")
    public ResponseEntity<Usuario> actualizarUsuaio(@PathVariable Long idUsuario, @Valid @RequestBody ActualizarUsuarioDto actualizarUsuarioDto){
        Usuario usuario = usuarioService.actualizarUsuario(idUsuario,actualizarUsuarioDto);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping(path = "/{idUsuario}")
    public ResponseEntity<Usuario> eliminarUsuario(@PathVariable Long idUsuario){
        Usuario usuario = usuarioService.eliminarUsuario(idUsuario);
        return ResponseEntity.ok(usuario);
    }
}
