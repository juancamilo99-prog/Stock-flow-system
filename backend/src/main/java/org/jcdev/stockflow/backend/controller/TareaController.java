package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.creardto.CrearTareaDto;
import org.jcdev.stockflow.backend.entity.Tarea;
import org.jcdev.stockflow.backend.enums.tarea.EstadoTarea;
import org.jcdev.stockflow.backend.service.TareaService;
import org.springframework.web.bind.annotation.*;

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

    //obtener tarea por estado
    @GetMapping(path = "/estados/{estadoTarea}")
    public List<Tarea> obtenerTareasPorEstadoTarea(@PathVariable EstadoTarea estadoTarea){
        return tareaService.obtenerTareasPorEstadoTarea(estadoTarea);
    }

    //crear tarea
    @PostMapping
    public Tarea crearTarea(@Valid @RequestBody CrearTareaDto crearTareaDto){
        return  tareaService.crearTarea(crearTareaDto);
    }
}
