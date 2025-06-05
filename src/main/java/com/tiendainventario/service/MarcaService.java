package com.tiendainventario.service;

import com.tiendainventario.model.Marca;
import com.tiendainventario.repository.MarcaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarcaService extends BaseService<Marca, Long, MarcaRepository> {

    public MarcaService(MarcaRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Marca";
    }

    @Transactional
    public Marca crearMarca(Marca marca) {
        return repository.save(marca);
    }

    @Transactional
    public Marca actualizarMarca(Long id, Marca marcaActualizada) {
        Marca marca = findById(id);
        marca.setNombre(marcaActualizada.getNombre());
        marca.setDescripcion(marcaActualizada.getDescripcion());
        marca.setFechaCreacion(marcaActualizada.getFechaCreacion());
        return repository.save(marca);
    }
}