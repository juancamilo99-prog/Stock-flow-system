package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarIncidenciaDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearIncidenciaDto;
import org.jcdev.stockflow.backend.entity.*;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;
import org.jcdev.stockflow.backend.enums.incidencia.EstadoIncidencia;
import org.jcdev.stockflow.backend.exception.CambioNoDetectadoException;
import org.jcdev.stockflow.backend.exception.RecursoNoEncontradoException;
import org.jcdev.stockflow.backend.exception.TransicionEstadoInvalidaException;
import org.jcdev.stockflow.backend.repository.*;
import org.jcdev.stockflow.backend.service.security.AuthorizationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;
    private final ProductoRepository productoRepository;
    private final RecepcionRepository recepcionRepository;

    //servicios
    AuditoriaService auditoriaService;
    //autorizaciones
    AuthorizationService authorizationService;

    public IncidenciaService(IncidenciaRepository incidenciaRepository, ProductoRepository productoRepository,
                             RecepcionRepository recepcionRepository, AuditoriaService auditoriaService, AuthorizationService authorizationService) {
        this.incidenciaRepository = incidenciaRepository;
        this.productoRepository = productoRepository;
        this.recepcionRepository = recepcionRepository;
        this.auditoriaService = auditoriaService;
        this.authorizationService = authorizationService;
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
    @Transactional
    public Incidencia crearIncidencia(CrearIncidenciaDto crearIncidenciaDto) {
        //sacamos al usuario autenticado
        Usuario usuario = authorizationService.obtenerUsuarioAutenticado();

        //como no son obligatorios, pueden ser null
        Producto producto = null;
        Recepcion recepcion = null;

        //validamos el campo y buscamos un producto
        if (crearIncidenciaDto.getIdProducto() != null) {
            producto = productoRepository.findById(crearIncidenciaDto.getIdProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
        }
        //validamos el campo y buscamos una recepcion
        if (crearIncidenciaDto.getIdRecepcion() != null) {
            recepcion = recepcionRepository.findById(crearIncidenciaDto.getIdRecepcion())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Recepcion no encontrada"));
        }

        //validamos que este asociada a algun producto o recepcion
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
        incidencia = incidenciaRepository.save(incidencia);
        auditoriaService.registrarAuditoria(
                TipoAccion.CREAR,
                EntidadAuditoria.INCIDENCIA,
                "Se ha creado una incidencia nueva de: "+crearIncidenciaDto.getTipoIncidencia(),
                incidencia.getId(),
                authorizationService.obtenerUsuarioAutenticado()
        );
        return incidencia;
    }

    //actualizar una incidencia
    @Transactional
    public Incidencia actualizarIncidencia(Long idIncidencia,ActualizarIncidenciaDto actualizarIncidenciaDto) {
        //buscamos la incidencia
        Incidencia incidencia = incidenciaRepository.findById(idIncidencia)
                .orElseThrow(() -> new RecursoNoEncontradoException("Incidencia no encontrada"));

        boolean detectarCambio = false;
        boolean cambioEstado = false;
        if (actualizarIncidenciaDto.getDescripcion()!= null && !actualizarIncidenciaDto.getDescripcion().isBlank()) {
            String descripcionNueva = actualizarIncidenciaDto.getDescripcion().trim();
            String descripcionActual = incidencia.getDescripcion().trim();
            if (!descripcionNueva.equalsIgnoreCase(descripcionActual)) {
                incidencia.setDescripcion(descripcionNueva);
                detectarCambio = true;
            }
        }

        EstadoIncidencia estadoActual = incidencia.getEstadoIncidencia();
        EstadoIncidencia estadoNuevo = actualizarIncidenciaDto.getEstadoIncidencia();

        if (estadoNuevo != null && estadoNuevo != estadoActual) {
            switch (estadoActual) {
                case resuelta -> throw new TransicionEstadoInvalidaException("Una incidencia resuelta no puede cambiar de estado");
                case en_proceso -> {
                    if (estadoNuevo == EstadoIncidencia.pendiente){
                        throw new TransicionEstadoInvalidaException(
                                "Una incidencia en proceso no puede volver a pendiente"
                        );
                    }
                }
            }
            incidencia.setEstadoIncidencia(estadoNuevo);
            cambioEstado = true;
        }

        if (detectarCambio || cambioEstado) {
            incidencia =  incidenciaRepository.save(incidencia);
        } else {
            throw new CambioNoDetectadoException("No se detecto ningun cambio");
        }

        if (cambioEstado) {
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.INCIDENCIA,
                    "Se ha actualizado el estado de la incidencia "+incidencia.getId()+" a "+estadoNuevo,
                    incidencia.getId(),
                    authorizationService.obtenerUsuarioAutenticado()
            );
        }
        if (detectarCambio) {
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.INCIDENCIA,
                    "Se ha actualizado una incidencia",
                    incidencia.getId(),
                    authorizationService.obtenerUsuarioAutenticado()
            );
        }
        return incidencia;
    }
}

