// EmpleadoRepository.java
package com.tiendainventario.repository;

import com.tiendainventario.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    boolean existsByDocumento(String documento);
    boolean existsByEmail(String email);
}