package com.tiendainventario.repository;

import com.tiendainventario.model.CargoEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoEmpleadoRepository extends JpaRepository<CargoEmpleado, Long> {
    boolean existsByNombre(String nombre);
    CargoEmpleado findByNombre(String nombre);
}