package org.jcdev.stockflow.backend.service;

import jakarta.transaction.Transactional;
import org.jcdev.stockflow.backend.dto.AjusteInventarioDto;
import org.jcdev.stockflow.backend.entity.MovimientoInventario;
import org.jcdev.stockflow.backend.entity.Producto;
import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.enums.TipoMovimiento;
import org.jcdev.stockflow.backend.repository.MovimientoInventarioRepository;
import org.jcdev.stockflow.backend.repository.ProductoRepository;
import org.jcdev.stockflow.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoInventarioRepository, ProductoRepository productoRepository, UsuarioRepository usuarioRepository) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
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
        Producto producto = productoRepository.findById(ajusteInventarioDto.getIdProducto())
                .orElseThrow(
                        ()-> new IllegalArgumentException("El producto no existe")
                );
        Usuario usuario = usuarioRepository.findById(ajusteInventarioDto.getIdUsuario())
                .orElseThrow(
                        ()-> new IllegalArgumentException("El usuario no existe"));

        if (ajusteInventarioDto.getTipoMovimiento() != TipoMovimiento.AJUSTE_POSITIVO
        && ajusteInventarioDto.getTipoMovimiento() != TipoMovimiento.AJUSTE_NEGATIVO) {
            throw new IllegalArgumentException("El ajuste debe ser positivo o negativo");
        }

        if (ajusteInventarioDto.getTipoMovimiento() == TipoMovimiento.AJUSTE_POSITIVO) {
            producto.setStock(producto.getStock() + ajusteInventarioDto.getCantidad());
            productoRepository.save(producto);
        }
        if (ajusteInventarioDto.getTipoMovimiento() == TipoMovimiento.AJUSTE_NEGATIVO){
            if (ajusteInventarioDto.getCantidad() > producto.getStock()) {
                throw new IllegalArgumentException("El ajuste no puede dejar el stock en negativo");
            }
            producto.setStock(producto.getStock() - ajusteInventarioDto.getCantidad());
            productoRepository.save(producto);
        }

        if (ajusteInventarioDto.getCantidad() < 0){
            throw new IllegalArgumentException("El ajuste no puede ser menor que 0");
        }

        MovimientoInventario movimientoAjuste = new MovimientoInventario();
        movimientoAjuste.setFechaMovimiento(LocalDate.now());
        movimientoAjuste.setCantidad(ajusteInventarioDto.getCantidad());
        movimientoAjuste.setProducto(producto);
        movimientoAjuste.setUsuario(usuario);
        movimientoAjuste.setDescripcion(ajusteInventarioDto.getDescripcion());
        movimientoAjuste.setTipoMovimiento(ajusteInventarioDto.getTipoMovimiento());

        return movimientoInventarioRepository.save(movimientoAjuste);
    }
}
