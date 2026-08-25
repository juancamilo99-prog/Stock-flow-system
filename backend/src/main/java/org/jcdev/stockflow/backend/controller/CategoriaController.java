package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.creardto.CrearCategoriaDto;
import org.jcdev.stockflow.backend.entity.Categoria;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.repository.CategoriaRepository;
import org.jcdev.stockflow.backend.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Categoria>> obtenerCategorias(){
        List<Categoria> categoria = categoriaService.obtenerCategorias();
        return ResponseEntity.ok(categoria);
    }

    //obtener los productos por las categorias
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{idCategoria}/productos")
    public ResponseEntity<List<Producto>> obtenerProductosPorCategoria(@PathVariable Long idCategoria){
        List<Producto> productos = categoriaService.obtenerProductosPorCategoria(idCategoria);
        return ResponseEntity.ok(productos);
    }

    //crear una categoria
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @PostMapping
    public ResponseEntity<Categoria> crearCategoria(@Valid @RequestBody CrearCategoriaDto crearCategoriaDto) {
        Categoria categoria = categoriaService.crearCategoria(crearCategoriaDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoria);
    }

    //actualizar
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @PatchMapping(path = "/{idCategoria}")
    public ResponseEntity<Categoria> actualizarCategoria(@PathVariable Long idCategoria, @Valid @RequestBody CrearCategoriaDto crearCategoriaDto) {
        Categoria categoria = categoriaService.actualizarCategoria(idCategoria, crearCategoriaDto);
        return ResponseEntity.ok(categoria);
    }
}
