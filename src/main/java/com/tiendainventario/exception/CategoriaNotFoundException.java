   package com.tiendainventario.exception;

   public class CategoriaNotFoundException extends RuntimeException {
       public CategoriaNotFoundException(String mensaje) {
           super(mensaje);
       }
   }