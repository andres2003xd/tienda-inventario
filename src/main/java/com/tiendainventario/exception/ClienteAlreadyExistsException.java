package com.tiendainventario.exception;

public class ClienteAlreadyExistsException extends RuntimeException {
    public ClienteAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}