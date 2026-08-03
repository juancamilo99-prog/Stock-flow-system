package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarUbicacionDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearUbicacionDto;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.entity.Ubicacion;
import org.jcdev.stockflow.backend.service.UbicacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @GetMapping
    public ResponseEntity<List<Ubicacion>> obtenerUbicaciones() {
        List<Ubicacion> ubicaciones = ubicacionService.obtenerUbicaciones();
        return ResponseEntity.ok(ubicaciones);
    }

    //obtener ubicaciones por productos
    @GetMapping(path = "/{idUbicacion}/productos")
    public ResponseEntity<List<Producto>> obtenerProductosPorUbicacion(@PathVariable Long idUbicacion) {
        List<Producto> producto = ubicacionService.obtenerProductosPorUbicacion(idUbicacion);
        return ResponseEntity.ok(producto);
    }

    //crear una ubicacion
    @PostMapping
    public ResponseEntity<Ubicacion> crearUbicacion(@Valid @RequestBody CrearUbicacionDto crearUbicacionDto) {
        Ubicacion ubicacion = ubicacionService.crearUbicacion(crearUbicacionDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ubicacion);
    }

    //actualizar una ubicacion
    @PatchMapping(path = "/{idUbicacion}")
    public ResponseEntity<Ubicacion> actualizarUbicacion(@PathVariable Long idUbicacion,@Valid @RequestBody ActualizarUbicacionDto actualizarUbicacionDto) {
        Ubicacion ubicacion = ubicacionService.actualizarUbicacion(idUbicacion, actualizarUbicacionDto);
        return ResponseEntity.ok(ubicacion);
    }
}
