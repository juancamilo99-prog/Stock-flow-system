package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarDetallePedidoDto;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarPedidoDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearDetallePedidoDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearPedidoDto;
import org.jcdev.stockflow.backend.entity.*;
import org.jcdev.stockflow.backend.enums.EstadoPedido;
import org.jcdev.stockflow.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, DetallePedidoRepository detallePedidoRepository, EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    //obtener todos los pedidos
    public List<Pedido> obtenerPedidos(){
        return pedidoRepository.findAll();
    }

    //obtener pedido por identificador
    public Pedido obtenerPedidoId(Long idPedido){
        return pedidoRepository.findById(idPedido)
                .orElseThrow(
                        () -> new IllegalArgumentException("Pedido no encontrado")
                );
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

    //obtener detalle pedido por su identificador
    public DetallePedido obtenerDetallePorId(Long idDetallePedido){
        return detallePedidoRepository.findById(idDetallePedido)
                .orElseThrow(() -> new IllegalArgumentException("El detalle de pedido no existe"));
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

    //actualizar pedido
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

    //eliminar pedido
    public Pedido eliminarPedido(Long idPedido){
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe"));
        pedidoRepository.delete(pedido);
        return pedido;
    }

    //crear detalle de pedido
    public DetallePedido crearDetallePedido(CrearDetallePedidoDto crearDetallePedidoDto){

        Pedido pedido = pedidoRepository.findById(crearDetallePedidoDto.getIdPedido())
                .orElseThrow(() -> new IllegalArgumentException("El pedido no existe"));
        Producto producto = productoRepository.findById(crearDetallePedidoDto.getIdProducto())
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe"));
        if (crearDetallePedidoDto.getCantidadSolicitada() <= 0){
            throw new IllegalArgumentException("La cantidad de solicitada debe ser mayor que 0");
        }
        if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO || pedido.getEstadoPedido() == EstadoPedido.RECIBIDO){
            throw new IllegalArgumentException("no se puede añadir un detalle a un pedido cancelado o recibido");
        }
        if (detallePedidoRepository.existsByPedidoIdAndProductoId(pedido.getId(), producto.getId())){
            throw new IllegalArgumentException("Ya existe un producto con el detalle de solicitado");
        }

        DetallePedido detallePedido = new DetallePedido(crearDetallePedidoDto.getCantidadSolicitada(),pedido,producto);
        return detallePedidoRepository.save(detallePedido);
    }

    //actualizar detalle pedido
    public DetallePedido actualizarDetallePedido(Long idDetallePedido ,ActualizarDetallePedidoDto actualizarDetallePedidoDto){
        DetallePedido detallePedido = detallePedidoRepository.findById(idDetallePedido)
                .orElseThrow(() -> new IllegalArgumentException("El detalle de pedido no existe"));

        if (actualizarDetallePedidoDto.getCantidadSolicitada() != null){
            if (actualizarDetallePedidoDto.getCantidadSolicitada() <= 0){
                throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
            }
        }

        Pedido pedido = detallePedido.getPedido();
        if (pedido.getEstadoPedido() == EstadoPedido.RECIBIDO || pedido.getEstadoPedido() == EstadoPedido.CANCELADO){
            throw new IllegalArgumentException(
                    "No se puede modificar un detalle de un pedido recibido o cancelado"
            );
        }
        detallePedido.setCantidadSolicitada(actualizarDetallePedidoDto.getCantidadSolicitada());

        return detallePedidoRepository.save(detallePedido);
    }

    public DetallePedido eliminarDetallePedido(Long idDetallePedido){
        DetallePedido detallePedido = detallePedidoRepository.findById(idDetallePedido)
                .orElseThrow(
                        () -> new IllegalArgumentException("El detalle del pedido no existe")
                );
        Pedido pedido = detallePedido.getPedido();
        if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO || pedido.getEstadoPedido() == EstadoPedido.RECIBIDO){
            throw new IllegalArgumentException(
                    "No se puede eliminar un detalle de un pedido cancelado o recibido"
            );
        }
        detallePedidoRepository.delete(detallePedido);
        return detallePedido;
    }
}