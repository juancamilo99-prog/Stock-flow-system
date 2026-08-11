package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    boolean existsByNombreIgnoreCase(String nombre);
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailAndIdNot(String email,Long idEmpresa);
    boolean existsByNombreAndIdNot(String nombre,Long idEmpresa);
}
