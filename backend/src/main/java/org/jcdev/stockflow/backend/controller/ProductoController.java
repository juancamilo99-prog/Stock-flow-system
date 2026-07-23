package org.jcdev.stockflow.backend.controller;


import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.jcdev.stockflow.backend.service.ProductoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/productos")
public class ProductoController {


    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping(path = "/")
    public List<Producto> obtenerProductos(){
        return productoService.obtenerTodosProductos();
    }

    @GetMapping(path = "/{idProducto}")
    public Producto obtenerProductoPorId(@PathVariable Long idProducto){
        return productoService.obtenerProductoPorId(idProducto);
    }
}
