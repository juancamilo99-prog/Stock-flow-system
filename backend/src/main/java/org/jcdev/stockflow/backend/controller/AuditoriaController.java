package org.jcdev.stockflow.backend.controller;

import org.jcdev.stockflow.backend.entity.Auditoria;
import org.jcdev.stockflow.backend.service.AuditoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('COORDINADOR')")
    @GetMapping
    public ResponseEntity<List<Auditoria>> obtenerAuditorias() {
        List<Auditoria> auditorias = auditoriaService.obtenerAuditorias();
        return ResponseEntity.ok(auditorias);
    }

    @PreAuthorize("hasRole('COORDINADOR')")
    @GetMapping(path = "/{idUsuario}/usuario")
    public ResponseEntity<List<Auditoria>> obtenerAuditoriasByUsuarioId(@PathVariable Long idUsuario) {
        List<Auditoria> auditorias = auditoriaService.obtenerAuditoriasByUsuarioId(idUsuario);
        return ResponseEntity.status(HttpStatus.OK)
                .body(auditorias);
    }
}
