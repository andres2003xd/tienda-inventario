package com.tiendainventario.service;

import com.tiendainventario.exception.GlobalExceptionHandler;
import com.tiendainventario.exception.ResourceAlreadyExistsException;
import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.model.Stock;
import com.tiendainventario.model.Producto;
import com.tiendainventario.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService extends BaseService<Stock, Long, StockRepository> {

    private final ProductoService productoService;

    public StockService(StockRepository repository, ProductoService productoService) {
        super(repository);
        this.productoService = productoService;
    }

    @Transactional
    public Stock crearStock(Stock stock) {
        // Verificar si ya existe stock para este producto
        if (repository.existsByProductoId(stock.getProducto().getId())) {
            throw new ResourceAlreadyExistsException("Stock", "producto", stock.getProducto().getId());
        }

        // Verificar que el producto existe
        Producto producto = productoService.findById(stock.getProducto().getId());
        stock.setProducto(producto);

        return repository.save(stock);
    }

    @Transactional
    public Stock actualizarStock(Long id, Stock stockActualizado) {
        Stock stock = findById(id);

        // Actualizar campos
        stock.setCantidad(stockActualizado.getCantidad());
        stock.setUbicacion(stockActualizado.getUbicacion());
        stock.setStockMinimo(stockActualizado.getStockMinimo());
        stock.setStockMaximo(stockActualizado.getStockMaximo());

        return repository.save(stock);
    }


    // Agrega este método a tu StockService existente
    @Transactional
    public void actualizarStockPorEntrada(Long productoId, Integer cantidad) {
        Stock stock = repository.findByProductoId(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "producto", productoId));

        stock.setCantidad(stock.getCantidad() + cantidad);
        repository.save(stock);
    }

    @Transactional
    public void actualizarStockPorSalida(Long productoId, Integer cantidad) {
        Stock stock = repository.findByProductoId(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "producto", productoId));

        if (stock.getCantidad() < cantidad) {
            throw new GlobalExceptionHandler.StockInsuficienteException(
                    "No se puede actualizar el stock: cantidad insuficiente");
        }

        stock.setCantidad(stock.getCantidad() - cantidad);
        repository.save(stock);
    }

    public Stock obtenerStockPorProducto(Long productoId) {
        return repository.findByProductoId(productoId)
                .orElseThrow(() -> new ResourceNotFoundException("Stock", "producto", productoId));
    }

    @Override
    protected String getEntityName() {
        return "Stock";
    }
}