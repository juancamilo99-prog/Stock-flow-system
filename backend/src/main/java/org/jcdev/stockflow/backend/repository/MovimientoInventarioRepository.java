package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.MovimientoInventario;
import org.jcdev.stockflow.backend.enums.movimiento.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {

    List<MovimientoInventario> findByPedidoId(Long idPedido);
    List<MovimientoInventario> findByUsuarioId(Long idUsuario);
    List<MovimientoInventario> findByProductoId(Long idProducto);
    List<MovimientoInventario> findByRecepcionId(Long idRecepcion);

    Optional<MovimientoInventario> findByRecepcionIdAndProductoIdAndTipoMovimiento(Long recepcionId, Long productoId, TipoMovimiento tipoMovimiento);
}
