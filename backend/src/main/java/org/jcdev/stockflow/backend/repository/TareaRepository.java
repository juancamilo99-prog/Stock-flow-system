package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Tarea;
import org.jcdev.stockflow.backend.enums.tarea.EstadoTarea;
import org.jcdev.stockflow.backend.enums.tarea.PrioridadTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository  extends JpaRepository<Tarea, Long> {

    List<Tarea> findByUsuarioId(Long idUsuario);
    List<Tarea> findByPedidoId(Long idPedido);
    List<Tarea> findByRecepcionId(Long idRecepcion);
    List<Tarea> findByEstadoTarea(EstadoTarea estadoTarea);
    List<Tarea> findByPrioridadTarea(PrioridadTarea prioridadTarea);
}
