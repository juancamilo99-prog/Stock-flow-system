package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email,Long idEmpresa);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre,Long idEmpresa);
}
