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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
    @Transactional
    public Producto actualizarProducto(Long idProducto, ActualizarProductoDto actualizarProductoDto){
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("No existe el producto con el id: " + idProducto));

        LocalDate fechaProduccionFinal = actualizarProductoDto.getFechaProduccion() != null
                ? actualizarProductoDto.getFechaProduccion() : producto.getFechaProduccion();
        LocalDate fechaCaducidadFinal = actualizarProductoDto.getFechaCaducidad() != null
                ? actualizarProductoDto.getFechaCaducidad() : producto.getFechaCaducidad();

        boolean detectarCambio = false;

        if (actualizarProductoDto.getNombre() != null){
            String nombreNuevo = actualizarProductoDto.getNombre().trim();
            String nombreActual = producto.getNombre().trim();
            if (!nombreNuevo.equalsIgnoreCase(nombreActual)){
                producto.setNombre(nombreNuevo);
                detectarCambio = true;
            }
        }
        if (actualizarProductoDto.getDescripcion() != null){
            String descripcionNueva =  actualizarProductoDto.getDescripcion().trim();
            String descripcionActual = producto.getDescripcion().trim();
            if (!descripcionNueva.equalsIgnoreCase(descripcionActual)){
                producto.setDescripcion(descripcionNueva);
                detectarCambio = true;
            }
        }
        if (fechaProduccionFinal != null && fechaCaducidadFinal != null && fechaCaducidadFinal.isBefore(fechaProduccionFinal)){
            throw new IllegalArgumentException(
                    "La fecha de caducidad no puede ser anterior a la fecha de produccion"
            );
        }
        if (actualizarProductoDto.getFechaProduccion() != null){
            LocalDate fechaProduccionNueva = actualizarProductoDto.getFechaProduccion();
            LocalDate fechaProduccionActual = producto.getFechaProduccion();
            if (!fechaProduccionNueva.equals(fechaProduccionActual)){
                producto.setFechaProduccion(fechaProduccionNueva);
                detectarCambio = true;
            }
        }
        if (actualizarProductoDto.getFechaCaducidad() != null){
            LocalDate fechaCaducidadNueva = actualizarProductoDto.getFechaCaducidad();
            LocalDate fechaCaducidadActual = producto.getFechaCaducidad();
            if (!fechaCaducidadNueva.equals(fechaCaducidadActual)){
                producto.setFechaCaducidad(fechaCaducidadNueva);
                detectarCambio = true;
            }
        }
        if (actualizarProductoDto.getIdEmpresa() != null){
            Empresa empresa = empresaRepository.findById(actualizarProductoDto.getIdEmpresa())
                    .orElseThrow(() -> new IllegalArgumentException("Empresa no encontrada"));
            Long nuevaEmpresa = actualizarProductoDto.getIdEmpresa();
            Long actualEmpresa = producto.getEmpresa().getId();
            if (nuevaEmpresa.longValue() != actualEmpresa.longValue()){
                producto.setEmpresa(empresa);
                detectarCambio = true;
            }
        }
        if (actualizarProductoDto.getIdCategoria() != null){
            Categoria categoria = categoriaRepository.findById(actualizarProductoDto.getIdCategoria())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
            Long nuevaCategoria = actualizarProductoDto.getIdCategoria();
            Long actualCategoria = producto.getCategoria().getId();
            if (nuevaCategoria.longValue() != actualCategoria.longValue()){
                producto.setCategoria(categoria);
                detectarCambio = true;
            }
        }
        if (actualizarProductoDto.getIdUbicacion() != null){
            Ubicacion ubicacion = ubicacionRepository.findById(actualizarProductoDto.getIdUbicacion())
                    .orElseThrow(() -> new IllegalArgumentException("Ubicacion no encontrada"));
            Long  nuevaUbicacion = actualizarProductoDto.getIdUbicacion();
            Long actualUbicacion = producto.getUbicacion().getId();
            if (nuevaUbicacion.longValue() != actualUbicacion.longValue()){
                producto.setUbicacion(ubicacion);
                detectarCambio = true;
            }
        }
        if (actualizarProductoDto.getActivo() != null){
            boolean activoActual = producto.isActivo();
            boolean activoNuevo = actualizarProductoDto.getActivo();
            if (activoActual != activoNuevo){
                producto.setActivo(activoNuevo);
                detectarCambio = true;
            }
        }

        if (detectarCambio){
            //TODO el usuario se obtendra cuando implementemos Spring Security
            producto = productoRepository.save(producto);
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.PRODUCTO,
                    "Se ha actualizado un producto",
                    producto.getId(),
                    null
            );
        }else {
            throw new IllegalArgumentException("No se detecto ningún cambio de producto");
        }
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
