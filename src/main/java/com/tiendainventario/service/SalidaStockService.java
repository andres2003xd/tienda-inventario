package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.exception.GlobalExceptionHandler.StockInsuficienteException;
import com.tiendainventario.model.SalidaStock;
import com.tiendainventario.model.Producto;
import com.tiendainventario.model.Empleado;
import com.tiendainventario.model.Stock;
import com.tiendainventario.repository.SalidaStockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalidaStockService {

    private final SalidaStockRepository repository;
    private final ProductoService productoService;
    private final EmpleadoService empleadoService;
    private final StockService stockService;

    public SalidaStockService(SalidaStockRepository repository,
                              ProductoService productoService,
                              EmpleadoService empleadoService,
                              StockService stockService) {
        this.repository = repository;
        this.productoService = productoService;
        this.empleadoService = empleadoService;
        this.stockService = stockService;
    }

    @Transactional
    public SalidaStock registrarSalida(SalidaStock salida) {
        // Validar y obtener entidades relacionadas
        Producto producto = productoService.findById(salida.getProducto().getId());
        Empleado empleado = empleadoService.findById(salida.getEmpleado().getId());

        // Validar cantidad positiva
        if (salida.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        // Verificar stock disponible
        Stock stock = stockService.obtenerStockPorProducto(producto.getId());
        if (stock.getCantidad() < salida.getCantidad()) {
            throw new StockInsuficienteException(
                    String.format("Stock insuficiente para el producto %s. Disponible: %d, Solicitado: %d",
                            producto.getNombre(), stock.getCantidad(), salida.getCantidad())
            );
        }

        // Resto del método permanece igual
        salida.setProducto(producto);
        salida.setEmpleado(empleado);

        SalidaStock salidaGuardada = repository.save(salida);
        stockService.actualizarStockPorSalida(producto.getId(), salida.getCantidad());

        return salidaGuardada;
    }

    public List<SalidaStock> obtenerTodas() {
        return repository.findAll();
    }

    public SalidaStock obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SalidaStock", id));
    }

    public List<SalidaStock> obtenerPorProducto(Long productoId) {
        return repository.findByProductoId(productoId);
    }

    public List<SalidaStock> obtenerPorEmpleado(Long empleadoId) {
        return repository.findByEmpleadoId(empleadoId);
    }

    public List<SalidaStock> obtenerPorVenta(Long idVenta) {
        return repository.findByIdVenta(idVenta);
    }

    public List<SalidaStock> obtenerPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return repository.findByFechaBetween(fechaInicio, fechaFin);
    }
}