package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarDetallePedidoDto;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarPedidoDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearDetallePedidoDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearPedidoDto;
import org.jcdev.stockflow.backend.entity.*;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;
import org.jcdev.stockflow.backend.enums.pedido.EstadoPedido;
import org.jcdev.stockflow.backend.exception.CambioNoDetectadoException;
import org.jcdev.stockflow.backend.exception.RecursoNoEncontradoException;
import org.jcdev.stockflow.backend.exception.TransicionEstadoInvalidaException;
import org.jcdev.stockflow.backend.repository.*;
import org.jcdev.stockflow.backend.service.security.AuthorizationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    //servicios
    private final AuditoriaService auditoriaService;

    //autorizaciones
    private final AuthorizationService authorizationService;

    public PedidoService(PedidoRepository pedidoRepository, DetallePedidoRepository detallePedidoRepository, EmpresaRepository empresaRepository, UsuarioRepository usuarioRepository, ProductoRepository productoRepository, AuditoriaService auditoriaService, AuthorizationService authorizationService) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.auditoriaService = auditoriaService;
        this.authorizationService = authorizationService;
    }

    //obtener todos los pedidos
    public List<Pedido> obtenerPedidos(){
        return pedidoRepository.findAll();
    }

    //obtener pedido por identificador
    public Pedido obtenerPedidoId(Long idPedido){
        return pedidoRepository.findById(idPedido)
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("Pedido no encontrado")
                );
    }

    //obtenemos el detalle de pedidos por identificador
    public List<DetallePedido> obtenerDetallesPorPedido(Long idPedido){
        pedidoRepository.findById(idPedido).orElseThrow(() ->
                new RecursoNoEncontradoException("El pedido no existe")
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
                .orElseThrow(() -> new RecursoNoEncontradoException("El detalle de pedido no existe"));
    }

    //crear pedido
    @Transactional
    public Pedido crearPedido(CrearPedidoDto crearPedidoDto){
        Empresa empresa = empresaRepository.findById(crearPedidoDto.getIdEmpresa())
                    .orElseThrow(() -> new RecursoNoEncontradoException("El empresa no existe"));
        Usuario usuario = usuarioRepository.findById(crearPedidoDto.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no existe"));
        Pedido pedido = new Pedido(crearPedidoDto.getObservaciones(), empresa, usuario);
        pedido = pedidoRepository.save(pedido);
        auditoriaService.registrarAuditoria(
                TipoAccion.CREAR,
                EntidadAuditoria.PEDIDO,
                "Se ha creado un pedido nuevo para la empresa "+empresa.getNombre(),
                pedido.getId(),
                authorizationService.obtenerUsuarioAutenticado()
        );
        return pedido;
    }

    //actualizar pedido
    @Transactional
    public Pedido actualizarPedido(Long idPedido, ActualizarPedidoDto actualizarPedidoDto){
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RecursoNoEncontradoException("El pedido no existe"));

        boolean detectarCambio = false;
        boolean cambioEstado = false;

        if (actualizarPedidoDto.getObservaciones() != null) {
            String observacionesNuevas =  actualizarPedidoDto.getObservaciones().trim();
            String observacionActual = pedido.getObservaciones().trim();
            if (!observacionesNuevas.equalsIgnoreCase(observacionActual)) {
                pedido.setObservaciones(observacionesNuevas);
                detectarCambio = true;
            }
        }

        //refactorizacion
        EstadoPedido estadoNuevo = actualizarPedidoDto.getEstadoPedido();
        EstadoPedido estadoActual = pedido.getEstadoPedido();
        if (estadoNuevo != null &&  estadoNuevo != estadoActual) {
            switch (estadoActual){
                case RECIBIDO -> throw new TransicionEstadoInvalidaException("Un pedido recibido no puede cambiar de estado");
                case CANCELADO -> throw new TransicionEstadoInvalidaException("Un pedido cancelado no puede cambiar de estado");
                case PARCIAL -> {
                    if (estadoNuevo != EstadoPedido.RECIBIDO) {
                        throw new TransicionEstadoInvalidaException("Un pedido en estado parcial, solo puede cambiar de estado a recibido.");
                    }
                }
            }
            pedido.setEstadoPedido(estadoNuevo);
            cambioEstado = true;
        }
        if (detectarCambio || cambioEstado) {
            pedido =  pedidoRepository.save(pedido);
        }
        if (cambioEstado) {
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.PEDIDO,
                    "Se ha actualizado un pedido de estado "+estadoActual+ " a "+estadoNuevo,
                    pedido.getId(),
                    authorizationService.obtenerUsuarioAutenticado()
            );
        }
        if (detectarCambio) {
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.PEDIDO,
                    "Se ha actualizado un pedido",
                    pedido.getId(),
                    authorizationService.obtenerUsuarioAutenticado()
            );
        }
        return pedido;
    }

    //eliminar pedido
    @Transactional
    public Pedido eliminarPedido(Long idPedido){
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RecursoNoEncontradoException("El pedido no existe"));
        pedidoRepository.delete(pedido);
        //auditoria
        auditoriaService.registrarAuditoria(
                TipoAccion.ELIMINAR,
                EntidadAuditoria.PEDIDO,
                "Se ha eliminado un pedido.",
                pedido.getId(),
                authorizationService.obtenerUsuarioAutenticado()
        );
        return pedido;
    }

    //crear detalle de pedido
    public DetallePedido crearDetallePedido(CrearDetallePedidoDto crearDetallePedidoDto){

        Pedido pedido = pedidoRepository.findById(crearDetallePedidoDto.getIdPedido())
                .orElseThrow(() -> new RecursoNoEncontradoException("El pedido no existe"));
        Producto producto = productoRepository.findById(crearDetallePedidoDto.getIdProducto())
                .orElseThrow(() -> new RecursoNoEncontradoException("El producto no existe"));
        if (crearDetallePedidoDto.getCantidadSolicitada() <= 0){
            throw new IllegalArgumentException("La cantidad de solicitada debe ser mayor que 0");
        }
        if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO || pedido.getEstadoPedido() == EstadoPedido.RECIBIDO){
            throw new TransicionEstadoInvalidaException("no se puede añadir un detalle a un pedido cancelado o recibido");
        }
        if (detallePedidoRepository.existsByPedidoIdAndProductoId(pedido.getId(), producto.getId())){
            throw new TransicionEstadoInvalidaException("Ya existe un producto con el detalle de solicitado");
        }

        DetallePedido detallePedido = new DetallePedido(crearDetallePedidoDto.getCantidadSolicitada(),pedido,producto);
        return detallePedidoRepository.save(detallePedido);
    }

    //actualizar detalle pedido
    @Transactional
    public DetallePedido actualizarDetallePedido(Long idDetallePedido ,ActualizarDetallePedidoDto actualizarDetallePedidoDto){
        DetallePedido detallePedido = detallePedidoRepository.findById(idDetallePedido)
                .orElseThrow(() -> new RecursoNoEncontradoException("El detalle de pedido no existe"));

        boolean cambioCantidad = false;
        Pedido pedido = detallePedido.getPedido();

        if (pedido.getEstadoPedido() == EstadoPedido.RECIBIDO || pedido.getEstadoPedido() == EstadoPedido.CANCELADO){
            throw new TransicionEstadoInvalidaException(
                    "No se puede modificar un detalle de un pedido recibido o cancelado"
            );
        }

        Integer cantidadNueva = actualizarDetallePedidoDto.getCantidadSolicitada();
        Integer cantidadActual = detallePedido.getCantidadSolicitada();
        if (cantidadNueva != null && !cantidadNueva.equals(cantidadActual)){
            if (cantidadNueva <= 0){
                throw new IllegalArgumentException("La cantidad debe ser mayor que 0");
            }
            detallePedido.setCantidadSolicitada(cantidadNueva);
            cambioCantidad = true;
        }

        if (cambioCantidad){
            detallePedido = detallePedidoRepository.save(detallePedido);
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.PEDIDO,
                    "Se ha actualizado la cantidad del pedido "+detallePedido.getPedido().getId(),
                    pedido.getId(),
                    authorizationService.obtenerUsuarioAutenticado()
            );
        }else {
            throw new CambioNoDetectadoException("No se detecto ningun cambio");
        }

        return detallePedido;
    }

    @Transactional
    public DetallePedido eliminarDetallePedido(Long idDetallePedido){
        DetallePedido detallePedido = detallePedidoRepository.findById(idDetallePedido)
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("El detalle del pedido no existe")
                );
        Pedido pedido = detallePedido.getPedido();
        if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO || pedido.getEstadoPedido() == EstadoPedido.RECIBIDO || pedido.getEstadoPedido() == EstadoPedido.PARCIAL){
            throw new TransicionEstadoInvalidaException(
                    "No se puede eliminar un detalle de un pedido cancelado, recibido o parcial"
            );
        }
        detallePedidoRepository.delete(detallePedido);
        auditoriaService.registrarAuditoria(
                TipoAccion.ELIMINAR,
                EntidadAuditoria.PEDIDO,
                "Se ha eliminado el detalle de un pedido pedido "+detallePedido.getPedido().getId(),
                pedido.getId(),
                pedido.getUsuario()
        );
        return detallePedido;
    }
}