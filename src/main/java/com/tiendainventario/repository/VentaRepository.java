package com.tiendainventario.repository;

import com.tiendainventario.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.metodoPago LEFT JOIN FETCH v.cliente LEFT JOIN FETCH v.empleado WHERE v.id = :id")
    Optional<Venta> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT v FROM Venta v LEFT JOIN FETCH v.metodoPago LEFT JOIN FETCH v.cliente LEFT JOIN FETCH v.empleado")
    List<Venta> findAllWithRelations();
}