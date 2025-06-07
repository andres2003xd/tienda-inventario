package com.tiendainventario.repository;

import com.tiendainventario.model.PQRS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PqrsRepository extends JpaRepository<PQRS, Long> {
    List<PQRS> findByClienteId(Long clienteId);
    List<PQRS> findByEstadoId(Long estadoId);
    List<PQRS> findByTipoId(Long tipoId);
}