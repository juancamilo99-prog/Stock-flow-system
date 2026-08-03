package org.jcdev.stockflow.backend.controller;

import org.jcdev.stockflow.backend.dto.creardto.CrearUbicacionDto;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.entity.Ubicacion;
import org.jcdev.stockflow.backend.service.UbicacionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/ubicaciones")
public class UbicacionController {

    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    //obtener ubicaciones
    @GetMapping(path = "/")
    public List<Ubicacion> obtenerUbicaciones() {
        return ubicacionService.obtenerUbicaciones();
    }

    //obtener ubicaciones por productos
    @GetMapping(path = "/{idUbicacion}/productos")
    public List<Producto> obtenerProductosPorUbicacion(@PathVariable Long idUbicacion) {
        return ubicacionService.obtenerProductosPorUbicacion(idUbicacion);
    }

    //crear una ubicacion
    @PostMapping
    public Ubicacion crearUbicacion(CrearUbicacionDto crearUbicacionDto) {
        return ubicacionService.crearUbicacion(crearUbicacionDto);
    }
}
