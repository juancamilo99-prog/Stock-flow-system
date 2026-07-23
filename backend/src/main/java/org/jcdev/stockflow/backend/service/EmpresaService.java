package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.entity.Empresa;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.repository.EmpresaRepository;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final ProductoRepository productoRepository;

    public EmpresaService(EmpresaRepository empresaRepository, ProductoRepository productoRepository) {
        this.empresaRepository = empresaRepository;
        this.productoRepository = productoRepository;
    }

    //obtener todas las empresas
    public List<Empresa> obtenerEmpresas(){
        return empresaRepository.findAll();
    }

    //obtener productos por empresa
    public List<Producto> obtenerProductosPorEmpresa(Long idEmpresa){
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() ->
                        new IllegalArgumentException("La empresa no existe: "+idEmpresa));

        return productoRepository.findByEmpresaId(empresa.getId());
    }
}
