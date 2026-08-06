package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.creardto.CrearTareaDto;
import org.jcdev.stockflow.backend.entity.Tarea;
import org.jcdev.stockflow.backend.enums.tarea.EstadoTarea;
import org.jcdev.stockflow.backend.enums.tarea.PrioridadTarea;
import org.jcdev.stockflow.backend.service.TareaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Tarea> >obtenerTareas(){
        List<Tarea>  tareas = tareaService.obtenerTareas();
        return ResponseEntity.ok(tareas);
    }

    //obtener tarea por identificador de usuario
    @GetMapping(path = "/usuario/{idUsuario}")
    public ResponseEntity<List<Tarea>> obtenerTareaPorUsuario(@PathVariable Long idUsuario){
        List<Tarea>  tareas =  tareaService.obtenerTareaPorUsuario(idUsuario);
        return ResponseEntity.ok(tareas);
    }

    //obtener tarea por identificador del pedido
    @GetMapping(path = "/pedido/{idPedido}")
    public ResponseEntity<List<Tarea>> obtenerTareasPorPedido(@PathVariable Long idPedido){
        List<Tarea> tareas = tareaService.obtenerTareaPorPedido(idPedido);
        return ResponseEntity.ok(tareas);
    }

    //obtener tarea por identificador de la recepcion
    @GetMapping(path = "/recepcion/{idRecepcion}")
    public ResponseEntity<List<Tarea>> obtenerTareasPorRecepcion(@PathVariable Long idRecepcion){
        List<Tarea> tareas = tareaService.obtenerTareaPorRecepcion(idRecepcion);
        return ResponseEntity.ok(tareas);
    }

    //obtener tarea por estado
    @GetMapping(path = "/estados/{estadoTarea}")
    public ResponseEntity<List<Tarea>> obtenerTareasPorEstadoTarea(@PathVariable EstadoTarea estadoTarea){
        List<Tarea> tareas = tareaService.obtenerTareasPorEstadoTarea(estadoTarea);
        return ResponseEntity.ok(tareas);
    }

    //obtener tareas por prioridad
    @GetMapping(path = "/prioridad/{prioridadTarea}")
    public ResponseEntity<List<Tarea>> obtenerTareasPorPrioridad(@PathVariable PrioridadTarea prioridadTarea){
        List<Tarea> tareas = tareaService.obtenerTareasPorPrioridadTarea(prioridadTarea);
        return ResponseEntity.ok(tareas);
    }

    //crear tarea
    @PostMapping
    public ResponseEntity<Tarea> crearTarea(@Valid @RequestBody CrearTareaDto crearTareaDto){
        Tarea tarea = tareaService.crearTarea(crearTareaDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tarea);
    }
}
