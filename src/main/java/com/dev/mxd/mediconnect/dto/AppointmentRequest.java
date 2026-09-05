package com.dev.mxd.mediconnect.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AppointmentRequest(
        @NotNull(message = "El doctor es obligatorio")
        Long doctorId,
        @NotBlank(message = "El nombre del paciente es obligatorio")
        String pacienteNombre,
        @NotBlank(message = "El correo del paciente es obligatorio")
        @Email(message = "El correo del paciente no tiene un formato válido")
        String pacienteCorreo,
        @NotBlank(message = "El motivo es obligatorio")
        String motivo,
        @NotNull(message = "La fecha y hora son obligatorias")
        @Future(message = "La fecha y hora deben ser futuras")
        LocalDateTime fechaHora
) {
}
