package com.tiendainventario.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s ya existe con %s: %s", resourceName, fieldName, fieldValue));
    }
}