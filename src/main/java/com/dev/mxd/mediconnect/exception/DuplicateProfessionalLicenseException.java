package com.dev.mxd.mediconnect.exception;

public class DuplicateProfessionalLicenseException extends RuntimeException {

    public DuplicateProfessionalLicenseException(String tarjetaProfesional) {
        super("La tarjeta profesional ya está registrada: " + tarjetaProfesional);
    }
}
