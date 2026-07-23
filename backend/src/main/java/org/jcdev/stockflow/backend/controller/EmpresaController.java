package org.jcdev.stockflow.backend.controller;

import org.jcdev.stockflow.backend.entity.Empresa;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.repository.EmpresaRepository;
import org.jcdev.stockflow.backend.service.EmpresaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
