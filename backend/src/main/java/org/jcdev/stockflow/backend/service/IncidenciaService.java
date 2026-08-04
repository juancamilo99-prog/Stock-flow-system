package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarIncidenciaDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearIncidenciaDto;
import org.jcdev.stockflow.backend.entity.*;
import org.jcdev.stockflow.backend.enums.incidencia.EstadoIncidencia;
import org.jcdev.stockflow.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final RecepcionRepository recepcionRepository;

    public IncidenciaService(IncidenciaRepository incidenciaRepository, UsuarioRepository usuarioRepository, ProductoRepository productoRepository, RecepcionRepository recepcionRepository) {
        this.incidenciaRepository = incidenciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.recepcionRepository = recepcionRepository;
    }

    //obtener todas las incidencias
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

    //obtener incidencias por estado
    public List<Incidencia> obtenerIncidenciasByEstadoIncidencia(EstadoIncidencia estadoIncidencia) {
        return incidenciaRepository.findByEstadoIncidencia(estadoIncidencia);
    }

    // crear una incidencia
    public Incidencia crearIncidencia(CrearIncidenciaDto crearIncidenciaDto) {
        //buscar un usuario
        Usuario usuario =  usuarioRepository.findById(crearIncidenciaDto.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        //como no son obligatorios, pueden ser null
        Producto producto = null;
        Recepcion recepcion = null;

        //validamos el campo y buscamos un producto
        if (crearIncidenciaDto.getIdProducto() != null) {
            producto = productoRepository.findById(crearIncidenciaDto.getIdProducto())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
        }
        //validamos el campo y buscamos una recepcion
        if (crearIncidenciaDto.getIdRecepcion() != null) {
            recepcion = recepcionRepository.findById(crearIncidenciaDto.getIdRecepcion())
                    .orElseThrow(() -> new IllegalArgumentException("Recepcion no encontrada"));
        }

        //validamos que este asociada a algun pedido, producto o recepcion
        if (producto == null && recepcion == null) {
            throw new IllegalArgumentException(
                    "La incidencia debe estar asociada a un producto o una recepción"
            );
        }

        Incidencia incidencia = new Incidencia(
                crearIncidenciaDto.getTipoIncidencia(),
                crearIncidenciaDto.getDescripcion(),
                usuario, producto, recepcion != null ? recepcion.getPedido() : null ,recepcion
        );
        return incidenciaRepository.save(incidencia);
    }

    //actualizar una incidencia
    public Incidencia actualizarIncidencia(Long idIncidencia,ActualizarIncidenciaDto actualizarIncidenciaDto) {
        //buscamos la incidencia
        Incidencia incidencia = incidenciaRepository.findById(idIncidencia)
                .orElseThrow(() -> new IllegalArgumentException("Incidencia no encontrada"));

        if (actualizarIncidenciaDto.getDescripcion() != null) {
            if (actualizarIncidenciaDto.getDescripcion().isBlank()){
                throw new IllegalArgumentException("La Descripción no puede estar vacia");
            }
            incidencia.setDescripcion(actualizarIncidenciaDto.getDescripcion());
        }

        EstadoIncidencia estadoActual = incidencia.getEstadoIncidencia();
        EstadoIncidencia estadoNuevo = actualizarIncidenciaDto.getEstadoIncidencia();

        if (estadoNuevo != null && estadoNuevo != estadoActual) {
            switch (estadoActual) {
                case resuelta -> throw new IllegalArgumentException("Una incidencia resuelta no puede cambiar de estado");
                case en_proceso -> {
                    if (estadoNuevo == EstadoIncidencia.pendiente){
                        throw new IllegalArgumentException(
                                "Una incidencia en proceso no puede volver a pendiente"
                        );
                    }
                }
            }
            incidencia.setEstadoIncidencia(estadoNuevo);
        }

        return incidenciaRepository.save(incidencia);
    }
}

