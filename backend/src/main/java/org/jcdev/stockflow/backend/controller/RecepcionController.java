package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.ActualizarDetalleRecepcionDto;
import org.jcdev.stockflow.backend.dto.ActualizarRecepcionDto;
import org.jcdev.stockflow.backend.dto.CrearDetalleRecepcionDto;
import org.jcdev.stockflow.backend.dto.CrearRecepcionDto;
import org.jcdev.stockflow.backend.entity.DetallePedido;
import org.jcdev.stockflow.backend.entity.DetalleRecepcion;
import org.jcdev.stockflow.backend.entity.Recepcion;
import org.jcdev.stockflow.backend.service.RecepcionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping
    public ResponseEntity<List<Recepcion>> obtenerRecepciones() {
        List<Recepcion> listaRecepciones = recepcionService.obtenerRecepciones();
        return ResponseEntity.ok(listaRecepciones);
    }

    //obtener recepcion por id
    @GetMapping(path = "/{idRecepcion}")
    public ResponseEntity<Recepcion> obtenerRecepcionPorId(Long idRecepcion) {
        Recepcion recepcion = recepcionService.obtenerRecepcionPorId(idRecepcion);
        return ResponseEntity.ok(recepcion);
    }

    //obtener detalles
    @GetMapping(path = "/{idRecepcion}/detalle")
    public ResponseEntity<List<DetalleRecepcion>> obtenerDetalleRecepciones(@PathVariable Long idRecepcion) {
        List<DetalleRecepcion> detalleRecepciones = recepcionService.obtenerDetalleRecepciones(idRecepcion);
        return ResponseEntity.ok(detalleRecepciones);
    }

    @GetMapping(path = "/{idProducto}/producto")
    public ResponseEntity<List<DetalleRecepcion>> obtenerDetallePorProducto(@PathVariable Long idProducto) {
        List<DetalleRecepcion> detalleRecepciones = recepcionService.obtenerDetalleRecepciones(idProducto);
        return ResponseEntity.ok(detalleRecepciones);
    }

    //crear recepcion
    @PostMapping
    public ResponseEntity<Recepcion> crearRecepcion(@Valid @RequestBody CrearRecepcionDto crearRecepcionDto) {
        Recepcion recepcion = recepcionService.crearRecepcion(crearRecepcionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(recepcion);
    }

    //actualizar recepcion
    @PatchMapping(path = "/{idRecepcion}")
    public ResponseEntity<Recepcion> actualizarRecepcion(@PathVariable Long idRecepcion,  @Valid @RequestBody ActualizarRecepcionDto actualizarRecepcionDto) {
        Recepcion recepcion = recepcionService.actualizarRecepcion(idRecepcion, actualizarRecepcionDto);
        return  ResponseEntity.ok(recepcion);
    }

    //eliminar recepcion
    @DeleteMapping(path = "/{idRecepcion}")
    public ResponseEntity<Void> eliminarRecepcion(@PathVariable Long idRecepcion) {
        recepcionService.eliminarRecepcion(idRecepcion);
        return ResponseEntity.noContent().build();
    }

    //crear detalle recepcion
    @PostMapping(path = "/crear-detalles")
    public ResponseEntity<List<DetalleRecepcion>> crearDetalleRecepcion(@Valid @RequestBody CrearDetalleRecepcionDto crearDetalleRecepcionDto) {
        List<DetalleRecepcion> detallesRecepcion = recepcionService.crearDetalleRecepcion(crearDetalleRecepcionDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(detallesRecepcion);
    }

    @PatchMapping(path = "/{idDetalleRecepcion}/actualizar-detalle")
    public ResponseEntity<DetalleRecepcion> actualizarDetalleRecepcion(@PathVariable Long idDetalleRecepcion, @Valid @RequestBody ActualizarDetalleRecepcionDto actualizarDetalleRecepcionDto) {
        DetalleRecepcion detalleRecepcion = recepcionService.actualizarDetalleRecepcion(idDetalleRecepcion, actualizarDetalleRecepcionDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(detalleRecepcion);
    }
}
