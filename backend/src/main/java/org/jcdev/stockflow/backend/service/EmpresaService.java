package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.dto.ActualizarEmpresaDto;
import org.jcdev.stockflow.backend.dto.CrearEmpresaDto;
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

    //crear una empresa
    public Empresa crearEmpresa(CrearEmpresaDto crearEmpresaDto){

        if (empresaRepository.existsByNombre(crearEmpresaDto.getNombre())){
            throw new IllegalArgumentException("Ya existe una empresa con ese nombre!");
        }
        if (empresaRepository.existsByEmail(crearEmpresaDto.getEmail())){
            throw new IllegalArgumentException("Ya existe una empresa con ese email!");
        }
        Empresa empresa = new Empresa();
        empresa.setNombre(crearEmpresaDto.getNombre());
        empresa.setEmail(crearEmpresaDto.getEmail());
        empresa.setTelefono(crearEmpresaDto.getTelefono());
        empresa.setDireccion(crearEmpresaDto.getDireccion());
        empresa.setTipoEmpresa(crearEmpresaDto.getTipoEmpresa());

        return empresaRepository.save(empresa);
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
