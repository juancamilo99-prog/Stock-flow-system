package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.ActualizarRecepcionDto;
import org.jcdev.stockflow.backend.dto.CrearRecepcionDto;
import org.jcdev.stockflow.backend.entity.DetallePedido;
import org.jcdev.stockflow.backend.entity.DetalleRecepcion;
import org.jcdev.stockflow.backend.entity.Recepcion;
import org.jcdev.stockflow.backend.service.RecepcionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/recepciones")
public class RecepcionController {
    RecepcionService recepcionService;

    public RecepcionController(RecepcionService recepcionService) {
        this.recepcionService = recepcionService;
    }

    @GetMapping
    public List<Recepcion> obtenerRecepciones() {
        return recepcionService.obtenerRecepciones();
    }

    //obtener detalles
    @GetMapping(path = "/{idRecepcion}/detalle")
    public List<DetalleRecepcion> obtenerDetalleRecepciones(@PathVariable Long idRecepcion) {
        return recepcionService.obtenerDetalleRecepciones(idRecepcion);
    }

    @GetMapping(path = "/{idProducto}/producto")
    public List<DetalleRecepcion> obtenerDetallePorProducto(@PathVariable Long idProducto) {
        return recepcionService.obtenerDetallePorProductos(idProducto);
    }

    //crear recepcion
    @PostMapping
    public Recepcion crearRecepcion(@Valid @RequestBody CrearRecepcionDto crearRecepcionDto) {
        return recepcionService.crearRecepcion(crearRecepcionDto);
    }

    //actualizar recepcion
    @PatchMapping(path = "/{idRecepcion}")
    public Recepcion actualizarRecepcion(@PathVariable Long idRecepcion,  @Valid @RequestBody ActualizarRecepcionDto actualizarRecepcionDto) {
        return recepcionService.actualizarRecepcion(idRecepcion, actualizarRecepcionDto);
    }

    //eliminar recepcion
    @DeleteMapping(path = "/{idRecepcion}")
    public Recepcion eliminarRecepcion(@PathVariable Long idRecepcion) {
        return recepcionService.eliminarRecepcion(idRecepcion);
    }
}
