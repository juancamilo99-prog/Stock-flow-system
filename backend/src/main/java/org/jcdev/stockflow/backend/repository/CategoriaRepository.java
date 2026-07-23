package org.jcdev.stockflow.backend.repository;

import org.jcdev.stockflow.backend.entity.Categoria;
import org.jcdev.stockflow.backend.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
