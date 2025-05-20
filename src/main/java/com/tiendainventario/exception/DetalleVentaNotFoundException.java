package com.tiendainventario.exception;

public class DetalleVentaNotFoundException extends RuntimeException {
    public DetalleVentaNotFoundException(String mensaje) {
        super(mensaje);
    }
}