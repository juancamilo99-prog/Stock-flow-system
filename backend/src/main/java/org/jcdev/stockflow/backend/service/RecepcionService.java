package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarDetalleRecepcionDto;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarRecepcionDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearDetalleRecepcionDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearRecepcionDto;
import org.jcdev.stockflow.backend.entity.*;
import org.jcdev.stockflow.backend.enums.pedido.EstadoPedido;
import org.jcdev.stockflow.backend.enums.recepcion.EstadoRecepcion;
import org.jcdev.stockflow.backend.enums.movimiento.TipoMovimiento;
import org.jcdev.stockflow.backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecepcionService {

    RecepcionRepository recepcionRepository;
    DetalleRecepcionRepository detalleRecepcionRepository;
    PedidoRepository pedidoRepository;
    EmpresaRepository empresaRepository;
    UsuarioRepository usuarioRepository;
    ProductoRepository productoRepository;
    DetallePedidoRepository detallePedidoRepository;
    MovimientoInventarioRepository movimientoInventarioRepository;
    //service
    ProductoService productoService;

    public RecepcionService(RecepcionRepository recepcionRepository,  DetalleRecepcionRepository detalleRecepcionRepository,
                            PedidoRepository pedidoRepository, EmpresaRepository empresaRepository,
                            UsuarioRepository usuarioRepository, ProductoRepository productoRepository,  DetallePedidoRepository detallePedidoRepository,
                            ProductoService productoService, MovimientoInventarioRepository movimientoInventarioRepository) {
        this.recepcionRepository = recepcionRepository;
        this.detalleRecepcionRepository = detalleRecepcionRepository;
        this.pedidoRepository = pedidoRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.productoService = productoService;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
    }

    //ver todas las recepciones
    public List<Recepcion> obtenerRecepciones() {
        return recepcionRepository.findAll();
    }

    //ver una recepcion por identificador
    public Recepcion obtenerRecepcionPorId(Long idRecepcion) {
        return recepcionRepository.findById(idRecepcion).orElseThrow(
                () -> new IllegalArgumentException("El recepcion no existe")
        );
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
    public Recepcion actualizarRecepcion(Long idRecepcion, ActualizarRecepcionDto actualizarRecepcionDto) {
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
    public void eliminarRecepcion(Long idRecepcion) {
        Recepcion recepcion = recepcionRepository.findById(idRecepcion)
                .orElseThrow(() -> new IllegalArgumentException("El recepcion no existe"));
        recepcionRepository.delete(recepcion);
    }

    //buscar el detalle de las recepciones por identificador
    public List<DetalleRecepcion> obtenerDetalleRecepciones(Long idRecepcion) {
        recepcionRepository.findById(idRecepcion)
                .orElseThrow(() ->
                        new IllegalArgumentException("El recepcion no existe"));

        return detalleRecepcionRepository.findByRecepcionId(idRecepcion);
    }

    //obtener detalles por producto
    public List<DetalleRecepcion> obtenerDetallePorProductos(Long idProducto) {
        return detalleRecepcionRepository.findByProductoId(idProducto);
    }

    //crear un detalle de recepcion
    @Transactional
    public List<DetalleRecepcion> crearDetalleRecepcion(CrearDetalleRecepcionDto crearDetalleRecepcionDto) {
        //buscamos la recepcion
        Recepcion recepcion = recepcionRepository.findById(crearDetalleRecepcionDto.getIdRecepcion())
                .orElseThrow(() -> new IllegalArgumentException("El recepcion no existe"));

        if (recepcion.getEstadoRecepcion() == EstadoRecepcion.cancelado || recepcion.getEstadoRecepcion() == EstadoRecepcion.recibida) {
            throw new IllegalArgumentException("No se pueden añadir productos a una recepcion cerrada.");
        }

        //nos traemos el id de pedido de la recepcion
        Long idPedido = recepcion.getPedido().getId();
        //nos traemos toda la lista de los detalles
        List<DetallePedido> detallesPedido = detallePedidoRepository.findByPedidoId(idPedido);
        if (detallesPedido.isEmpty()) {
            throw new IllegalArgumentException("El pedido no tiene detalles.");
        }

        //creamos una lista vacia
        List<DetalleRecepcion> detallesRecepcion = new ArrayList<>();

        //recorremos los detalles de los pedidos
        for (DetallePedido detallePedido : detallesPedido) {

            //creamos un detalle de recepcion vacia
            DetalleRecepcion detalleRecepcion = new DetalleRecepcion();

            //guardamos la recepcion
            detalleRecepcion.setRecepcion(recepcion);
            //guardamos el producto del detalle del pedido
            detalleRecepcion.setProducto(detallePedido.getProducto());

            //nos traemos la cantidad solicitada del detalle del pedido
            detalleRecepcion.setCantidadEsperada(detallePedido.getCantidadSolicitada());
            //iniciamos en 0 la cantidad recibida
            detalleRecepcion.setCantidadRecibida(0);
            //guardamos toda la coleccion o lista
            detallesRecepcion.add(detalleRecepcion);
        }
        //rotnarmos la lista al repository
        return detalleRecepcionRepository.saveAll(detallesRecepcion);
    }

    //actualizar un detalle de pedido
    @Transactional
    public DetalleRecepcion actualizarDetalleRecepcion(Long idDetalleRecepcion, ActualizarDetalleRecepcionDto actualizarDetalleRecepcionDto) {

        DetalleRecepcion detalleRecepcion = detalleRecepcionRepository.findById(idDetalleRecepcion)
                .orElseThrow(() -> new IllegalArgumentException("El detalle de recepcion no existe"));


        //guardamos el valor anterior antes de modificar la entidad
        Integer cantidadAnterior = detalleRecepcion.getCantidadRecibida();
        Integer cantidadNueva = actualizarDetalleRecepcionDto.getCantidadRecibida();
        //validamos las cantidades
        validarCantidades(cantidadAnterior,cantidadNueva, detalleRecepcion.getCantidadEsperada());
        //calculamos la diferencia
        int diferencia = cantidadNueva - cantidadAnterior;
        //actualizamos
        detalleRecepcion.setCantidadRecibida(cantidadNueva);

        //me traigo la recepcion
        Recepcion recepcion = detalleRecepcion.getRecepcion();

        if (diferencia > 0 ){
            Producto producto = detalleRecepcion.getProducto();
            producto.setStock(
                    producto.getStock() + diferencia
            );
            productoRepository.save(producto);


            Optional<MovimientoInventario> movimientoExiste = movimientoInventarioRepository.findByRecepcionIdAndProductoIdAndTipoMovimiento(
                    recepcion.getId(),producto.getId(), TipoMovimiento.ENTRADA
            );
            if (movimientoExiste.isPresent()) {
                MovimientoInventario movimientoInventarioExistente = movimientoExiste.get();
                movimientoInventarioExistente.setCantidad(movimientoInventarioExistente.getCantidad() + diferencia);
                movimientoInventarioRepository.save(movimientoInventarioExistente);
            } else {
                MovimientoInventario movimientoInventarioNuevo = new MovimientoInventario();
                movimientoInventarioNuevo.setTipoMovimiento(TipoMovimiento.ENTRADA);
                movimientoInventarioNuevo.setCantidad(diferencia);
                movimientoInventarioNuevo.setProducto(detalleRecepcion.getProducto());
                movimientoInventarioNuevo.setRecepcion(detalleRecepcion.getRecepcion());
                movimientoInventarioNuevo.setUsuario(recepcion.getUsuario());
                movimientoInventarioNuevo.setDescripcion(recepcion.getObservaciones());
                movimientoInventarioNuevo.setFechaMovimiento(LocalDate.now());
                movimientoInventarioRepository.save(movimientoInventarioNuevo);
            }
        }

        detalleRecepcionRepository.save(detalleRecepcion);
        //obtener todos los detalles asociados a la recepcion
        List<DetalleRecepcion>detallesRecepcion = detalleRecepcionRepository.findByRecepcionId(recepcion.getId());
        boolean todosCompletos = true;
        boolean existeCantidadRecibida = false;
        //recorremos los detalles de las recepciones
        for (DetalleRecepcion detalle : detallesRecepcion) {
            if (detalle.getCantidadRecibida() > 0){
                existeCantidadRecibida = true;
            }
            if (!detalle.getCantidadRecibida().equals(detalle.getCantidadEsperada())){
                todosCompletos = false;
            }
        }

        if (todosCompletos) {
            recepcion.setEstadoRecepcion(EstadoRecepcion.recibida);
        }else if (existeCantidadRecibida) {
            recepcion.setEstadoRecepcion(EstadoRecepcion.parcial);
        }else {
            recepcion.setEstadoRecepcion(EstadoRecepcion.pendiente);
        }

        recepcionRepository.save(recepcion);
        return  detalleRecepcion;
    }

    private void validarCantidades(Integer cantidadAnterior, Integer cantidadNueva, Integer cantidadEsperada) {
        if (cantidadNueva > cantidadEsperada) {
            throw new IllegalArgumentException(
                    "La cantidad recibida no puede superar la cantidad esperada"
            );
        }
        if (cantidadNueva < cantidadAnterior) {
            throw new IllegalArgumentException(
                    "La cantidad recibida no puede reducirse"
            );
        }
    }
}
