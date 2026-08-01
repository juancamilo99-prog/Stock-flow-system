package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarEmpresaDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearEmpresaDto;
import org.jcdev.stockflow.backend.entity.Empresa;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.service.EmpresaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping(path = "/")
    public List<Empresa> obtenerEmpresas(){
        return empresaService.obtenerEmpresas();
    }

    @GetMapping(path = "/{idEmpresa}/productos")
    public List<Producto> obtenerProductosPorEmpresa(@PathVariable Long idEmpresa) {
        return empresaService.obtenerProductosPorEmpresa(idEmpresa);
    }

    @PostMapping
    public Empresa crearEmpresa(@Valid @RequestBody CrearEmpresaDto crearEmpresaDto) {
        return empresaService.crearEmpresa(crearEmpresaDto);
    }

    @PatchMapping(path = "/{idEmpresa}")
    public Empresa actualizarEmpresa(@PathVariable Long idEmpresa,@Valid @RequestBody ActualizarEmpresaDto actualizarEmpresaDto) {
        return empresaService.actualizarEmpresa(idEmpresa, actualizarEmpresaDto);
    }

    @DeleteMapping(path = "/{idEmpresa}")
    public Empresa eliminarEmpresa(@PathVariable Long idEmpresa) {
        return empresaService.eliminarEmpresa(idEmpresa);
    }
}
