package com.tiendainventario.service;

import com.tiendainventario.exception.CategoriaAlreadyExistsException;
import com.tiendainventario.exception.CategoriaNotFoundException;
import com.tiendainventario.model.Categoria;
import com.tiendainventario.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException("Categoría no encontrada con ID: " + id));
    }
    public Categoria crearCategoria(Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new CategoriaAlreadyExistsException("La categoría con el nombre '" + categoria.getNombre() + "' ya existe.");
        }
        return categoriaRepository.save(categoria);
    }

    public Categoria actualizarCategoria(Long id, Categoria categoriaActualizada) {
        Categoria categoria = buscarPorId(id);
        categoria.setNombre(categoriaActualizada.getNombre());
        categoria.setDescripcion(categoriaActualizada.getDescripcion());


        if (!categoria.getId().equals(id) && categoriaRepository.existsByNombre(categoriaActualizada.getNombre())) {
            throw new CategoriaAlreadyExistsException("El nombre '" + categoriaActualizada.getNombre() + "' ya está en uso.");
        }

        return categoriaRepository.save(categoria);
    }

    public void eliminarCategoria(Long id) {
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }
}