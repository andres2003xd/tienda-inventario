package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceAlreadyExistsException;
import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.model.Categoria;
import com.tiendainventario.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService extends BaseService<Categoria, Long, CategoriaRepository> {

    @Autowired
    public CategoriaService(CategoriaRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Categoría";
    }

    @Transactional
    public Categoria create(Categoria categoria) {
        // Validar unicidad del nombre
        if (repository.existsByNombre(categoria.getNombre())) {
            throw new ResourceAlreadyExistsException(
                    getEntityName(),
                    "nombre",
                    categoria.getNombre()
            );
        }
        return repository.save(categoria);
    }

    @Transactional
    public Categoria update(Long id, Categoria categoriaDetails) {
        Categoria categoria = findById(id); // Usa el método heredado

        // Validar cambio de nombre
        if (!categoria.getNombre().equals(categoriaDetails.getNombre()) &&
                repository.existsByNombre(categoriaDetails.getNombre())) {
            throw new ResourceAlreadyExistsException(
                    getEntityName(),
                    "nombre",
                    categoriaDetails.getNombre()
            );
        }

        // Actualizar campos
        categoria.setNombre(categoriaDetails.getNombre());
        categoria.setDescripcion(categoriaDetails.getDescripcion());

        return repository.save(categoria);
    }
}