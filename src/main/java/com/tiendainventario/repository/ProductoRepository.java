package com.tiendainventario.repository;

import com.tiendainventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByProveedorId(Long proveedorId);
    boolean existsByCategoriaId(Long categoriaId);
}