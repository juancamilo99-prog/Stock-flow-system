package org.jcdev.stockflow.backend.service;

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
}
