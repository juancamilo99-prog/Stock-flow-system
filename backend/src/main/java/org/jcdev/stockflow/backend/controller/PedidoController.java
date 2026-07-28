package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.ActualizarPedidoDto;
import org.jcdev.stockflow.backend.dto.CrearPedidoDto;
import org.jcdev.stockflow.backend.entity.DetallePedido;
import org.jcdev.stockflow.backend.entity.Pedido;
import org.jcdev.stockflow.backend.service.PedidoService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public Pedido crearPedido(@Valid @RequestBody CrearPedidoDto crearPedidoDto){
        return pedidoService.crearPedido(crearPedidoDto);
    }

    @PatchMapping(path = "/{idPedido}")
    public Pedido actualizarPedido(@PathVariable Long idPedido,@Valid @RequestBody ActualizarPedidoDto actualizarPedidoDto){
        return pedidoService.actualizarPedido(idPedido,actualizarPedidoDto);
    }

    @DeleteMapping(path = "/{idPedido}")
    public Pedido eliminarPedido(@PathVariable Long idPedido){
        return pedidoService.eliminarPedido(idPedido);
    }
}
