package org.jcdev.stockflow.backend.controller;

import org.jcdev.stockflow.backend.entity.DetallePedido;
import org.jcdev.stockflow.backend.entity.DetalleRecepcion;
import org.jcdev.stockflow.backend.entity.Recepcion;
import org.jcdev.stockflow.backend.service.RecepcionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
