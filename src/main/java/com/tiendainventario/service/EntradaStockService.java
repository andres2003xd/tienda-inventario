package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.model.EntradaStock;
import com.tiendainventario.model.Producto;
import com.tiendainventario.model.Proveedor;
import com.tiendainventario.model.Empleado;
import com.tiendainventario.repository.EntradaStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

import java.util.List;

@Service
public class EntradaStockService {

    private final EntradaStockRepository repository;
    private final ProductoService productoService;
    private final ProveedorService proveedorService;
    private final EmpleadoService empleadoService;
    private final StockService stockService;

    public EntradaStockService(EntradaStockRepository repository,
                               ProductoService productoService,
                               ProveedorService proveedorService,
                               EmpleadoService empleadoService,
                               StockService stockService) {
        this.repository = repository;
        this.productoService = productoService;
        this.proveedorService = proveedorService;
        this.empleadoService = empleadoService;
        this.stockService = stockService;
    }

    @Transactional
    public EntradaStock registrarEntrada(EntradaStock entrada) {
        // Validar y obtener entidades relacionadas
        Producto producto = productoService.findById(entrada.getProducto().getId());
        Empleado empleado = empleadoService.findById(entrada.getEmpleado().getId());

        Proveedor proveedor = null;
        if (entrada.getProveedor() != null && entrada.getProveedor().getId() != null) {
            proveedor = proveedorService.findById(entrada.getProveedor().getId());
        }

        // Validar cantidad positiva
        if (entrada.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        // Configurar relaciones
        entrada.setProducto(producto);
        entrada.setEmpleado(empleado);
        entrada.setProveedor(proveedor);

        // Guardar entrada
        EntradaStock entradaGuardada = repository.save(entrada);

        // Actualizar stock
        stockService.actualizarStockPorEntrada(producto.getId(), entrada.getCantidad());

        return entradaGuardada;
    }

    public List<EntradaStock> obtenerTodas() {
        return repository.findAll();
    }

    public EntradaStock obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EntradaStock", id));
    }

    public List<EntradaStock> obtenerPorProducto(Long productoId) {
        return repository.findByProductoId(productoId);
    }

    // Nuevos métodos agregados
    public List<EntradaStock> obtenerPorProveedor(Long proveedorId) {
        return repository.findByProveedorId(proveedorId);
    }

    public List<EntradaStock> obtenerPorEmpleado(Long empleadoId) {
        return repository.findByEmpleadoId(empleadoId);
    }

    public List<EntradaStock> obtenerPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return repository.findByFechaBetween(fechaInicio, fechaFin);
    }
}