package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarTareaDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearTareaDto;
import org.jcdev.stockflow.backend.entity.Pedido;
import org.jcdev.stockflow.backend.entity.Recepcion;
import org.jcdev.stockflow.backend.entity.Tarea;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;
import org.jcdev.stockflow.backend.enums.tarea.EstadoTarea;
import org.jcdev.stockflow.backend.enums.tarea.PrioridadTarea;
import org.jcdev.stockflow.backend.exception.CambioNoDetectadoException;
import org.jcdev.stockflow.backend.exception.RecursoNoEncontradoException;
import org.jcdev.stockflow.backend.exception.TransicionEstadoInvalidaException;
import org.jcdev.stockflow.backend.repository.PedidoRepository;
import org.jcdev.stockflow.backend.repository.RecepcionRepository;
import org.jcdev.stockflow.backend.repository.TareaRepository;
import org.jcdev.stockflow.backend.repository.UsuarioRepository;
import org.jcdev.stockflow.backend.service.security.AuthorizationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecepcionRepository recepcionRepository;
    private final PedidoRepository pedidoRepository;
    private final AuditoriaService auditoriaService;

    //autorizaciones
    AuthorizationService authorizationService;

    public TareaService(TareaRepository tareaRepository, UsuarioRepository usuarioRepository, RecepcionRepository recepcionRepository,
                        PedidoRepository pedidoRepository, AuditoriaService auditoriaService, AuthorizationService authorizationService) {
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
        this.recepcionRepository = recepcionRepository;
        this.pedidoRepository = pedidoRepository;
        this.auditoriaService = auditoriaService;
        this.authorizationService = authorizationService;
    }

    //obtener todas las tareas
    public List<Tarea> obtenerTareas(){
        return tareaRepository.findAll();
    }

    //obtener las tareas por identificador de usuario
    public List<Tarea> obtenerTareaPorUsuario(Long idUsuario){
        return tareaRepository.findByUsuarioId(idUsuario);
    }

    //obtener las tareas por identificador del pedido
    public List<Tarea> obtenerTareaPorPedido(Long idPedido){
        return tareaRepository.findByPedidoId(idPedido);
    }

    //obtener las tareas por identificacion de la recepcion
    public List<Tarea> obtenerTareaPorRecepcion(Long idRecepcion){
        return tareaRepository.findByRecepcionId(idRecepcion);
    }

    //obtener las tareas por su estado
    public List<Tarea> obtenerTareasPorEstadoTarea(EstadoTarea estadoTarea){
      List<Tarea> tareas = tareaRepository.findByEstadoTarea(estadoTarea);
      if (tareas.isEmpty()){
          throw new RecursoNoEncontradoException(
                  "No existe tareas con el estado: " + estadoTarea
          );
      }
      return tareas;
    }

    //obtener tareas por su prioridad
    public List<Tarea> obtenerTareasPorPrioridadTarea(PrioridadTarea prioridadTarea){
        List<Tarea> tareas = tareaRepository.findByPrioridadTarea(prioridadTarea);
        if (tareas.isEmpty()){
            throw new RecursoNoEncontradoException(
                    "No existen tareas con la prioridad: " + prioridadTarea
            );
        }
        return tareas;
    }

    //crear una tarea
    @Transactional
    public Tarea crearTarea(CrearTareaDto crearTareaDto){

        //TODO validar jerarquia de asignacion: COORDINADOR puede asignar tarea tanto a encargado como a operario
        //TODO ENCARGADO soo puede asignar a operario
        Tarea tarea = new Tarea();
        if (crearTareaDto.getIdPedido() != null && crearTareaDto.getIdRecepcion() != null) {
            throw new IllegalArgumentException("Debe indicar un pedido o una recepcion, no ambos");
        }
        if (crearTareaDto.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(crearTareaDto.getIdUsuario())
                    .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no existe"));
            tarea.setUsuario(usuario);
        }
        Pedido pedido = null;
        Recepcion recepcion = null;
        if (crearTareaDto.getIdRecepcion() != null) {
            recepcion = recepcionRepository.findById(crearTareaDto.getIdRecepcion())
                    .orElseThrow(() -> new RecursoNoEncontradoException("La recepcion no existe"));
            pedido = recepcion.getPedido();
            tarea.setRecepcion(recepcion);
            tarea.setPedido(pedido);
        }
        if (crearTareaDto.getIdRecepcion() == null && crearTareaDto.getIdPedido() != null) {
            pedido = pedidoRepository.findById(crearTareaDto.getIdPedido())
                    .orElseThrow(()-> new RecursoNoEncontradoException("El pedido no existe"));
            tarea.setPedido(pedido);
        }
        if (pedido == null && recepcion == null) {
            throw new IllegalArgumentException("La tarea debe estar asociada a un pedido o una recepcion");
        }
        tarea.setFechaCreacion(LocalDate.now());
        tarea.setEstadoTarea(EstadoTarea.pendiente);
        tarea.setTipoTarea(crearTareaDto.getTipoTarea());
        tarea.setDescripcion(crearTareaDto.getDescripcion().trim());
        tarea.setPrioridadTarea(crearTareaDto.getPrioridadTarea());
        tarea = tareaRepository.save(tarea);

        //registrar una auditoria
        auditoriaService.registrarAuditoria(
                TipoAccion.CREAR,
                EntidadAuditoria.TAREA,
                "Se ha creado una tarea",
                tarea.getId(),
                authorizationService.obtenerUsuarioAutenticado()
        );

        return tarea;
    }

    //actualizar una tarea
    @Transactional
    public Tarea actualizarTarea(Long idTarea,ActualizarTareaDto actualizarTareaDto){
        //TODO validar jerarquia de asignacion: COORDINADOR puede asignar tarea tanto a encargado como a operario
        //TODO ENCARGADO soo puede asignar a operario
        Tarea tarea = tareaRepository.findById(idTarea)
                .orElseThrow(() -> new RecursoNoEncontradoException("La tarea no existe"));
        boolean seCambioEstado = false;
        boolean detectarCambio = false;

        if (actualizarTareaDto.getDescripcion() != null){
            String descripcionActual = tarea.getDescripcion().trim();
            String descripcionNueva = actualizarTareaDto.getDescripcion().trim();
            if (descripcionNueva.isBlank()){
                throw new IllegalArgumentException("La descripcion no puede estar vacia");
            }
            if (!descripcionNueva.equals(descripcionActual)){
                tarea.setDescripcion(descripcionNueva);
                detectarCambio = true;
            }
        }
        if (actualizarTareaDto.getPrioridad() != null){
            PrioridadTarea prioridadActual = tarea.getPrioridadTarea();
            PrioridadTarea prioridadNueva = actualizarTareaDto.getPrioridad();
            if (prioridadNueva != prioridadActual){
                tarea.setPrioridadTarea(prioridadNueva);
                detectarCambio = true;
            }
        }
        EstadoTarea estadoNuevo = actualizarTareaDto.getEstado();
        EstadoTarea estadoActual = tarea.getEstadoTarea();

        if (estadoNuevo != null && estadoNuevo != estadoActual){
            switch (estadoActual){
                case resuelta -> throw new TransicionEstadoInvalidaException(
                        "Una tarea resuelta no puede cambiar de estado"
                );
                case en_proceso -> {
                    if (estadoNuevo == EstadoTarea.pendiente){
                        throw new TransicionEstadoInvalidaException(
                                "Una tarea en proceso no puede volver a estar pendiente"
                        );
                    }
                }
            }
            tarea.setEstadoTarea(estadoNuevo);
            seCambioEstado = true;
        }

        if (detectarCambio || seCambioEstado){
            tarea = tareaRepository.save(tarea);
        }else {
            throw new CambioNoDetectadoException("No se detecto ningun cambio");
        }

        if (seCambioEstado) {
            //registrar Auditoria
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.TAREA,
                    "La tarea cambio el estado de " + estadoActual + " a estado " + estadoNuevo,
                    tarea.getId(),
                    authorizationService.obtenerUsuarioAutenticado()
            );
        }
        if (detectarCambio) {
            //registrar Auditoria
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.TAREA,
                    "Se ha Actualizado una tarea",
                    tarea.getId(),
                    authorizationService.obtenerUsuarioAutenticado()
            );
        }
        return tarea;
    }
}
