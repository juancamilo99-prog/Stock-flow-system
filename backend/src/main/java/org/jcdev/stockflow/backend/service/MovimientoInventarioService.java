package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.entity.MovimientoInventario;
import org.jcdev.stockflow.backend.repository.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoInventarioRepository) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
    }

    public List<MovimientoInventario> obtenerMovimientoInventarios() {
        return movimientoInventarioRepository.findAll();
    }

    //movimiento de pedidos
    public List<MovimientoInventario> obtenerMovimientoInventariosByPedidoId(Long idPedido) {
        return movimientoInventarioRepository.findByPedidoId(idPedido);
    }

    //movimientos por usuario
    public List<MovimientoInventario> obtenerMovimientoInventariosByUsuarioId(Long idUsuario) {
        return movimientoInventarioRepository.findByUsuarioId(idUsuario);
    }

    //movimientos por producto
    public List<MovimientoInventario> obtenerMovimientoInventariosByProductoId(Long idProducto) {
        return movimientoInventarioRepository.findByProductoId(idProducto);
    }

    //movimientos por recepcion
    public List<MovimientoInventario> obtenerMovimientoInventariosByRecepcionId(Long idRecepcion) {
        return movimientoInventarioRepository.findByRecepcionId(idRecepcion);
    }
}
