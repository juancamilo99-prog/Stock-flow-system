package org.jcdev.stockflow.backend.controller;


import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarProductoDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearProductoDto;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/productos")
public class ProductoController {


    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos(){
        List<Producto> productos = productoService.obtenerTodosProductos();
        return ResponseEntity.ok(productos);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{idProducto}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long idProducto){
        Producto productos = productoService.obtenerProductoPorId(idProducto);
        return ResponseEntity.ok(productos);
    }

    //crear un producto
    @PreAuthorize("hasRole('COORDINADOR')")
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Valid @RequestBody CrearProductoDto crearProductoDto){
        Producto productos = productoService.crearProducto(crearProductoDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productos);
    }

    //actualizar un producto
    @PreAuthorize("hasRole('COORDINADOR')")
    @PatchMapping("/{idProducto}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long idProducto, @Valid @RequestBody ActualizarProductoDto actualizarProductoDto){
        Producto producto = productoService.actualizarProducto(idProducto, actualizarProductoDto);
        return ResponseEntity.ok(producto);
    }

    //eliminar un producto
    @PreAuthorize("hasRole('COORDINADOR')")
    @DeleteMapping(path = "/{idProducto}")
    public ResponseEntity<Producto> eliminarProducto(@PathVariable Long idProducto){
        Producto productos = productoService.eliminarProducto(idProducto);
        return ResponseEntity.ok(productos);
    }
}
