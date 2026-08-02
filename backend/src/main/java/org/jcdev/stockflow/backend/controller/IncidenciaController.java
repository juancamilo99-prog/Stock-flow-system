package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.creardto.CrearIncidenciaDto;
import org.jcdev.stockflow.backend.entity.Incidencia;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.enums.EstadoIncidencia;
import org.jcdev.stockflow.backend.service.IncidenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/incidencias")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;

    public IncidenciaController(IncidenciaService incidenciaService) {
        this.incidenciaService = incidenciaService;
    }

    //mostrar todas las incidencias
    @GetMapping
    public ResponseEntity<List<Incidencia>> getIncidencias() {
        List<Incidencia> incidencias = incidenciaService.obtenerIncidencias();
        return ResponseEntity.ok(incidencias);
    }

    //incidencias por registro de usuario
    @GetMapping(path = "/{idUsuario}/usuario")
    public ResponseEntity<List<Incidencia>> obtenerIncidenciasByUsuarioId(@PathVariable Long idUsuario) {
        List<Incidencia> incidenciaUsuario = incidenciaService.obtenerIncidenciasByUsuarioId(idUsuario);
        return ResponseEntity.ok(incidenciaUsuario);
    }

    //incidencia por registro de producto
    @GetMapping(path = "/{idProducto}/producto")
    public ResponseEntity<List<Incidencia>> obtenerIncidenciasByProductoId(@PathVariable Long idProducto) {
        List<Incidencia> incidenciaProducto = incidenciaService.obtenerIncidenciasByProductoId(idProducto);
        return ResponseEntity.ok(incidenciaProducto);
    }

    //incidencia por registro de pedido
    @GetMapping(path = "/{idPedido}/pedido")
    public ResponseEntity<List<Incidencia>> obtenerIncidenciasByPedidoId(@PathVariable Long idPedido) {
        List<Incidencia> incidenciaPedido = incidenciaService.obtenerIncidenciasByPedidoId(idPedido);
        return ResponseEntity.ok(incidenciaPedido);
    }

    //incidencia por registro de recepcion
    @GetMapping(path = "/{idRecepcion}/recepcion")
    public ResponseEntity<List<Incidencia>> obtenerIncidenciasByRecepcion(@PathVariable Long idRecepcion) {
        List<Incidencia> incidenciaRecepcion = incidenciaService.obtenerIncidenciasByRecepcionId(idRecepcion);
        return  ResponseEntity.ok(incidenciaRecepcion);
    }

    //incidencias por registro de estado
    @GetMapping(path = "/estado/{estadoIncidencia}")
    public ResponseEntity<List<Incidencia>> obtenerIncidenciasByEstadoIncidencia(@PathVariable EstadoIncidencia estadoIncidencia) {
        List<Incidencia> incidenciasPorEstado = incidenciaService.obtenerIncidenciasByEstadoIncidencia(estadoIncidencia);
        return ResponseEntity.ok(incidenciasPorEstado);
    }

    @PostMapping
    public ResponseEntity<Incidencia> crearIncidencia(@Valid @RequestBody CrearIncidenciaDto crearIncidenciaDto) {
        Incidencia crearIncidencia = incidenciaService.crearIncidencia(crearIncidenciaDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crearIncidencia);
    }


}
