package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.entity.DetalleRecepcion;
import org.jcdev.stockflow.backend.entity.Recepcion;
import org.jcdev.stockflow.backend.repository.DetalleRecepcionRepository;
import org.jcdev.stockflow.backend.repository.RecepcionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecepcionService {

    RecepcionRepository recepcionRepository;
    DetalleRecepcionRepository detalleRecepcionRepository;

    public RecepcionService(RecepcionRepository recepcionRepository,  DetalleRecepcionRepository detalleRecepcionRepository) {
        this.recepcionRepository = recepcionRepository;
        this.detalleRecepcionRepository = detalleRecepcionRepository;
    }

    public List<Recepcion> obtenerRecepciones() {
        return recepcionRepository.findAll();
    }

    //buscar el detalle de las recepciones por identificador
    public List<DetalleRecepcion> obtenerDetalleRecepciones(Long idRecepcion) {
        recepcionRepository.findById(idRecepcion)
                .orElseThrow(() ->
                        new IllegalArgumentException("El recepcion no existe"));

        return detalleRecepcionRepository.findByRecepcionId(idRecepcion);
    }

    public List<DetalleRecepcion> obtenerDetallePorProductos(Long idProducto) {
        return detalleRecepcionRepository.findByProductoId(idProducto);
    }
}
