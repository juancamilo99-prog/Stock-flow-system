package org.jcdev.stockflow.backend.controller;


import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.CrearProductoDto;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.service.ProductoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/productos")
public class ProductoController {


    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> obtenerProductos(){
        return productoService.obtenerTodosProductos();
    }

    @GetMapping(path = "/{idProducto}")
    public Producto obtenerProductoPorId(@PathVariable Long idProducto){
        return productoService.obtenerProductoPorId(idProducto);
    }

    @PostMapping
    public Producto crearProducto(@Valid @RequestBody CrearProductoDto crearProductoDto){
        return productoService.crearProducto(crearProductoDto);
    }
}
