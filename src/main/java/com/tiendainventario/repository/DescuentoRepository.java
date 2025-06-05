package com.tiendainventario.repository;

import com.tiendainventario.model.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescuentoRepository extends JpaRepository<Descuento, Long> {}