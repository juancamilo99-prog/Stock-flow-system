package org.jcdev.stockflow.backend.controller;

import org.jcdev.stockflow.backend.entity.DetallePedido;
import org.jcdev.stockflow.backend.entity.Pedido;
import org.jcdev.stockflow.backend.service.PedidoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> obtenerPedidos() {
        return pedidoService.obtenerPedidos();
    }

    @GetMapping(path = "/{idPedido}/detalle")
    public List<DetallePedido> obtenerDetallePedidos(@PathVariable Long idPedido) {
        return pedidoService.obtenerDetallesPorPedido(idPedido);
    }

    @GetMapping(path = "/detalles")
    public List<DetallePedido> obtenerDetalles(){
        return pedidoService.obtenerDetalles();
    }
}
