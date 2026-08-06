package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.dto.creardto.CrearTareaDto;
import org.jcdev.stockflow.backend.entity.Pedido;
import org.jcdev.stockflow.backend.entity.Recepcion;
import org.jcdev.stockflow.backend.entity.Tarea;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.enums.tarea.EstadoTarea;
import org.jcdev.stockflow.backend.enums.tarea.PrioridadTarea;
import org.jcdev.stockflow.backend.repository.PedidoRepository;
import org.jcdev.stockflow.backend.repository.RecepcionRepository;
import org.jcdev.stockflow.backend.repository.TareaRepository;
import org.jcdev.stockflow.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RecepcionRepository recepcionRepository;
    private final PedidoRepository pedidoRepository;

    public TareaService(TareaRepository tareaRepository, UsuarioRepository usuarioRepository, RecepcionRepository recepcionRepository, PedidoRepository pedidoRepository) {
        this.tareaRepository = tareaRepository;
        this.usuarioRepository = usuarioRepository;
        this.recepcionRepository = recepcionRepository;
        this.pedidoRepository = pedidoRepository;
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
          throw new IllegalArgumentException(
                  "No existe tareas con el estado: " + estadoTarea
          );
      }
      return tareas;
    }

    //obtener tareas por su prioridad
    public List<Tarea> obtenerTareasPorPrioridadTarea(PrioridadTarea prioridadTarea){
        List<Tarea> tareas = tareaRepository.findByPrioridadTarea(prioridadTarea);
        if (tareas.isEmpty()){
            throw new IllegalArgumentException(
                    "No existen tareas con la prioridad: " + prioridadTarea
            );
        }
        return tareas;
    }

    //crear una tarea
    public Tarea crearTarea(CrearTareaDto crearTareaDto){
        Tarea tarea = new Tarea();
        if (crearTareaDto.getIdPedido() != null && crearTareaDto.getIdRecepcion() != null) {
            throw new IllegalArgumentException("Debe indicar un pedido o una recepcion, no ambos");
        }
        if (crearTareaDto.getIdUsuario() != null) {
            Usuario usuario = usuarioRepository.findById(crearTareaDto.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
            tarea.setUsuario(usuario);
        }
        Pedido pedido = null;
        Recepcion recepcion = null;
        if (crearTareaDto.getIdRecepcion() != null) {
            recepcion = recepcionRepository.findById(crearTareaDto.getIdRecepcion())
                    .orElseThrow(() -> new IllegalArgumentException("La recepcion no existe"));
            pedido = recepcion.getPedido();
            tarea.setRecepcion(recepcion);
            tarea.setPedido(pedido);
        }
        if (crearTareaDto.getIdRecepcion() == null && crearTareaDto.getIdPedido() != null) {
            pedido = pedidoRepository.findById(crearTareaDto.getIdPedido())
                    .orElseThrow(()-> new IllegalArgumentException("El pedido no existe"));
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
        return tareaRepository.save(tarea);
    }
}
