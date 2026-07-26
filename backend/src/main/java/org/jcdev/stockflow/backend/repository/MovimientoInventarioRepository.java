package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findByPedidoId(Long idPedido);
    List<MovimientoInventario> findByUsuarioId(Long idUsuario);
    List<MovimientoInventario> findByProductoId(Long idProducto);
    List<MovimientoInventario> findByRecepcionId(Long idRecepcion);
}
