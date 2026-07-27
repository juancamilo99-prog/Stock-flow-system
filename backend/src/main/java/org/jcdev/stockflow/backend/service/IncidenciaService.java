package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.entity.Incidencia;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.repository.IncidenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;

    public IncidenciaService(IncidenciaRepository incidenciaRepository) {
        this.incidenciaRepository = incidenciaRepository;
    }

    public List<Incidencia> obtenerIncidencias() {
        return incidenciaRepository.findAll();
    }

    //obtener incidencias por identificador de usuario
    public List<Incidencia> obtenerIncidenciasByUsuarioId(Long idUsuario) {
        return incidenciaRepository.findByUsuarioId(idUsuario);
    }

    //obtener incidencias por identificador del producto
    public List<Incidencia> obtenerIncidenciasByProductoId(Long idProducto) {
        return incidenciaRepository.findByProductoId(idProducto);
    }

    //obtener incidencias por identificador de pedido
    public List<Incidencia> obtenerIncidenciasByPedidoId(Long idPedido) {
        return incidenciaRepository.findByPedidoId(idPedido);
    }

    //obtener incidencias por identificador de recepcion
    public List<Incidencia> obtenerIncidenciasByRecepcionId(Long idRecepcion) {
        return incidenciaRepository.findByRecepcionId(idRecepcion);
    }
}
