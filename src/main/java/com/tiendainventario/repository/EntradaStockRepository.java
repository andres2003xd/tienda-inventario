package com.tiendainventario.repository;

import com.tiendainventario.model.EntradaStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntradaStockRepository extends JpaRepository<EntradaStock, Long> {
    List<EntradaStock> findByProductoId(Long productoId);
    List<EntradaStock> findByProveedorId(Long proveedorId);
    List<EntradaStock> findByEmpleadoId(Long empleadoId);
    List<EntradaStock> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);
}