package com.tiendainventario.service;

import com.tiendainventario.model.MetodoPago;
import com.tiendainventario.repository.MetodoPagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MetodoPagoService extends BaseService<MetodoPago, Long, MetodoPagoRepository> {

    public MetodoPagoService(MetodoPagoRepository repository) {
        super(repository);
    }

    @Transactional
    public MetodoPago crearMetodoPago(MetodoPago metodoPago) {
        return repository.save(metodoPago);
    }

    @Transactional
    public MetodoPago actualizarMetodoPago(Long id, MetodoPago metodoPagoActualizado) {
        MetodoPago metodoExistente = findById(id);
        metodoExistente.setNombre(metodoPagoActualizado.getNombre());
        metodoExistente.setDescripcion(metodoPagoActualizado.getDescripcion());
        return repository.save(metodoExistente);
    }

    @Override
    protected String getEntityName() {
        return "Método de pago";
    }
}