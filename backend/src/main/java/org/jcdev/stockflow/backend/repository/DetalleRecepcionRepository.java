package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.DetalleRecepcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleRecepcionRepository extends JpaRepository<DetalleRecepcion, Long> {

    List<DetalleRecepcion> findByRecepcionId(Long idRecepcion);
    List<DetalleRecepcion> findByProductoId(Long idProducto);
    boolean existsByRecepcionIdAndProductoId(Long idRecepcion, Long idProducto);
}
