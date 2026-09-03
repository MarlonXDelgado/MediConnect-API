package com.dev.mxd.mediconnect.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DoctorResponse(
        Long id,
        String nombre,
        String apellido,
        String especialidad,
        String tarjetaProfesional,
        String correo,
        String telefono,
        Boolean disponible
) {
}
