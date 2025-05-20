package com.tiendainventario.service;

import com.tiendainventario.model.Venta;
import com.tiendainventario.repository.VentaRepository;
import org.springframework.stereotype.Service;

@Service
public class VentaService extends BaseService<Venta, Long, VentaRepository> {

    public VentaService(VentaRepository repository) {
        super(repository);
    }

    public Venta crearVenta(Venta venta) {
        return repository.save(venta);
    }

    public Venta actualizarVenta(Long id, Venta ventaActualizada) {
        Venta venta = findById(id);
        venta.setFecha(ventaActualizada.getFecha());
        venta.setTotal(ventaActualizada.getTotal());
        venta.setCliente(ventaActualizada.getCliente());
        return repository.save(venta);
    }

    @Override
    protected String getEntityName() {
        return "Venta";
    }
}