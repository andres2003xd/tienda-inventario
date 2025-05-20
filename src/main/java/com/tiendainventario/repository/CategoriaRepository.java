package com.tiendainventario.repository;

import com.tiendainventario.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByNombre(String nombre);
    Optional<Categoria> findByNombre(String nombre);
}