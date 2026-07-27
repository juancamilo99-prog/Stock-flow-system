package org.jcdev.stockflow.backend.controller;

import org.jcdev.stockflow.backend.entity.Auditoria;
import org.jcdev.stockflow.backend.service.AuditoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(AuditoriaService auditoriaService) {
        this.auditoriaService = auditoriaService;
    }

    @GetMapping
    public List<Auditoria>  obtenerAuditorias() {
        return auditoriaService.obtenerAuditorias();
    }

    @GetMapping(path = "/{idUsuario}/usuario")
    public List<Auditoria> obtenerAuditoriasByUsuarioId(@PathVariable Long idUsuario) {
        return auditoriaService.obtenerAuditoriasByUsuarioId(idUsuario);
    }
}
