package com.tiendainventario.exception;

public class DetalleVentaAlreadyExistsException extends RuntimeException {
    public DetalleVentaAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}