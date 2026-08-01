package org.jcdev.stockflow.backend.controller;


import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarProductoDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearProductoDto;
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

    //crear un producto
    @PostMapping
    public Producto crearProducto(@Valid @RequestBody CrearProductoDto crearProductoDto){
        return productoService.crearProducto(crearProductoDto);
    }

    //actualizar un producto
    @PatchMapping("/{idProducto}")
    public Producto actualizarProducto(@PathVariable Long idProducto, @Valid @RequestBody ActualizarProductoDto actualizarProductoDto){
        return productoService.actualizarProducto(idProducto, actualizarProductoDto);
    }

    //eliminar un producto
    @DeleteMapping(path = "/{idProducto}")
    public Producto eliminarProducto(@PathVariable Long idProducto){
        return productoService.eliminarProducto(idProducto);
    }


}
