package org.jcdev.stockflow.backend.controller;

import org.jcdev.stockflow.backend.entity.Tarea;
import org.jcdev.stockflow.backend.service.TareaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    //obtener todas las tareas
    @GetMapping
    public List<Tarea> obtenerTareas(){
        return tareaService.obtenerTareas();
    }

    //obtener tarea por identificador de usuario
    @GetMapping(path = "/{idUsuario}/usuario")
    public List<Tarea> obtenerTareaPorUsuario(@PathVariable Long idUsuario){
        return tareaService.obtenerTareaPorUsuario(idUsuario);
    }

    //obtener tarea por identificador del pedido
    @GetMapping(path = "/{idPedido}/pedido")
    public List<Tarea> obtenerTareasPorPedido(@PathVariable Long idPedido){
        return tareaService.obtenerTareaPorPedido(idPedido);
    }

    //obtener tarea por identificador de la recepcion
    @GetMapping(path = "/{idRecepcion}/recepcion")
    public List<Tarea> obtenerTareasPorRecepcion(@PathVariable Long idRecepcion){
        return tareaService.obtenerTareaPorRecepcion(idRecepcion);
    }
}
