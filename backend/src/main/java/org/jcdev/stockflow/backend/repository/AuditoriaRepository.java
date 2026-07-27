package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {

    List<Auditoria> findByUsuarioId(Long idUsuario);
}
