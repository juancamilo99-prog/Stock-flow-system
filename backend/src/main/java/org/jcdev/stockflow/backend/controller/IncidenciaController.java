package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarIncidenciaDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearIncidenciaDto;
import org.jcdev.stockflow.backend.entity.Incidencia;
import org.jcdev.stockflow.backend.enums.incidencia.EstadoIncidencia;
import org.jcdev.stockflow.backend.service.IncidenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping
    public ResponseEntity<List<Incidencia>> getIncidencias() {
        List<Incidencia> incidencias = incidenciaService.obtenerIncidencias();
        return ResponseEntity.ok(incidencias);
    }

    //incidencias por registro de usuario
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO') OR @authorizationService.esUsuarioActual(#idUsuario)")
    @GetMapping(path = "/{idUsuario}/usuario")
    public ResponseEntity<List<Incidencia>> obtenerIncidenciasByUsuarioId(@PathVariable Long idUsuario) {
        List<Incidencia> incidenciaUsuario = incidenciaService.obtenerIncidenciasByUsuarioId(idUsuario);
        return ResponseEntity.ok(incidenciaUsuario);
    }

    //incidencia por registro de producto
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping(path = "/{idProducto}/producto")
    public ResponseEntity<List<Incidencia>> obtenerIncidenciasByProductoId(@PathVariable Long idProducto) {
        List<Incidencia> incidenciaProducto = incidenciaService.obtenerIncidenciasByProductoId(idProducto);
        return ResponseEntity.ok(incidenciaProducto);
    }

    //incidencia por registro de pedido
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping(path = "/{idPedido}/pedido")
    public ResponseEntity<List<Incidencia>> obtenerIncidenciasByPedidoId(@PathVariable Long idPedido) {
        List<Incidencia> incidenciaPedido = incidenciaService.obtenerIncidenciasByPedidoId(idPedido);
        return ResponseEntity.ok(incidenciaPedido);
    }

    //incidencia por registro de recepcion
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping(path = "/{idRecepcion}/recepcion")
    public ResponseEntity<List<Incidencia>> obtenerIncidenciasByRecepcion(@PathVariable Long idRecepcion) {
        List<Incidencia> incidenciaRecepcion = incidenciaService.obtenerIncidenciasByRecepcionId(idRecepcion);
        return  ResponseEntity.ok(incidenciaRecepcion);
    }

    //incidencias por registro de estado
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping(path = "/estado/{estadoIncidencia}")
    public ResponseEntity<List<Incidencia>> obtenerIncidenciasByEstadoIncidencia(@PathVariable EstadoIncidencia estadoIncidencia) {
        List<Incidencia> incidenciasPorEstado = incidenciaService.obtenerIncidenciasByEstadoIncidencia(estadoIncidencia);
        return ResponseEntity.ok(incidenciasPorEstado);
    }

    //crear una incidencia
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @PostMapping
    public ResponseEntity<Incidencia> crearIncidencia(@Valid @RequestBody CrearIncidenciaDto crearIncidenciaDto) {
        Incidencia crearIncidencia = incidenciaService.crearIncidencia(crearIncidenciaDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(crearIncidencia);
    }

    //actualizar una incidencia
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @PatchMapping(path = "/{idIncidencia}")
    public ResponseEntity<Incidencia> actualizarIncidencia(@PathVariable Long idIncidencia, @Valid @RequestBody ActualizarIncidenciaDto actualizarIncidenciaDto) {
        Incidencia incidencia = incidenciaService.actualizarIncidencia(idIncidencia, actualizarIncidenciaDto);
        return ResponseEntity.ok(incidencia);
    }



}
