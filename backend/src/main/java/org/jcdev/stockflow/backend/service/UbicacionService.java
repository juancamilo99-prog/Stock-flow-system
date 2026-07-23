package org.jcdev.stockflow.backend.service;

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
}
