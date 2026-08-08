package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarUbicacionDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearUbicacionDto;
import org.jcdev.stockflow.backend.entity.*;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.jcdev.stockflow.backend.repository.UbicacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final ProductoRepository productoRepository;
    private final AuditoriaService auditoriaService;

    public UbicacionService(UbicacionRepository ubicacionRepository, ProductoRepository productoRepository, AuditoriaService auditoriaService) {
        this.ubicacionRepository = ubicacionRepository;
        this.productoRepository = productoRepository;
        this.auditoriaService = auditoriaService;
    }

    //obtener todas las ubicaciones
    public List<Ubicacion> obtenerUbicaciones(){
        return ubicacionRepository.findAll();
    }

    //obtener productos por ubicacion
    public List<Producto> obtenerProductosPorUbicacion(Long idUbicacion){
        Ubicacion ubicacion = ubicacionRepository.findById(idUbicacion)
                .orElseThrow(() ->
                        new IllegalArgumentException("La ubicacion no existe"+idUbicacion
                        ));
        return productoRepository.findByUbicacionId(ubicacion.getId());
    }

    //crear una ubicacion
    @Transactional
    public Ubicacion crearUbicacion(CrearUbicacionDto crearUbicacionDto){
        String codigo = crearUbicacionDto.getCodigo().trim().toUpperCase();
        String descripcion = crearUbicacionDto.getDescripcion().trim();
        if (ubicacionRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new IllegalArgumentException("La ubicacion ya existe: "+codigo);
        }
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setCodigo(codigo);
        ubicacion.setDescripcion(descripcion);
        ubicacion = ubicacionRepository.save(ubicacion);

        //TODO el usuario se obtendra del SecurityContext
        //TODO Volver a hacer obligatorio id_usuario en la base de datos (NOT NULL) una vez todas las auditorías tengan un actor real.
        auditoriaService.registrarAuditoria(
                TipoAccion.CREAR,
                EntidadAuditoria.UBICACION,
                "Se ha creado una nueva ubicacion: "+ubicacion.getId(),
                ubicacion.getId(),
                null
        );
        return ubicacion;
    }

    //actualizar una ubicacion
    @Transactional
    public Ubicacion actualizarUbicacion(Long idUbicacion,ActualizarUbicacionDto actualizarUbicacionDto){
        Ubicacion ubicacion = ubicacionRepository.findById(idUbicacion)
                .orElseThrow(() -> new IllegalArgumentException("La ubicacion no existe"+idUbicacion));
        boolean detectarCambio = false;
        if (actualizarUbicacionDto.getDescripcion() != null) {
            String descripcionNueva = actualizarUbicacionDto.getDescripcion().trim();
            if (descripcionNueva.isBlank()){
                throw new IllegalArgumentException("La descripcion no puede estar vacia");
            }
            if (ubicacion.getDescripcion().equalsIgnoreCase(descripcionNueva)) {
                throw new IllegalArgumentException("La nueva descripcion debe ser diferente de la actual");
            }
            ubicacion.setDescripcion(descripcionNueva);
            detectarCambio = true;
        }

        if (actualizarUbicacionDto.getCodigo() != null) {
            String codigoNuevo = actualizarUbicacionDto.getCodigo().trim().toUpperCase();
            if (codigoNuevo.isBlank()){
                throw new IllegalArgumentException("El codigo no puede estar vacia");
            }
            if (ubicacion.getCodigo().equalsIgnoreCase(codigoNuevo)) {
                throw new IllegalArgumentException("El nuevo codigo no puede ser el mismo que el actual");
            }
            if (ubicacionRepository.existsByCodigoIgnoreCase(codigoNuevo)) {
                throw new IllegalArgumentException("La ubicacion ya existe: "+codigoNuevo);
            }
            ubicacion.setCodigo(codigoNuevo);
            detectarCambio = true;
        }

        if (detectarCambio) {
            ubicacion = ubicacionRepository.save(ubicacion);
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.UBICACION,
                    "Se ha actualizado una ubicacion: "+ubicacion.getId(),
                    ubicacion.getId(),
                    null
            );
        }else {
            throw new IllegalStateException("No se detecto ningun cambio");
        }
        return ubicacion;
    }
}
