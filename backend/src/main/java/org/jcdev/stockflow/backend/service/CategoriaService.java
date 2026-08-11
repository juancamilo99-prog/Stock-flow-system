package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.creardto.CrearCategoriaDto;
import org.jcdev.stockflow.backend.entity.Categoria;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;
import org.jcdev.stockflow.backend.repository.CategoriaRepository;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    //servicios
    private final AuditoriaService auditoriaService;


    public CategoriaService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository, AuditoriaService auditoriaService) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.auditoriaService = auditoriaService;
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
    @Transactional
    public Categoria crearCategoria(CrearCategoriaDto crearCategoriaDto) {
        String nombre = crearCategoriaDto.getNombre().trim();
        if (categoriaRepository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("La categoria ya existe: "+crearCategoriaDto.getNombre());
        }
        Categoria categoria = new Categoria(nombre);
        categoria = categoriaRepository.save(categoria);
        auditoriaService.registrarAuditoria(
                TipoAccion.CREAR,
                EntidadAuditoria.CATEGORIA,
                "Se ha creado una categoria nueva: "+categoria.getNombre(),
                categoria.getId(),
                null
        );
        return categoria;
    }

    //actualizar una categoria
    @Transactional
    public Categoria actualizarCategoria(Long idCategoria, CrearCategoriaDto crearCategoriaDto) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new IllegalArgumentException("La categoria no existe: "+idCategoria));

        boolean cambioNombre = false;

        if (crearCategoriaDto.getNombre() != null){
            String nuevoNombre = crearCategoriaDto.getNombre().trim();
            String nombreActual = categoria.getNombre().trim();
            if (nuevoNombre.isBlank()){
                throw new IllegalArgumentException(
                        "El nombre no puede estar vacio"
                );
            }
            if (!nuevoNombre.equalsIgnoreCase(nombreActual)){
                if (categoriaRepository.existsByNombreIgnoreCaseAndIdNot(nuevoNombre, idCategoria)) {
                    throw new IllegalArgumentException("Ya existe una categoria con el nombre "+nuevoNombre);
                }
                categoria.setNombre(nuevoNombre);
                cambioNombre = true;
            }
        }
        if (cambioNombre){
            categoria = categoriaRepository.save(categoria);
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.CATEGORIA,
                    "Se ha modificado el nombre de la categoria a: "+categoria.getNombre(),
                    categoria.getId(),
                    null
            );
        }else {
            throw new IllegalArgumentException("No se detecto ningún cambio");
        }
        return categoria;
    }
}
