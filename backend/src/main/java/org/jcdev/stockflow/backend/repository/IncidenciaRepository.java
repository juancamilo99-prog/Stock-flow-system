package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Incidencia;
import org.jcdev.stockflow.backend.enums.incidencia.EstadoIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    List<Incidencia> findByUsuarioId(Long idUsuario);
    List<Incidencia> findByProductoId(Long idProducto);
    List<Incidencia> findByPedidoId(Long idPedido);
    List<Incidencia> findByRecepcionId(Long idRecepcion);
    List<Incidencia> findByEstadoIncidencia(EstadoIncidencia estadoIncidencia);
}
