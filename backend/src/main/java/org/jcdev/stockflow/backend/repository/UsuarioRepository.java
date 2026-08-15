package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Usuario;
import org.jcdev.stockflow.backend.enums.usuario.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByActivoFalse(Boolean activo);
    List<Usuario> findByActivoTrue(Boolean activo);

    //usuarios que tengan este email
    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
