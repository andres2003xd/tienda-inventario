package com.tiendainventario.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, Object id) {
        super(String.format("%s no encontrado con ID: %s", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s no encontrado con %s: %s", resourceName, fieldName, fieldValue));
    }
}