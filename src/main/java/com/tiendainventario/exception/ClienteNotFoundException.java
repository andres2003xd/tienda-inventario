package com.tiendainventario.exception;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(String mensaje) {
        super(mensaje);
    }
}