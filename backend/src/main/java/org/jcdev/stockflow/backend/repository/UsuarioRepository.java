package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByActivoFalse(Boolean activo);
    List<Usuario> findByActivoTrue(Boolean activo);
    boolean existsByEmail(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
