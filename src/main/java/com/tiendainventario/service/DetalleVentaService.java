package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.model.DetalleVenta;
import com.tiendainventario.model.Producto;
import com.tiendainventario.repository.DetalleVentaRepository;
import com.tiendainventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

@Service
public class DetalleVentaService extends BaseService<DetalleVenta, Long, DetalleVentaRepository> {

    private final ProductoRepository productoRepository;

    public DetalleVentaService(DetalleVentaRepository repository, ProductoRepository productoRepository) {
        super(repository);
        this.productoRepository = productoRepository;
    }

    public DetalleVenta crearDetalleVenta(DetalleVenta detalleVenta) {
        Long productoId = detalleVenta.getProducto().getId();
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "ID", productoId));

        if (producto.getProveedor() == null) {
            throw new IllegalArgumentException("El producto debe tener un proveedor asociado.");
        }

        detalleVenta.setProducto(producto);
        return repository.save(detalleVenta);
    }

    public DetalleVenta actualizarDetalleVenta(Long id, DetalleVenta detalleActualizado) {
        DetalleVenta detalleExistente = findById(id);
        detalleExistente.setCantidad(detalleActualizado.getCantidad());
        detalleExistente.setPrecioUnitario(detalleActualizado.getPrecioUnitario());
        detalleExistente.setSubtotal(detalleActualizado.getSubtotal());
        detalleExistente.setProducto(detalleActualizado.getProducto());
        detalleExistente.setVenta(detalleActualizado.getVenta());

        return repository.save(detalleExistente);
    }

    @Override
    protected String getEntityName() {
        return "DetalleVenta";
    }
}