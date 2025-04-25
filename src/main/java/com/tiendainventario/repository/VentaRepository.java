package com.tiendainventario.repository;
import com.tiendainventario.model.DetalleVenta;
import com.tiendainventario.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    boolean existsByClienteId(Long clienteId);
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DetalleVenta d WHERE d.venta.id = :ventaId")
    boolean existsDetallesByVentaId(@Param("ventaId") Long ventaId);
    List<Venta> findByClienteId(Long clienteId);
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.venta.id = :ventaId")
    List<DetalleVenta> findDetallesByVentaId(@Param("ventaId") Long ventaId);
}