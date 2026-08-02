package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.dto.creardto.CrearCategoriaDto;
import org.jcdev.stockflow.backend.entity.Categoria;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.repository.CategoriaRepository;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;


    public CategoriaService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    //obtener todas las categorias
    public List<Categoria> obtenerCategorias(){
        return categoriaRepository.findAll();
    }

    //obtener productos por categoria
    public List<Producto> obtenerProductosPorCategoria(Long idCategoria) {
            if (!categoriaRepository.existsById(idCategoria)) {
                throw new IllegalArgumentException("La categoria no existe: "+idCategoria);
            }
        return productoRepository.findByCategoriaId(idCategoria);
    }

    //crear una categoria
    public Categoria crearCategoria(CrearCategoriaDto crearCategoriaDto) {
        String nombre = crearCategoriaDto.getNombre().trim();
        if (categoriaRepository.existsByNombreIgnoreCase(crearCategoriaDto.getNombre())) {
            throw new IllegalArgumentException("La categoria ya existe: "+crearCategoriaDto.getNombre());
        }
        Categoria categoria = new Categoria(nombre);
        return categoriaRepository.save(categoria);
    }

    //actualizar una categoria
    public Categoria actualizarCategoria(Long idCategoria, CrearCategoriaDto crearCategoriaDto) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new IllegalArgumentException("La categoria no existe: "+idCategoria));

        String nombre = crearCategoriaDto.getNombre().trim();

        if (categoria.getNombre().equalsIgnoreCase(nombre)) {
            throw new IllegalArgumentException(
                    "El nuevo nombre debe ser diferente del nombre actual"
            );
        }

        if (categoriaRepository.existsByNombreIgnoreCase(crearCategoriaDto.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoria con el nombre "+crearCategoriaDto.getNombre());
        }
        categoria.setNombre(nombre);
        return categoriaRepository.save(categoria);
    }
}
