package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarDetalleRecepcionDto;
import org.jcdev.stockflow.backend.dto.actualizardto.ActualizarRecepcionDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearDetalleRecepcionDto;
import org.jcdev.stockflow.backend.dto.creardto.CrearRecepcionDto;
import org.jcdev.stockflow.backend.entity.*;
import org.jcdev.stockflow.backend.enums.auditoria.EntidadAuditoria;
import org.jcdev.stockflow.backend.enums.auditoria.TipoAccion;
import org.jcdev.stockflow.backend.enums.pedido.EstadoPedido;
import org.jcdev.stockflow.backend.enums.recepcion.EstadoRecepcion;
import org.jcdev.stockflow.backend.enums.movimiento.TipoMovimiento;
import org.jcdev.stockflow.backend.exception.CambioNoDetectadoException;
import org.jcdev.stockflow.backend.exception.RecursoNoEncontradoException;
import org.jcdev.stockflow.backend.exception.TransicionEstadoInvalidaException;
import org.jcdev.stockflow.backend.repository.*;
import org.jcdev.stockflow.backend.service.security.AuthorizationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecepcionService {

    //repositorys
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
    AuditoriaService auditoriaService;
    //autorizaciones
    AuthorizationService authorizationService;


    public RecepcionService(RecepcionRepository recepcionRepository,  DetalleRecepcionRepository detalleRecepcionRepository,
                            PedidoRepository pedidoRepository, EmpresaRepository empresaRepository,
                            UsuarioRepository usuarioRepository, ProductoRepository productoRepository,  DetallePedidoRepository detallePedidoRepository,
                            ProductoService productoService, MovimientoInventarioRepository movimientoInventarioRepository, AuditoriaService auditoriaService,
                            AuthorizationService authorizationService) {
        this.recepcionRepository = recepcionRepository;
        this.detalleRecepcionRepository = detalleRecepcionRepository;
        this.pedidoRepository = pedidoRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.productoService = productoService;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.auditoriaService = auditoriaService;
        this.authorizationService = authorizationService;
    }

    //ver todas las recepciones
    public List<Recepcion> obtenerRecepciones() {
        return recepcionRepository.findAll();
    }

    //ver una recepcion por identificador
    public Recepcion obtenerRecepcionPorId(Long idRecepcion) {
        return recepcionRepository.findById(idRecepcion).orElseThrow(
                () -> new RecursoNoEncontradoException("El recepcion no existe")
        );
    }

    //obtener una recepcion por identificador de usuario
    public List<Recepcion> obtenerRecepcionPorUsuario(Long idUsuario) {
        return recepcionRepository.findByUsuarioId(idUsuario);
    }

    //crear una recepcion
    @Transactional
    public Recepcion crearRecepcion(CrearRecepcionDto crearRecepcionDto) {
        Pedido pedido = pedidoRepository.findById(crearRecepcionDto.getIdPedido())
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("El pedido no existe")
                );
        Empresa empresa = empresaRepository.findById(crearRecepcionDto.getIdEmpresa())
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("El empresa no existe"));
        Usuario usuario = usuarioRepository.findById(crearRecepcionDto.getIdUsuario())
                .orElseThrow(
                        () -> new RecursoNoEncontradoException("El usuario no existe")
                );

        if (!authorizationService.esOperario(usuario.getId())){
            throw new IllegalArgumentException("No se puede asignar una recepcion a un usuario con un rol diferente a operario");
        }

        if (!pedido.getEmpresa().getId().equals(empresa.getId())) {
            throw new IllegalArgumentException("La empresa de la recepcion no coincide con la empresa del pedido");
        }
        if (pedido.getEstadoPedido() == EstadoPedido.CANCELADO || pedido.getEstadoPedido() == EstadoPedido.RECIBIDO){
            throw new TransicionEstadoInvalidaException("No se puede crear una recepcion para un pedido cancelado o recibido");
        }
        Recepcion recepcion = new Recepcion(crearRecepcionDto.getObservaciones(), empresa, usuario, pedido);
        recepcion = recepcionRepository.save(recepcion);
        auditoriaService.registrarAuditoria(
                TipoAccion.CREAR,
                EntidadAuditoria.RECEPCION,
                "Se ha creado una recepcion nueva.",
                recepcion.getId(),
                authorizationService.obtenerUsuarioAutenticado()
        );
        return recepcion;
    }

    //actualizar una recepcion
    @Transactional
    public Recepcion actualizarRecepcion(Long idRecepcion, ActualizarRecepcionDto actualizarRecepcionDto) {
        Recepcion recepcion = recepcionRepository.findById(idRecepcion)
                .orElseThrow(() -> new RecursoNoEncontradoException("El recepcion no existe"));
        boolean detectarCambio = false;
        boolean cambioEstado = false;
        if (actualizarRecepcionDto.getObservaciones() != null && !actualizarRecepcionDto.getObservaciones().isBlank()) {
            String descripcionNueva = actualizarRecepcionDto.getObservaciones().trim();
            String descripcionActual = recepcion.getObservaciones().trim();
            if (!descripcionNueva.equals(descripcionActual)) {
                recepcion.setObservaciones(descripcionNueva);
                detectarCambio = true;
            }
        }
        //estados de la recepcion
        EstadoRecepcion actualEstado = recepcion.getEstadoRecepcion();
        EstadoRecepcion nuevoEstado = actualizarRecepcionDto.getEstadoRecepcion();

        if (nuevoEstado != null && nuevoEstado != actualEstado) {
            switch (actualEstado) {
                case cancelado -> throw new TransicionEstadoInvalidaException("Una recepcion cancelada no puede cambiar de estado");
                case recibida ->  throw new TransicionEstadoInvalidaException("Una recepcion recibida no puede cambiar de estado");
                case parcial -> {
                    if (nuevoEstado != EstadoRecepcion.recibida){
                        throw new TransicionEstadoInvalidaException("Una recepcion parcial solo puede cambiar a estado recibido.");
                    }
                }
            }
            recepcion.setEstadoRecepcion(nuevoEstado);
            cambioEstado = true;
        }
        if (cambioEstado || detectarCambio) {
            recepcion = recepcionRepository.save(recepcion);
        }else {
            throw new CambioNoDetectadoException("No se detecto ningún cambio");
        }
        if (cambioEstado){
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.RECEPCION,
                    "Se modificado el estado de la recepcion "+recepcion.getId()+" de "+actualEstado+" a "+nuevoEstado,
                    recepcion.getId(),
                    authorizationService.obtenerUsuarioAutenticado()
            );
        }
        if (detectarCambio) {
            auditoriaService.registrarAuditoria(
                    TipoAccion.ACTUALIZAR,
                    EntidadAuditoria.RECEPCION,
                    "Se modificado la recepcion "+recepcion.getId(),
                    recepcion.getId(),
                    authorizationService.obtenerUsuarioAutenticado()
            );
        }

        return recepcion;
    }

    //eliminar recepcion
    @Transactional
    public void eliminarRecepcion(Long idRecepcion) {
        Recepcion recepcion = recepcionRepository.findById(idRecepcion)
                .orElseThrow(() -> new RecursoNoEncontradoException("El recepcion no existe"));
        recepcionRepository.delete(recepcion);
        auditoriaService.registrarAuditoria(
                TipoAccion.ELIMINAR,
                EntidadAuditoria.RECEPCION,
                "Se ha eliminado una recepcion.",
                recepcion.getId(),
                authorizationService.obtenerUsuarioAutenticado()
        );
    }

    //buscar el detalle de las recepciones por identificador
    public List<DetalleRecepcion> obtenerDetalleRecepciones(Long idRecepcion) {
        recepcionRepository.findById(idRecepcion)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("El recepcion no existe"));

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
                .orElseThrow(() -> new RecursoNoEncontradoException("El recepcion no existe"));

        if (recepcion.getEstadoRecepcion() == EstadoRecepcion.cancelado || recepcion.getEstadoRecepcion() == EstadoRecepcion.recibida) {
            throw new IllegalArgumentException("No se pueden añadir productos a una recepcion cerrada.");
        }

        //nos traemos el id de pedido de la recepcion
        Long idPedido = recepcion.getPedido().getId();
        //nos traemos toda la lista de los detalles
        List<DetallePedido> detallesPedido = detallePedidoRepository.findByPedidoId(idPedido);
        if (detallesPedido.isEmpty()) {
            throw new RecursoNoEncontradoException("El pedido no tiene detalles.");
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
                .orElseThrow(() -> new RecursoNoEncontradoException("El detalle de recepcion no existe"));


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
