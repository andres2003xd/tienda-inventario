package com.tiendainventario.repository;

import com.tiendainventario.model.HistorialLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialLoginRepository extends JpaRepository<HistorialLogin, Long> {
}