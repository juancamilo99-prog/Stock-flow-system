package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.creardto.CrearCategoriaDto;
import org.jcdev.stockflow.backend.entity.Categoria;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.repository.CategoriaRepository;
import org.jcdev.stockflow.backend.service.CategoriaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    //obtener todas las categorias
    @GetMapping(path = "/")
    public List<Categoria> obtenerCategorias(){
        return categoriaService.obtenerCategorias();
    }

    //obtener los productos por las categorias
    @GetMapping(path = "/{idCategoria}/productos")
    public List<Producto> obtenerProductosPorCategoria(@PathVariable Long idCategoria){
        return categoriaService.obtenerProductosPorCategoria(idCategoria);
    }

    //crear una categoria
    @PostMapping
    public Categoria crearCategoria(@Valid @RequestBody CrearCategoriaDto crearCategoriaDto) {
        return categoriaService.crearCategoria(crearCategoriaDto);
    }
}
