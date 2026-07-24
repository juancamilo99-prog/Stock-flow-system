package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.dto.CrearProductoDto;
import org.jcdev.stockflow.backend.entity.Categoria;
import org.jcdev.stockflow.backend.entity.Empresa;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.entity.Ubicacion;
import org.jcdev.stockflow.backend.repository.CategoriaRepository;
import org.jcdev.stockflow.backend.repository.EmpresaRepository;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.jcdev.stockflow.backend.repository.UbicacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.random;

@Service
public class ProductoService {

    //instanciamos el repository
    private final ProductoRepository productoRepository;
    private final EmpresaRepository empresaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UbicacionRepository ubicacionRepository;
    private static final String PREFIJO_CODIGO = "COD";

    //creamos el constructor y le pasamos el repository
    public ProductoService(ProductoRepository productoRepository, EmpresaRepository empresaRepository, CategoriaRepository categoriaRepository, UbicacionRepository ubicacionRepository) {
        this.productoRepository = productoRepository;
        this.empresaRepository = empresaRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;
    }


    //obtener todos los productos
    public List<Producto> obtenerTodosProductos(){
        return productoRepository.findAll();
    }

    //obtener productos por identificador
    public Producto obtenerProductoPorId(Long idProducto){

        return productoRepository.findById(idProducto)
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe el producto con el id: " + idProducto));
    }

    private String generarCodigoBarras(){
        String numeroAleatorio = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 1_000_000));
        String codigoBarras = PREFIJO_CODIGO.concat(numeroAleatorio);
        while (productoRepository.existsByCodigoBarras(codigoBarras)){
            numeroAleatorio = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 1_000_000));
            codigoBarras = PREFIJO_CODIGO.concat(numeroAleatorio);
        }
        return codigoBarras;
    }

    public Producto crearProducto(CrearProductoDto crearProductoDto){

        if (crearProductoDto.getFechaCaducidad().isBefore(crearProductoDto.getFechaProduccion())){
            throw  new IllegalArgumentException("la fecha de caducidad debe ser posterior a la fecha de produccion");
        }

        Producto producto = new Producto(); // producto vacio
        if (productoRepository.existsByNombreAndEmpresaId(crearProductoDto.getNombre(), crearProductoDto.getIdEmpresa())){
            throw new IllegalArgumentException("Ya existe un producto con ese nombre para esta empresa");
        }
        producto.setNombre(crearProductoDto.getNombre());
        producto.setDescripcion(crearProductoDto.getDescripcion());
        producto.setStock(crearProductoDto.getStock());
        producto.setFechaProduccion(crearProductoDto.getFechaProduccion());
        producto.setFechaCaducidad(crearProductoDto.getFechaCaducidad());
        //buscamos la empresa
        Empresa empresa = empresaRepository.findById(crearProductoDto.getIdEmpresa())
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe la empresa con el id introducido para crear este producto: "+crearProductoDto.getIdEmpresa()));
        //la relacionamos
        producto.setEmpresa(empresa);
        //buscamos la categoria
        Categoria categoria = categoriaRepository.findById(crearProductoDto.getIdCategoria())
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe la categoria con el id: " + crearProductoDto.getIdCategoria()));
        //la relacionamos
        producto.setCategoria(categoria);
        //buscamos la ubicacion
        Ubicacion ubicacion = ubicacionRepository.findById(crearProductoDto.getIdUbicacion())
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe ubicacion para crear este producto: "+crearProductoDto.getIdUbicacion()));
        //la relacionamos
        producto.setUbicacion(ubicacion);
        //creamos el producto

        //generar codigo de barras
        producto.setCodigoBarras(generarCodigoBarras());

        return productoRepository.save(producto);

    }
}
