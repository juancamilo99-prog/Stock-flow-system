package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class ProductoService {

    //instanciamos el repository
    private final ProductoRepository productoRepository;

    //creamos el constructor y le pasamos el repository
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }


    //obtener todos los productos
    public List<Producto> obtenerTodosProductos(){
        return productoRepository.findAll();
    }

    //obtener productos por identificador
    public Producto obtenerProductoPorId(@PathVariable Long idProducto){

        return productoRepository.findById(idProducto)
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe el producto con el id: " + idProducto));
    }
}
