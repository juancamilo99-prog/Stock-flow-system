package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarDetallePedidoDto;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarPedidoDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearDetallePedidoDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearPedidoDto;
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

    //ver pedidos
    @GetMapping
    public List<Pedido> obtenerPedidos() {
        return pedidoService.obtenerPedidos();
    }

    //ver pedidos por identificador
    @GetMapping(path = "/{idPedido}")
    public Pedido obtenerPedidoPorId(@PathVariable Long idPedido) {
        return pedidoService.obtenerPedidoId(idPedido);
    }

    //ver detalles por pedido
    @GetMapping(path = "/{idPedido}/detalle")
    public List<DetallePedido> obtenerDetallePedidos(@PathVariable Long idPedido) {
        return pedidoService.obtenerDetallesPorPedido(idPedido);
    }

    //ver detalles
    @GetMapping(path = "/detalles")
    public List<DetallePedido> obtenerDetalles(){
        return pedidoService.obtenerDetalles();
    }

    //ver detalles por identificador
    @GetMapping(path = "/detalles/{idDetallePedido}")
    public DetallePedido obtenerDetallePorId(@PathVariable Long idDetallePedido){
        return pedidoService.obtenerDetallePorId(idDetallePedido);
    }

    //crear pedido
    @PostMapping
    public Pedido crearPedido(@Valid @RequestBody CrearPedidoDto crearPedidoDto){
        return pedidoService.crearPedido(crearPedidoDto);
    }

    //actualizar pedido
    @PatchMapping(path = "/{idPedido}")
    public Pedido actualizarPedido(@PathVariable Long idPedido,@Valid @RequestBody ActualizarPedidoDto actualizarPedidoDto){
        return pedidoService.actualizarPedido(idPedido,actualizarPedidoDto);
    }

    //eliminar pedido
    @DeleteMapping(path = "/{idPedido}")
    public Pedido eliminarPedido(@PathVariable Long idPedido){
        return pedidoService.eliminarPedido(idPedido);
    }

    //crear detalle pedido
    @PostMapping(path = "/detalles-pedido")
    public DetallePedido crearDetallePedido(@Valid @RequestBody CrearDetallePedidoDto crearDetallePedidoDto){
        return pedidoService.crearDetallePedido(crearDetallePedidoDto);
    }

    //actualizar detalle pedido
    @PatchMapping(path = "/actualizar-detalle/{idDetallePedido}")
    public DetallePedido actualizarDetallePedido(@PathVariable Long idDetallePedido, @Valid @RequestBody ActualizarDetallePedidoDto actualizarDetallePedidoDto){
        return pedidoService.actualizarDetallePedido(idDetallePedido,actualizarDetallePedidoDto);
    }

    //elminar detalle pedido
    @DeleteMapping(path = "/eliminar-detalle/{idDetallePedido}")
    public DetallePedido eliminarDetallePedido(@PathVariable Long idDetallePedido){
        return pedidoService.eliminarDetallePedido(idDetallePedido);
    }
}
