package org.jcdev.stockflow.backend.repository;


import org.jcdev.stockflow.backend.entity.Categoria;
import org.jcdev.stockflow.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaId(Long idCategoria);
    List<Producto> findByUbicacionId(Long idUbicacion);
    List<Producto> findByEmpresaId(Long idEmpresa);

    boolean existsByNombreAndEmpresaId(String nombre, Long idEmpresa);
    boolean existsByCodigoBarras(String codigoBarras);
}
