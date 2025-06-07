package com.tiendainventario.repository;

import com.tiendainventario.model.EstadoPQRS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstadoPqrsRepository extends JpaRepository<EstadoPQRS, Long> {
    Optional<EstadoPQRS> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}