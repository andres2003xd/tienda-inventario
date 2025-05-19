package com.tiendainventario.exception;

public class ProductoAlreadyExistsException extends RuntimeException {
    public ProductoAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}