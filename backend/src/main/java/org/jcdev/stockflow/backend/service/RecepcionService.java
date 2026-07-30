package org.jcdev.stockflow.backend.service;

import org.jcdev.stockflow.backend.dto.ActualizarRecepcionDto;
import org.jcdev.stockflow.backend.dto.CrearRecepcionDto;
import org.jcdev.stockflow.backend.entity.*;
import org.jcdev.stockflow.backend.enums.EstadoPedido;
import org.jcdev.stockflow.backend.enums.EstadoRecepcion;
import org.jcdev.stockflow.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecepcionService {

    RecepcionRepository recepcionRepository;
    DetalleRecepcionRepository detalleRecepcionRepository;
    PedidoRepository pedidoRepository;
    EmpresaRepository empresaRepository;
    UsuarioRepository usuarioRepository;

    public RecepcionService(RecepcionRepository recepcionRepository,  DetalleRecepcionRepository detalleRecepcionRepository,
                            PedidoRepository pedidoRepository, EmpresaRepository empresaRepository,
                            UsuarioRepository usuarioRepository) {
        this.recepcionRepository = recepcionRepository;
        this.detalleRecepcionRepository = detalleRecepcionRepository;
        this.pedidoRepository = pedidoRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //ver todas las recepciones
    public List<Recepcion> obtenerRecepciones() {
        return recepcionRepository.findAll();
    }

    //crear una recepcion
    public Recepcion crearRecepcion(CrearRecepcionDto crearRecepcionDto) {
        Pedido pedido = pedidoRepository.findById(crearRecepcionDto.getIdPedido())
                .orElseThrow(
                        () -> new IllegalArgumentException("El pedido no existe")
                );
        Empresa empresa = empresaRepository.findById(crearRecepcionDto.getIdEmpresa())
                .orElseThrow(
                        () -> new IllegalArgumentException("El empresa no existe"));
        Usuario usuario = usuarioRepository.findById(crearRecepcionDto.getIdUsuario())
                .orElseThrow(
                        () -> new IllegalArgumentException("El usuario no existe")
                );

        if (!pedido.getEmpresa().getId().equals(empresa.getId())) {
            throw new IllegalArgumentException("La empresa de la recepcion no coincide con la empresa del pedido");
        }
        if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO || pedido.getEstadoPedido() == EstadoPedido.RECIBIDO){
            throw new IllegalArgumentException("No se puede crear una recepcion para un pedido cancelado o recibido");
        }
        Recepcion recepcion = new Recepcion(crearRecepcionDto.getObservaciones(), empresa, usuario, pedido);
        return  recepcionRepository.save(recepcion);
    }

    //actualizar una recepcion
    public Recepcion actualizarRecepcion(Long idRecepcion,ActualizarRecepcionDto actualizarRecepcionDto) {
        Recepcion recepcion = recepcionRepository.findById(idRecepcion)
                .orElseThrow(() -> new IllegalArgumentException("El recepcion no existe"));
        if (actualizarRecepcionDto.getObservaciones() != null && !actualizarRecepcionDto.getObservaciones().isBlank()) {
            recepcion.setObservaciones(actualizarRecepcionDto.getObservaciones());
        }
        EstadoRecepcion actualEstado = recepcion.getEstadoRecepcion();
        EstadoRecepcion nuevoEstado = actualizarRecepcionDto.getEstadoRecepcion();

        if (nuevoEstado != null) {
            if (actualEstado == EstadoRecepcion.cancelado &&
            nuevoEstado != EstadoRecepcion.cancelado) {
                throw new IllegalArgumentException("La recepcion esta cancelado.");
            }
            if (actualEstado == EstadoRecepcion.recibida &&
            nuevoEstado != EstadoRecepcion.recibida) {
                throw new IllegalArgumentException("La recepcion ya esta recibido.");
            }
            if (actualEstado == EstadoRecepcion.parcial &&
            nuevoEstado == EstadoRecepcion.pendiente){
                throw new IllegalArgumentException("Pedido no puede regresar a estado pendiente.");
            }
            recepcion.setEstadoRecepcion(nuevoEstado);
        }
        return recepcionRepository.save(recepcion);
    }

    //eliminar recepcion
    public Recepcion eliminarRecepcion(Long idRecepcion) {
        Recepcion recepcion = recepcionRepository.findById(idRecepcion)
                .orElseThrow(() -> new IllegalArgumentException("El recepcion no existe"));
        recepcionRepository.delete(recepcion);
        return recepcion;
    }

    //buscar el detalle de las recepciones por identificador
    public List<DetalleRecepcion> obtenerDetalleRecepciones(Long idRecepcion) {
        recepcionRepository.findById(idRecepcion)
                .orElseThrow(() ->
                        new IllegalArgumentException("El recepcion no existe"));

        return detalleRecepcionRepository.findByRecepcionId(idRecepcion);
    }

    public List<DetalleRecepcion> obtenerDetallePorProductos(Long idProducto) {
        return detalleRecepcionRepository.findByProductoId(idProducto);
    }
}
