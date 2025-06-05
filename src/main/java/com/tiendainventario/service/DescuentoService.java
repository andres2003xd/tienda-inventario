package com.tiendainventario.service;

import com.tiendainventario.model.Descuento;
import com.tiendainventario.repository.DescuentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DescuentoService extends BaseService<Descuento, Long, DescuentoRepository> {

    public DescuentoService(DescuentoRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Descuento";
    }

    @Transactional
    public Descuento crearDescuento(Descuento descuento) {
        return repository.save(descuento);
    }

    @Transactional
    public Descuento actualizarDescuento(Long id, Descuento descuentoActualizado) {
        Descuento descuento = findById(id);
        descuento.setNombre(descuentoActualizado.getNombre());
        descuento.setPorcentaje(descuentoActualizado.getPorcentaje());
        descuento.setFechaInicio(descuentoActualizado.getFechaInicio());
        descuento.setFechaFin(descuentoActualizado.getFechaFin());
        descuento.setActivo(descuentoActualizado.getActivo());
        return repository.save(descuento);
    }
}