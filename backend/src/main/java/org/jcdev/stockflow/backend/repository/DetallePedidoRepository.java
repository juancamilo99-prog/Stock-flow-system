package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoId(Long idPedido);
    //existe este producto dentro de este pedido?
    boolean existsByPedidoIdAndProductoId(Long idPedido, Long idProducto);
}
