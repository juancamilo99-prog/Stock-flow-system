package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarEmpresaDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearEmpresaDto;
import org.jcdev.stockflow.backend.entity.Empresa;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;
import org.jcdev.stockflow.backend.repository.EmpresaRepository;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final ProductoRepository productoRepository;

    //servicios
    private final AuditoriaService auditoriaService;

    public EmpresaService(EmpresaRepository empresaRepository, ProductoRepository productoRepository, AuditoriaService auditoriaService) {
        this.empresaRepository = empresaRepository;
        this.productoRepository = productoRepository;
        this.auditoriaService = auditoriaService;
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

    //crear una empresa
    @Transactional
    public Empresa crearEmpresa(CrearEmpresaDto crearEmpresaDto){
        String nombre = crearEmpresaDto.getNombre().trim();
        String email = crearEmpresaDto.getEmail().trim().toLowerCase();
        String direccion = crearEmpresaDto.getDireccion().trim();
        String telefono = crearEmpresaDto.getTelefono().trim();
        if (empresaRepository.existsByNombreIgnoreCase(nombre)){
            throw new IllegalArgumentException("Ya existe una empresa con ese nombre!");
        }
        if (empresaRepository.existsByEmailIgnoreCase(email)){
            throw new IllegalArgumentException("Ya existe una empresa con ese email!");
        }
        Empresa empresa = new Empresa();
        empresa.setNombre(nombre);
        empresa.setEmail(email);
        empresa.setTelefono(telefono);
        empresa.setDireccion(direccion);
        empresa.setTipoEmpresa(crearEmpresaDto.getTipoEmpresa());

        empresa = empresaRepository.save(empresa);

        auditoriaService.registrarAuditoria(
                TipoAccion.CREAR,
                EntidadAuditoria.EMPRESA,
                "Se ha creado una empresa nueva: "+empresa.getNombre(),
                empresa.getId(),
                null
        );
        return empresa;
    }

    //actualizar una empresa
    public Empresa actualizarEmpresa(Long idEmpresa, ActualizarEmpresaDto actualizarEmpresaDto){

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(
                        () -> new IllegalArgumentException("La empresa no existe: "+idEmpresa)
                );

        if (actualizarEmpresaDto.getEmail() != null && empresaRepository.existsByEmailAndIdNot(actualizarEmpresaDto.getEmail(),idEmpresa)){
            throw new IllegalArgumentException("Ya existe una empresa con ese email!");
        }

        if (actualizarEmpresaDto.getNombre() != null && empresaRepository.existsByNombreAndIdNot(actualizarEmpresaDto.getNombre(),idEmpresa)){
            throw new IllegalArgumentException("Ya existe una empresa con ese nombre!");
        }

        if (actualizarEmpresaDto.getNombre() != null){
            empresa.setNombre(actualizarEmpresaDto.getNombre());
        }
        if (actualizarEmpresaDto.getEmail() != null){
            empresa.setEmail(actualizarEmpresaDto.getEmail());
        }
        if (actualizarEmpresaDto.getTelefono() != null){
            empresa.setTelefono(actualizarEmpresaDto.getTelefono());
        }
        if (actualizarEmpresaDto.getDireccion() != null){
            empresa.setDireccion(actualizarEmpresaDto.getDireccion());
        }
        if (actualizarEmpresaDto.getTipoEmpresa() != null){
            empresa.setTipoEmpresa(actualizarEmpresaDto.getTipoEmpresa());
        }
        if (actualizarEmpresaDto.getActivo() != null){
            empresa.setActivo(actualizarEmpresaDto.getActivo());
        }

        return empresaRepository.save(empresa);

    }

    public Empresa eliminarEmpresa(Long idEmpresa){
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(
                        () -> new IllegalArgumentException("La empresa no existe: "+idEmpresa)
                );
        empresaRepository.delete(empresa);
        return empresa;
    }

}
