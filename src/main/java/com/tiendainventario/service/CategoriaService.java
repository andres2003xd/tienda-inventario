package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceAlreadyExistsException;
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
        Categoria categoria = findById(id);


        if (!categoria.getNombre().equals(categoriaDetails.getNombre()) &&
                repository.existsByNombre(categoriaDetails.getNombre())) {
            throw new ResourceAlreadyExistsException(
                    getEntityName(),
                    "nombre",
                    categoriaDetails.getNombre()
            );
        }


        categoria.setNombre(categoriaDetails.getNombre());
        categoria.setDescripcion(categoriaDetails.getDescripcion());

        return repository.save(categoria);
    }

}