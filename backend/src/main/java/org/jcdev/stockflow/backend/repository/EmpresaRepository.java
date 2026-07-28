package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    boolean existsByNombre(String nombre);
    boolean existsByEmail(String email);
}
