package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.dto.creardto.CrearUbicacionDto;
import org.jcdev.stockflow.backend.entity.Categoria;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.entity.Ubicacion;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.jcdev.stockflow.backend.repository.UbicacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UbicacionService {

    private final UbicacionRepository ubicacionRepository;
    private final ProductoRepository productoRepository;

    public UbicacionService(UbicacionRepository ubicacionRepository, ProductoRepository productoRepository) {
        this.ubicacionRepository = ubicacionRepository;
        this.productoRepository = productoRepository;
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
    public Ubicacion crearUbicacion(CrearUbicacionDto crearUbicacionDto){
        String codigo = crearUbicacionDto.getCodigo().trim().toUpperCase();
        String descripcion = crearUbicacionDto.getDescripcion().trim();
        if (ubicacionRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new IllegalArgumentException("La ubicacion ya existe: "+codigo);
        }
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setCodigo(codigo);
        ubicacion.setDescripcion(descripcion);
        return ubicacionRepository.save(ubicacion);
    }
}
