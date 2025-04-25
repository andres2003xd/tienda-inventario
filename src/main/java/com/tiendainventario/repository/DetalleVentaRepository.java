package com.tiendainventario.repository;

import com.tiendainventario.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    List<DetalleVenta> findByVentaId(Long ventaId);
    boolean existsByVentaId(Long ventaId);
    boolean existsByProductoId(Long productoId);
}