package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarDetallePedidoDto;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarPedidoDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearDetallePedidoDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearPedidoDto;
import org.jcdev.stockflow.backend.entity.DetallePedido;
import org.jcdev.stockflow.backend.entity.Pedido;
import org.jcdev.stockflow.backend.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerPedidos();
        return ResponseEntity.ok(pedidos);
    }

    //ver pedidos por identificador
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{idPedido}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Long idPedido) {
        Pedido pedido = pedidoService.obtenerPedidoId(idPedido);
        return ResponseEntity.ok(pedido);
    }

    //ver detalles por pedido
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{idPedido}/detalle")
    public ResponseEntity<List<DetallePedido>> obtenerDetallePedidos(@PathVariable Long idPedido) {
        List<DetallePedido> pedidos = pedidoService.obtenerDetallesPorPedido(idPedido);
        return ResponseEntity.ok(pedidos);
    }

    //ver detalles
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/detalles")
    public ResponseEntity<List<DetallePedido>> obtenerDetalles(){
        List<DetallePedido> detallePedidos = pedidoService.obtenerDetalles();
        return ResponseEntity.ok(detallePedidos);
    }

    //ver detalles por identificador
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/detalles/{idDetallePedido}")
    public ResponseEntity<DetallePedido> obtenerDetallePorId(@PathVariable Long idDetallePedido){
        DetallePedido detallePedido = pedidoService.obtenerDetallePorId(idDetallePedido);
        return ResponseEntity.ok(detallePedido);
    }

    //crear pedido
    @PreAuthorize("hasRole('COORDINADOR')")
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@Valid @RequestBody CrearPedidoDto crearPedidoDto){
        Pedido pedido = pedidoService.crearPedido(crearPedidoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    //actualizar pedido
    @PreAuthorize("hasRole('COORDINADOR')")
    @PatchMapping(path = "/{idPedido}")
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable Long idPedido,@Valid @RequestBody ActualizarPedidoDto actualizarPedidoDto){
        Pedido pedido = pedidoService.actualizarPedido(idPedido,actualizarPedidoDto);
        return ResponseEntity.ok(pedido);
    }

    //eliminar pedido
    @PreAuthorize("hasRole('COORDINADOR')")
    @DeleteMapping(path = "/{idPedido}")
    public ResponseEntity<Pedido> eliminarPedido(@PathVariable Long idPedido){
        Pedido pedidos = pedidoService.eliminarPedido(idPedido);
        return ResponseEntity.ok(pedidos);
    }

    //crear detalle pedido
    @PreAuthorize("hasRole('COORDINADOR')")
    @PostMapping(path = "/detalles-pedido")
    public ResponseEntity<DetallePedido> crearDetallePedido(@Valid @RequestBody CrearDetallePedidoDto crearDetallePedidoDto){
        DetallePedido detallePedido = pedidoService.crearDetallePedido(crearDetallePedidoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(detallePedido);
    }

    //actualizar detalle pedido
    @PreAuthorize("isAuthenticated()")
    @PatchMapping(path = "/actualizar-detalle/{idDetallePedido}")
    public ResponseEntity<DetallePedido> actualizarDetallePedido(@PathVariable Long idDetallePedido, @Valid @RequestBody ActualizarDetallePedidoDto actualizarDetallePedidoDto){
        DetallePedido detallePedido = pedidoService.actualizarDetallePedido(idDetallePedido,actualizarDetallePedidoDto);
        return ResponseEntity.ok(detallePedido);
    }

    //elminar detalle pedido
    @PreAuthorize("hasRole('COORDINADOR')")
    @DeleteMapping(path = "/eliminar-detalle/{idDetallePedido}")
    public ResponseEntity<DetallePedido> eliminarDetallePedido(@PathVariable Long idDetallePedido){
        DetallePedido detallePedido = pedidoService.eliminarDetallePedido(idDetallePedido);
        return ResponseEntity.ok(detallePedido);
    }
}
