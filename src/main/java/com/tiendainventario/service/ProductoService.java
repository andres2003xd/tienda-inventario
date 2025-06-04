package com.tiendainventario.service;

import com.tiendainventario.model.Producto;
import com.tiendainventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService extends BaseService<Producto, Long, ProductoRepository> {

    public ProductoService(ProductoRepository repository) {
        super(repository);
    }

    @Transactional
    public Producto crearProducto(Producto producto) {
        return repository.save(producto);
    }

    @Transactional
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto producto = findById(id);
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecio(productoActualizado.getPrecio());
        producto.setCategoria(productoActualizado.getCategoria());
        producto.setProveedor(productoActualizado.getProveedor());
        return repository.save(producto);
    }


    @Override
    protected String getEntityName() {
        return "Producto";
    }
}