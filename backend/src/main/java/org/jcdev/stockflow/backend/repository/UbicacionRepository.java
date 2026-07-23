package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
}
