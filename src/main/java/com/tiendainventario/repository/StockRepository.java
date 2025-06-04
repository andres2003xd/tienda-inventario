package com.tiendainventario.repository;

import com.tiendainventario.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    boolean existsByProductoId(Long productoId);
    Optional<Stock> findByProductoId(Long productoId);
}
