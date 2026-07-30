package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Recepcion;
import org.jcdev.stockflow.backend.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecepcionRepository extends JpaRepository<Recepcion, Long> {

}
