package com.tiendainventario.service;

import com.tiendainventario.model.Venta;
import com.tiendainventario.repository.VentaRepository;
import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.exception.ResourceAlreadyExistsException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService extends BaseService<Venta, Long, VentaRepository> {

    public VentaService(VentaRepository repository) {
        super(repository);
    }

    @Transactional(readOnly = true)
    public List<Venta> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "fecha"));
    }

    @Transactional(readOnly = true)
    public Venta findById(Long id) {
        return repository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));
    }

    @Transactional
    public Venta crearVenta(Venta venta) {
        // Validación modificada (acepta cliente con ID o objeto completo)
        if (venta.getCliente() == null || venta.getCliente().getId() == null) {
            throw new IllegalArgumentException("Se requiere un cliente válido (ID o objeto completo)");
        }

        // Validar campos obligatorios
        if (venta.getCliente() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio para crear una venta");
        }

        // Establecer fecha actual si no viene especificada
        if (venta.getFecha() == null) {
            venta.setFecha(LocalDateTime.now());
        }

        return repository.save(venta);
    }

    @Transactional
    public Venta actualizarVenta(Long id, Venta ventaActualizada) {
        // Verificar existencia de la venta
        Venta venta = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "id", id));

        // Validar que el ID de la venta actualizada coincida
        if (ventaActualizada.getId() != null && !ventaActualizada.getId().equals(id)) {
            throw new IllegalArgumentException("El ID de la venta no coincide con el ID de la ruta");
        }

        // Actualizar campos
        if (ventaActualizada.getFecha() != null) {
            venta.setFecha(ventaActualizada.getFecha());
        }

        if (ventaActualizada.getTotal() != null) {
            venta.setTotal(ventaActualizada.getTotal());
        }

        if (ventaActualizada.getCliente() != null) {
            venta.setCliente(ventaActualizada.getCliente());
        }

        if (ventaActualizada.getMetodoPago() != null) {
            venta.setMetodoPago(ventaActualizada.getMetodoPago());
        }

        if (ventaActualizada.getEmpleado() != null) {
            venta.setEmpleado(ventaActualizada.getEmpleado());
        }

        return repository.save(venta);
    }

    @Override
    protected String getEntityName() {
        return "Venta";
    }
}