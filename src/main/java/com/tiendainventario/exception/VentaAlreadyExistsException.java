package com.tiendainventario.exception;

public class VentaAlreadyExistsException extends RuntimeException {
    public VentaAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}