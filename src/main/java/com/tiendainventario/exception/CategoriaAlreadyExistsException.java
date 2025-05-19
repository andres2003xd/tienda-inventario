package com.tiendainventario.exception;

public class CategoriaAlreadyExistsException extends RuntimeException {
    public CategoriaAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}
