package com.dev.mxd.mediconnect.exception;

public class DoctorNotFoundException extends RuntimeException {

    public DoctorNotFoundException(Long id) {
        super("No se encontró el doctor con id: " + id);
    }
}
