package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.entity.Tarea;
import org.jcdev.stockflow.backend.repository.TareaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
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
}
