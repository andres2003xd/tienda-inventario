package com.tiendainventario.repository;

import com.tiendainventario.model.TipoPQRS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoPqrsRepository extends JpaRepository<TipoPQRS, Long> {
    Optional<TipoPQRS> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}