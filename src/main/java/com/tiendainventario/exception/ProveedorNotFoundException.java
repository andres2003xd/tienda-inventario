package com.tiendainventario.exception;

public class ProveedorNotFoundException extends RuntimeException {
    public ProveedorNotFoundException(String mensaje) {
        super(mensaje);
    }
}