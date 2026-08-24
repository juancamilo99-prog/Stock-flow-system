package org.jcdev.stockflow.backend.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.jcdev.stockflow.backend.dto.creardto.AjusteInventarioDto;
import org.jcdev.stockflow.backend.entity.MovimientoInventario;
import org.jcdev.stockflow.backend.service.MovimientoInventarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/movimientos")
public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoInventarioService;

    public MovimientoInventarioController(MovimientoInventarioService movimientoInventarioService) {
        this.movimientoInventarioService = movimientoInventarioService;
    }

    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping
    public ResponseEntity<List<MovimientoInventario>> obtenerMovimientoInventarios() {
        List<MovimientoInventario> movimientoInventarios = movimientoInventarioService.obtenerMovimientoInventarios();
        return ResponseEntity.ok(movimientoInventarios);
    }

    //movimientos por pedido
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping(path = "/pedido/{idPedido}")
    public ResponseEntity<List<MovimientoInventario>> obtenerMovimientoInventariosByPedidoId(@PathVariable Long idPedido) {
        List<MovimientoInventario> movimientoInventarios = movimientoInventarioService.obtenerMovimientoInventariosByPedidoId(idPedido);
        return ResponseEntity.ok(movimientoInventarios);
    }

    //movimientos por usuario
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO') OR @authorizationService.esUsuarioActual(#idUsuario)")
    @GetMapping(path = "/usuario/{idUsuario}")
    public ResponseEntity<List<MovimientoInventario>> obtenerMovimientoInventariosByUsuarioId(@PathVariable Long idUsuario) {
        List<MovimientoInventario> movimientoInventarios = movimientoInventarioService.obtenerMovimientoInventariosByUsuarioId(idUsuario);
        return ResponseEntity.ok(movimientoInventarios);
    }

    //movimientos por productos
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping(path = "/producto/{idProducto}")
    public ResponseEntity<List<MovimientoInventario>> obtenerMovimientoInventariosByProductoId(@PathVariable Long idProducto) {
        List<MovimientoInventario> movimientoInventarios = movimientoInventarioService.obtenerMovimientoInventariosByProductoId(idProducto);
        return ResponseEntity.ok(movimientoInventarios);
    }

    //movimientos por recepcion
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @GetMapping(path = "/recepcion/{idRecepcion}")
    public ResponseEntity<List<MovimientoInventario>> obtenerMovimientoInventariosByRecepcionId(@PathVariable Long idRecepcion) {
        List<MovimientoInventario> movimientoInventarios = movimientoInventarioService.obtenerMovimientoInventariosByRecepcionId(idRecepcion);
        return ResponseEntity.ok(movimientoInventarios);
    }

    //crear un movimiento inventario
    @PreAuthorize("hasRole('COORDINADOR') OR hasRole('ENCARGADO')")
    @PostMapping(path = "/ajuste")
    public ResponseEntity<MovimientoInventario> ajusteMovimiento(@Valid @RequestBody AjusteInventarioDto ajusteInventarioDto) {
        MovimientoInventario movimientoInventario = movimientoInventarioService.ajusteMovimiento(ajusteInventarioDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoInventario);
    }
}
