package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarProductoDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearProductoDto;
import org.jcdev.stockflow.backend.entity.*;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;
import org.jcdev.stockflow.backend.repository.CategoriaRepository;
import org.jcdev.stockflow.backend.repository.EmpresaRepository;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.jcdev.stockflow.backend.repository.UbicacionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProductoService {

    //instanciamos el repository
    private final ProductoRepository productoRepository;
    private final EmpresaRepository empresaRepository;
    private final CategoriaRepository categoriaRepository;
    private final UbicacionRepository ubicacionRepository;
    private static final String PREFIJO_CODIGO = "COD";

    //servicios
    private final AuditoriaService auditoriaService;

    //creamos el constructor y le pasamos el repository
    public ProductoService(ProductoRepository productoRepository, EmpresaRepository empresaRepository, CategoriaRepository categoriaRepository, UbicacionRepository ubicacionRepository, AuditoriaService auditoriaService) {
        this.productoRepository = productoRepository;
        this.empresaRepository = empresaRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.auditoriaService = auditoriaService;
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

    //metodo para generar un codigo de barras
    private String generarCodigoBarras(){
        String numeroAleatorio = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 1_000_000));
        String codigoBarras = PREFIJO_CODIGO.concat(numeroAleatorio);
        while (productoRepository.existsByCodigoBarras(codigoBarras)){
            numeroAleatorio = String.valueOf(ThreadLocalRandom.current().nextInt(1000, 1_000_000));
            codigoBarras = PREFIJO_CODIGO.concat(numeroAleatorio);
        }
        return codigoBarras;
    }

    //crear un producto
    @Transactional
    public Producto crearProducto(CrearProductoDto crearProductoDto){
        //validamos que la fecha de caducidad no sea inferior a la de producción
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

        producto = productoRepository.save(producto);
        auditoriaService.registrarAuditoria(
                TipoAccion.CREAR,
                EntidadAuditoria.PRODUCTO,
                "Se ha creado un producto nuevo "+producto.getId(),
                producto.getId(),
                null
        );
        return producto;
    }

    //actualizar un producto
    public Producto actualizarProducto(Long idProducto, ActualizarProductoDto actualizarProductoDto){
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("No existe el producto con el id: " + idProducto));

        LocalDate fechaProduccionFinal = actualizarProductoDto.getFechaProduccion() != null
                ? actualizarProductoDto.getFechaProduccion() : producto.getFechaProduccion();
        LocalDate fechaCaducidadFinal = actualizarProductoDto.getFechaCaducidad() != null
                ? actualizarProductoDto.getFechaCaducidad() : producto.getFechaCaducidad();

        if (actualizarProductoDto.getNombre() != null){
            producto.setNombre(actualizarProductoDto.getNombre());
        }
        if (actualizarProductoDto.getDescripcion() != null){
            producto.setDescripcion(actualizarProductoDto.getDescripcion());
        }
        if (fechaProduccionFinal != null && fechaCaducidadFinal != null && fechaCaducidadFinal.isBefore(fechaProduccionFinal)){
            throw new IllegalArgumentException(
                    "La fecha de caducidad no puede ser anterior a la fecha de produccion"
            );
        }
        if (actualizarProductoDto.getFechaProduccion() != null){
            producto.setFechaProduccion(actualizarProductoDto.getFechaProduccion());
        }
        if (actualizarProductoDto.getFechaCaducidad() != null){
            producto.setFechaCaducidad(fechaCaducidadFinal);
        }
        if (actualizarProductoDto.getIdEmpresa() != null){
            Empresa empresa = empresaRepository.findById(actualizarProductoDto.getIdEmpresa())
                    .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
            producto.setEmpresa(empresa);
        }
        if (actualizarProductoDto.getIdCategoria() != null){
            Categoria categoria = categoriaRepository.findById(actualizarProductoDto.getIdCategoria())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
            producto.setCategoria(categoria);
        }
        if (actualizarProductoDto.getIdUbicacion() != null){
            Ubicacion ubicacion = ubicacionRepository.findById(actualizarProductoDto.getIdUbicacion())
                    .orElseThrow(() -> new IllegalArgumentException("Ubicacion no encontrada"));
            producto.setUbicacion(ubicacion);
        }
        if (actualizarProductoDto.getActivo() != null){
            producto.setActivo(actualizarProductoDto.getActivo());
        }

        producto = productoRepository.save(producto);
        return producto;
    }

    //eliminar un producto
    public Producto eliminarProducto(Long idProducto){
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("No existe el producto con el id: " + idProducto));
        productoRepository.delete(producto);
        return producto;
    }
}
