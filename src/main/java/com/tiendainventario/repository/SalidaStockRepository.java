package com.tiendainventario.repository;

import com.tiendainventario.model.SalidaStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalidaStockRepository extends JpaRepository<SalidaStock, Long> {
    List<SalidaStock> findByProductoId(Long productoId);
}