package com.dev.mxd.mediconnect.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppointmentResponse(
        Long id,
        Long doctorId,
        String pacienteNombre,
        String pacienteCorreo,
        String motivo,
        LocalDateTime fechaHora,
        String estado
) {
}
