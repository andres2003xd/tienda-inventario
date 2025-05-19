package com.tiendainventario.exception;

public class ProveedorAlreadyExistsException extends RuntimeException {
    public ProveedorAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}