package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.creardto.AjusteInventarioDto;
import org.jcdev.stockflow.backend.entity.MovimientoInventario;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.enums.movimiento.TipoMovimiento;
import org.jcdev.stockflow.backend.exception.RecursoNoEncontradoException;
import org.jcdev.stockflow.backend.repository.MovimientoInventarioRepository;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.jcdev.stockflow.backend.repository.UsuarioRepository;
import org.jcdev.stockflow.backend.service.security.AuthorizationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    //autorizaciones
    private final AuthorizationService  authorizationService;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoInventarioRepository, ProductoRepository productoRepository, UsuarioRepository usuarioRepository, AuthorizationService authorizationService) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.authorizationService = authorizationService;
    }

    public List<MovimientoInventario> obtenerMovimientoInventarios() {
        return movimientoInventarioRepository.findAll();
    }

    //movimiento de pedidos
    public List<MovimientoInventario> obtenerMovimientoInventariosByPedidoId(Long idPedido) {
        return movimientoInventarioRepository.findByPedidoId(idPedido);
    }

    //movimientos por usuario
    public List<MovimientoInventario> obtenerMovimientoInventariosByUsuarioId(Long idUsuario) {
        return movimientoInventarioRepository.findByUsuarioId(idUsuario);
    }

    //movimientos por producto
    public List<MovimientoInventario> obtenerMovimientoInventariosByProductoId(Long idProducto) {
        return movimientoInventarioRepository.findByProductoId(idProducto);
    }

    //movimientos por recepcion
    public List<MovimientoInventario> obtenerMovimientoInventariosByRecepcionId(Long idRecepcion) {
        return movimientoInventarioRepository.findByRecepcionId(idRecepcion);
    }

    //crear un ajuste de inventario
    @Transactional
    public MovimientoInventario ajusteMovimiento(AjusteInventarioDto ajusteInventarioDto) {

        //validamos que el producto exista
        Producto producto = productoRepository.findById(ajusteInventarioDto.getIdProducto())
                .orElseThrow(
                        ()-> new RecursoNoEncontradoException("El producto no existe")
                );

        //usuario autenticado
        Usuario usuario = authorizationService.obtenerUsuarioAutenticado();


        Integer stockSistema = producto.getStock();
        Integer stockReal = ajusteInventarioDto.getCantidad();
        int diferenciaStock = stockReal - stockSistema;

        if (stockReal < 0){
            throw new IllegalArgumentException("El stock no puede ser menor que 0");
        }

        MovimientoInventario movimientoAjuste = new MovimientoInventario();
        if (diferenciaStock>0){
            movimientoAjuste.setTipoMovimiento(TipoMovimiento.AJUSTE_POSITIVO);
            producto.setStock(stockReal);
            productoRepository.save(producto);
        }
        if(diferenciaStock < 0){
            movimientoAjuste.setTipoMovimiento(TipoMovimiento.AJUSTE_NEGATIVO);
            if (stockReal > stockSistema){
                throw new IllegalArgumentException("El ajuste no puede dejar el stock en negativo");
            }
            producto.setStock(stockReal);
            productoRepository.save(producto);
        }
        if (diferenciaStock == 0){
            throw new IllegalStateException("El stock real coincide con el stock del sistema");
        }
        movimientoAjuste.setFechaMovimiento(LocalDate.now());
        movimientoAjuste.setCantidad(Math.abs(diferenciaStock));
        movimientoAjuste.setProducto(producto);
        movimientoAjuste.setUsuario(usuario);
        movimientoAjuste.setDescripcion(ajusteInventarioDto.getDescripcion());

        return movimientoInventarioRepository.save(movimientoAjuste);
    }
}
