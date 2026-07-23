package org.jcdev.stockflow.backend.controller;

import org.jcdev.stockflow.backend.entity.Categoria;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.repository.CategoriaRepository;
import org.jcdev.stockflow.backend.service.CategoriaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping(path = "/")
    public List<Categoria> obtenerCategorias(){
        return categoriaService.obtenerCategorias();
    }

    @GetMapping(path = "/{idCategoria}/productos")
    public List<Producto> obtenerProductosPorCategoria(@PathVariable Long idCategoria){
        return categoriaService.obtenerProductosPorCategoria(idCategoria);
    }
}
