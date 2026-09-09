package com.dev.mxd.mediconnect.exception;

public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException(Long id) {
        super("No se encontró la cita con id: " + id);
    }
}
