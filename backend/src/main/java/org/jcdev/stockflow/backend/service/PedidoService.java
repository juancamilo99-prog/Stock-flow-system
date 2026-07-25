package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.entity.DetallePedido;
import org.jcdev.stockflow.backend.entity.Pedido;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.repository.DetallePedidoRepository;
import org.jcdev.stockflow.backend.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository, DetallePedidoRepository detallePedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    //obtener todos los pedidos
    public List<Pedido> obtenerPedidos(){
        return pedidoRepository.findAll();
    }

    //obtenemos el detalle de pedidos por identificador
    public List<DetallePedido> obtenerDetallesPorPedido(Long idPedido){
        pedidoRepository.findById(idPedido).orElseThrow(() ->
                new IllegalArgumentException("El pedido no existe")
        );

        return detallePedidoRepository.findByPedidoId(idPedido);
    }

    //obtener todos los detalles
    public List<DetallePedido> obtenerDetalles(){
        return detallePedidoRepository.findAll();
    }
}