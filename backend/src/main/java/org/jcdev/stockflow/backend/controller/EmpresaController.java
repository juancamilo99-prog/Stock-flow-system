package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarEmpresaDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearEmpresaDto;
import org.jcdev.stockflow.backend.entity.Empresa;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.service.EmpresaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    public ResponseEntity<List<Empresa>> obtenerEmpresas(){
        List<Empresa> empresas = empresaService.obtenerEmpresas();
        return ResponseEntity.ok(empresas);
    }

    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping(path = "/{idEmpresa}/productos")
    public ResponseEntity<List<Producto>> obtenerProductosPorEmpresa(@PathVariable Long idEmpresa) {
        List<Producto> productos = empresaService.obtenerProductosPorEmpresa(idEmpresa);
        return ResponseEntity.ok(productos);
    }

    @PreAuthorize("hasRole('COORDINADOR')")
    @PostMapping
    public ResponseEntity<Empresa> crearEmpresa(@Valid @RequestBody CrearEmpresaDto crearEmpresaDto) {
        Empresa empresa = empresaService.crearEmpresa(crearEmpresaDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(empresa);
    }

    @PreAuthorize("hasRole('COORDINADOR')")
    @PatchMapping(path = "/{idEmpresa}")
    public ResponseEntity<Empresa> actualizarEmpresa(@PathVariable Long idEmpresa,@Valid @RequestBody ActualizarEmpresaDto actualizarEmpresaDto) {
        Empresa empresa = empresaService.actualizarEmpresa(idEmpresa, actualizarEmpresaDto);
        return ResponseEntity.ok(empresa);
    }

    @PreAuthorize("hasRole('COORDINADOR')")
    @DeleteMapping(path = "/{idEmpresa}")
    public ResponseEntity<Empresa> eliminarEmpresa(@PathVariable Long idEmpresa) {
        Empresa empresa = empresaService.eliminarEmpresa(idEmpresa);
        return ResponseEntity.ok(empresa);
    }
}
