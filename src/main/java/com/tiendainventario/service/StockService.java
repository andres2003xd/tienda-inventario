package com.tiendainventario.service;

import com.tiendainventario.exception.ResourceAlreadyExistsException;
import com.tiendainventario.exception.ResourceNotFoundException;
import com.tiendainventario.model.Stock;
import com.tiendainventario.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService extends BaseService<Stock, Long, StockRepository> {

    @Autowired
    public StockService(StockRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "Stock";
    }

    @Transactional
    public Stock create(Stock stock) {
        if (repository.existsByProductoId(stock.getProducto().getId())) {
            throw new ResourceAlreadyExistsException(
                    getEntityName(),
                    "producto",
                    stock.getProducto().getId()
            );
        }
        return repository.save(stock);
    }

    @Transactional
    public Stock update(Long id, Stock stockDetails) {
        Stock stock = findById(id);

        // Verificar si el producto está cambiando y si ya existe stock para ese producto
        if (!stock.getProducto().getId().equals(stockDetails.getProducto().getId()) &&
                repository.existsByProductoId(stockDetails.getProducto().getId())) {
            throw new ResourceAlreadyExistsException(
                    getEntityName(),
                    "producto",
                    stockDetails.getProducto().getId()
            );
        }

        stock.setProducto(stockDetails.getProducto());
        stock.setCantidad(stockDetails.getCantidad());
        stock.setUbicacion(stockDetails.getUbicacion());
        stock.setStockMinimo(stockDetails.getStockMinimo());
        stock.setStockMaximo(stockDetails.getStockMaximo());

        return repository.save(stock);
    }

    public Stock findByProductoId(Long productoId) {
        return repository.findByProductoId(productoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        getEntityName(),
                        "producto ID",
                        productoId
                ));
    }
}