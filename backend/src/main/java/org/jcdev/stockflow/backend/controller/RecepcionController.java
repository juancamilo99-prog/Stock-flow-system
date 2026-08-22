package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarDetalleRecepcionDto;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarRecepcionDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearDetalleRecepcionDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearRecepcionDto;
import org.jcdev.stockflow.backend.entity.DetalleRecepcion;
import org.jcdev.stockflow.backend.entity.Recepcion;
import org.jcdev.stockflow.backend.service.RecepcionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/recepciones")
public class RecepcionController {
    RecepcionService recepcionService;

    public RecepcionController(RecepcionService recepcionService) {
        this.recepcionService = recepcionService;
    }

    //obtener todas las recepciones
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping
    public ResponseEntity<List<Recepcion>> obtenerRecepciones() {
        List<Recepcion> listaRecepciones = recepcionService.obtenerRecepciones();
        return ResponseEntity.ok(listaRecepciones);
    }

    //obtener recepcion por id
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO') OR @authorizationService.esRecepcionDelUsuarioActual(#idRecepcion)")
    @GetMapping(path = "/{idRecepcion}")
    public ResponseEntity<Recepcion> obtenerRecepcionPorId(@PathVariable Long idRecepcion) {
        Recepcion recepcion = recepcionService.obtenerRecepcionPorId(idRecepcion);
        return ResponseEntity.ok(recepcion);
    }

    //obtener recepcion por id de usuario
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO') OR @authorizationService.esUsuarioActual(#idUsuario)")
    @GetMapping(path = "/usuario/{idUsuario}")
    public ResponseEntity<List<Recepcion>> obtenerRecepcionPorUsuario(@PathVariable Long idUsuario) {
        List<Recepcion> recepcion = recepcionService.obtenerRecepcionPorUsuario(idUsuario);
        return ResponseEntity.ok(recepcion);
    }

    //obtener detalles
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO') OR @authorizationService.esRecepcionDelUsuarioActual(#idRecepcion)")
    @GetMapping(path = "/{idRecepcion}/detalle")
    public ResponseEntity<List<DetalleRecepcion>> obtenerDetalleRecepciones(@PathVariable Long idRecepcion) {
        List<DetalleRecepcion> detalleRecepciones = recepcionService.obtenerDetalleRecepciones(idRecepcion);
        return ResponseEntity.ok(detalleRecepciones);
    }

    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping(path = "/{idProducto}/producto")
    public ResponseEntity<List<DetalleRecepcion>> obtenerDetallePorProducto(@PathVariable Long idProducto) {
        List<DetalleRecepcion> detalleRecepciones = recepcionService.obtenerDetallePorProductos(idProducto);
        return ResponseEntity.ok(detalleRecepciones);
    }

    //crear recepcion
    @PreAuthorize("hasRole('COORDINADOR')")
    @PostMapping
    public ResponseEntity<Recepcion> crearRecepcion(@Valid @RequestBody CrearRecepcionDto crearRecepcionDto) {
        Recepcion recepcion = recepcionService.crearRecepcion(crearRecepcionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(recepcion);
    }

    //actualizar recepcion
    @PreAuthorize("hasRole('COORDINADOR')")
    @PatchMapping(path = "/{idRecepcion}")
    public ResponseEntity<Recepcion> actualizarRecepcion(@PathVariable Long idRecepcion,  @Valid @RequestBody ActualizarRecepcionDto actualizarRecepcionDto) {
        Recepcion recepcion = recepcionService.actualizarRecepcion(idRecepcion, actualizarRecepcionDto);
        return  ResponseEntity.ok(recepcion);
    }

    //eliminar recepcion
    @PreAuthorize("hasRole('COORDINADOR')")
    @DeleteMapping(path = "/{idRecepcion}")
    public ResponseEntity<Void> eliminarRecepcion(@PathVariable Long idRecepcion) {
        recepcionService.eliminarRecepcion(idRecepcion);
        return ResponseEntity.noContent().build();
    }

    //crear detalle recepcion
    @PreAuthorize("hasRole('COORDINADOR')")
    @PostMapping(path = "/crear-detalles")
    public ResponseEntity<List<DetalleRecepcion>> crearDetalleRecepcion(@Valid @RequestBody CrearDetalleRecepcionDto crearDetalleRecepcionDto) {
        List<DetalleRecepcion> detallesRecepcion = recepcionService.crearDetalleRecepcion(crearDetalleRecepcionDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(detallesRecepcion);
    }

    @PreAuthorize("hasRole('COORDINADOR')")
    @PatchMapping(path = "/{idDetalleRecepcion}/actualizar-detalle")
    public ResponseEntity<DetalleRecepcion> actualizarDetalleRecepcion(@PathVariable Long idDetalleRecepcion, @Valid @RequestBody ActualizarDetalleRecepcionDto actualizarDetalleRecepcionDto) {
        DetalleRecepcion detalleRecepcion = recepcionService.actualizarDetalleRecepcion(idDetalleRecepcion, actualizarDetalleRecepcionDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(detalleRecepcion);
    }
}
