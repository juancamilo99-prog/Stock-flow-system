package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarEmpresaDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearEmpresaDto;
import org.jcdev.stockflow.backend.entity.Empresa;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;
import org.jcdev.stockflow.backend.enums.empresa.TipoEmpresa;
import org.jcdev.stockflow.backend.repository.EmpresaRepository;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.jcdev.stockflow.backend.service.security.AuthorizationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final ProductoRepository productoRepository;

    //servicios
    private final AuditoriaService auditoriaService;

    //autorizaciones
    private final AuthorizationService authorizationService;

    public EmpresaService(EmpresaRepository empresaRepository, ProductoRepository productoRepository, AuditoriaService auditoriaService, AuthorizationService authorizationService) {
        this.empresaRepository = empresaRepository;
        this.productoRepository = productoRepository;
        this.auditoriaService = auditoriaService;
        this.authorizationService = authorizationService;
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
                authorizationService.obtenerUsuarioAutenticado()
        );
        return empresa;
    }

    //actualizar una empresa
    @Transactional
    public Empresa actualizarEmpresa(Long idEmpresa, ActualizarEmpresaDto actualizarEmpresaDto){

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(
                        () -> new IllegalArgumentException("La empresa no existe: "+idEmpresa)
                );

        boolean detectarCambio = false;
        if (actualizarEmpresaDto.getNombre() != null){
            String nombreNuevo = actualizarEmpresaDto.getNombre().trim();
            String nombreActual = empresa.getNombre().trim();
            if (nombreNuevo.isBlank()){
                throw new IllegalArgumentException("El nombre no puede estar vacio");
            }
            if (!nombreNuevo.equalsIgnoreCase(nombreActual)){
                if (empresaRepository.existsByNombreIgnoreCaseAndIdNot(nombreNuevo,idEmpresa)){
                    throw new IllegalArgumentException("Ya existe una empresa con ese nombre!");
                }
                empresa.setNombre(nombreNuevo);
                detectarCambio = true;
            }
        }
        if (actualizarEmpresaDto.getEmail() != null){
            String emailNuevo = actualizarEmpresaDto.getEmail().trim().toLowerCase();
            String emailActual = empresa.getEmail().trim().toLowerCase();
            if (emailNuevo.isBlank()){
                throw new IllegalArgumentException("El email no puede estar vacio");
            }
            if (!emailNuevo.equalsIgnoreCase(emailActual)){
                if (empresaRepository.existsByEmailIgnoreCaseAndIdNot(emailNuevo,idEmpresa)){
                    throw new IllegalArgumentException("Ya existe una empresa con ese email!");
                }
                empresa.setEmail(emailNuevo);
                detectarCambio = true;
            }
        }
        if (actualizarEmpresaDto.getTelefono() != null){
            String telefonoNuevo = actualizarEmpresaDto.getTelefono().trim();
            String telefonoActual = empresa.getTelefono().trim();
            if (telefonoNuevo.isBlank()){
                throw new IllegalArgumentException("El telefono no puede estar vacio");
            }
            if (!telefonoNuevo.equals(telefonoActual)){
                empresa.setTelefono(telefonoNuevo);
                detectarCambio = true;
            }
        }
        if (actualizarEmpresaDto.getDireccion() != null){
            String direccionNueva = actualizarEmpresaDto.getDireccion().trim();
            String direccionActual = empresa.getDireccion().trim();
            if (direccionNueva.isBlank()){
                throw new IllegalArgumentException("La direccion no puede estar vacia");
            }
            if (!direccionNueva.equals(direccionActual)){
                empresa.setDireccion(direccionNueva);
                detectarCambio = true;
            }
        }
        if (actualizarEmpresaDto.getTipoEmpresa() != null){
            TipoEmpresa tipoEmpresaActual = empresa.getTipoEmpresa();
            TipoEmpresa tipoEmpresaNuevo = actualizarEmpresaDto.getTipoEmpresa();
            if (tipoEmpresaNuevo != tipoEmpresaActual){
                empresa.setTipoEmpresa(tipoEmpresaNuevo);
                detectarCambio = true;
            }
        }
        if (actualizarEmpresaDto.getActivo() != null){
            boolean activoNuevo = actualizarEmpresaDto.getActivo();
            boolean activoActual = empresa.getActivo();
            if (activoNuevo != activoActual){
                empresa.setActivo(activoNuevo);
                detectarCambio = true;
            }
        }

        if (detectarCambio){
            empresa = empresaRepository.save(empresa);
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.EMPRESA,
                    "Se ha actualizado una empresa",
                    empresa.getId(),
                    authorizationService.obtenerUsuarioAutenticado()
            );
        }else {
            throw new IllegalArgumentException("No se detecto ningun cambio");
        }
        return empresa;
    }

    public Empresa eliminarEmpresa(Long idEmpresa){
        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(
                        () -> new IllegalArgumentException("La empresa no existe: "+idEmpresa)
                );
        empresaRepository.delete(empresa);
        auditoriaService.registrarAuditoria(
                TipoAccion.ELIMINAR,
                EntidadAuditoria.EMPRESA,
                "Se ha eliminado la empresa: "+empresa.getNombre(),
                empresa.getId(),
                authorizationService.obtenerUsuarioAutenticado()
        );
        return empresa;
    }

}
