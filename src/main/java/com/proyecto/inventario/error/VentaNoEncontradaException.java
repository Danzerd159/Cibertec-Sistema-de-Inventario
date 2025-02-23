package com.proyecto.inventario.error;

public class VentaNoEncontradaException extends RuntimeException {
    public VentaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
