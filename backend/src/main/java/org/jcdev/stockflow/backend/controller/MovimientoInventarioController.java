package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.jcdev.stockflow.backend.dto.creardto.AjusteInventarioDto;
import org.jcdev.stockflow.backend.entity.MovimientoInventario;
import org.jcdev.stockflow.backend.service.MovimientoInventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/movimientos")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoInventarioService;

    public MovimientoInventarioController(MovimientoInventarioService movimientoInventarioService) {
        this.movimientoInventarioService = movimientoInventarioService;
    }

    @GetMapping
    public List<MovimientoInventario> obtenerMovimientoInventarios() {
       return movimientoInventarioService.obtenerMovimientoInventarios();
    }

    //movimientos por pedido
    @GetMapping(path = "/pedido/{idPedido}")
    public List<MovimientoInventario> obtenerMovimientoInventariosByPedidoId(@PathVariable Long idPedido) {
        return movimientoInventarioService.obtenerMovimientoInventariosByPedidoId(idPedido);
    }

    //movimientos por usuario
    @GetMapping(path = "/usuario/{idUsuario}")
    public List<MovimientoInventario> obtenerMovimientoInventariosByUsuarioId(@PathVariable Long idUsuario) {
        return movimientoInventarioService.obtenerMovimientoInventariosByUsuarioId(idUsuario);
    }

    //movimientos por productos
    @GetMapping(path = "/producto/{idProducto}")
    public List<MovimientoInventario> obtenerMovimientoInventariosByProductoId(@PathVariable Long idProducto) {
        return movimientoInventarioService.obtenerMovimientoInventariosByProductoId(idProducto);
    }

    //movimientos por recepcion
    @GetMapping(path = "/recepcion/{idRecepcion}")
    public List<MovimientoInventario> obtenerMovimientoInventariosByRecepcionId(@PathVariable Long idRecepcion) {
        return movimientoInventarioService.obtenerMovimientoInventariosByRecepcionId(idRecepcion);
    }

    @PostMapping(path = "/ajuste")
    public ResponseEntity<MovimientoInventario> ajusteMovimiento(@Valid @RequestBody AjusteInventarioDto ajusteInventarioDto) {
        MovimientoInventario movimientoInventario = movimientoInventarioService.ajusteMovimiento(ajusteInventarioDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoInventario);
    }
}
