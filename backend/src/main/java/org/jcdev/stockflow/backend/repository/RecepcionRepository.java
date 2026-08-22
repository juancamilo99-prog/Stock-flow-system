package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Recepcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecepcionRepository extends JpaRepository<Recepcion, Long> {

    List<Recepcion> findByUsuarioId(Long idUsuario);

}
