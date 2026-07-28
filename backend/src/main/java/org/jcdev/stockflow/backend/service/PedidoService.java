package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.dto.ActualizarPedidoDto;
import org.jcdev.stockflow.backend.dto.CrearPedidoDto;
import org.jcdev.stockflow.backend.entity.*;
import org.jcdev.stockflow.backend.enums.EstadoPedido;
import org.jcdev.stockflow.backend.repository.DetallePedidoRepository;
import org.jcdev.stockflow.backend.repository.EmpresaRepository;
import org.jcdev.stockflow.backend.repository.PedidoRepository;
import org.jcdev.stockflow.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;

    public PedidoService(PedidoRepository pedidoRepository, DetallePedidoRepository detallePedidoRepository, EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
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

    //crear pedido
    public Pedido crearPedido(CrearPedidoDto crearPedidoDto){
        Empresa empresa = empresaRepository.findById(crearPedidoDto.getIdEmpresa())
                    .orElseThrow(() -> new IllegalArgumentException("El empresa no existe"));
        Usuario usuario = usuarioRepository.findById(crearPedidoDto.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe"));
        Pedido pedido = new Pedido(crearPedidoDto.getObservaciones(), empresa, usuario);
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizarPedido(Long idPedido, ActualizarPedidoDto actualizarPedidoDto){
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe"));
        if (actualizarPedidoDto.getObservaciones() != null && !actualizarPedidoDto.getObservaciones().isBlank()) {
            pedido.setObservaciones(actualizarPedidoDto.getObservaciones());
        }
        if (actualizarPedidoDto.getEstadoPedido() != null){
            if (pedido.getEstadoPedido() == EstadoPedido.RECIBIDO && actualizarPedidoDto.getEstadoPedido() == EstadoPedido.PENDIENTE) {
                throw new IllegalArgumentException(
                        "Un pedido recibido no puede volver a estar pendiente"
                );
            }
            if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO && actualizarPedidoDto.getEstadoPedido() == EstadoPedido.RECIBIDO) {
                throw new IllegalArgumentException(
                        "Un pedido cancelado no puede estar recibido"
                );
            }
            if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO &&  actualizarPedidoDto.getEstadoPedido() == EstadoPedido.PENDIENTE) {
                throw new IllegalArgumentException(
                        "Un pedido cancelado no puede volver a estar en pendiente"
                );
            }
            if (pedido.getEstadoPedido() == EstadoPedido.RECIBIDO &&  actualizarPedidoDto.getEstadoPedido() == EstadoPedido.CANCELADO) {
                throw new IllegalArgumentException(
                        "Un pedido recibido no puede cancelarse"
                );
            }
            if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO &&  actualizarPedidoDto.getEstadoPedido() == EstadoPedido.PARCIAL) {
                throw new IllegalArgumentException(
                        "Un pedido cancelado no puede pasar a estado parcial"
                );
            }
            if (pedido.getEstadoPedido() == EstadoPedido.RECIBIDO && actualizarPedidoDto.getEstadoPedido() == EstadoPedido.PARCIAL){
                throw new IllegalArgumentException(
                        "Un pedido recibido no puede volver a estar parcial"
                );
            }
            pedido.setEstadoPedido(actualizarPedidoDto.getEstadoPedido());
        }
        return pedidoRepository.save(pedido);
    }

    public Pedido eliminarPedido(Long idPedido){
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe"));
        pedidoRepository.delete(pedido);
        return pedido;
    }


}