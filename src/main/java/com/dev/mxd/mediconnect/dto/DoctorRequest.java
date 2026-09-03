package com.dev.mxd.mediconnect.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DoctorRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,
        @NotBlank(message = "El apellido es obligatorio")
        String apellido,
        @NotBlank(message = "La especialidad es obligatoria")
        String especialidad,
        @NotBlank(message = "La tarjeta profesional es obligatoria")
        String tarjetaProfesional,
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato válido")
        String correo,
        @NotBlank(message = "El teléfono es obligatorio")
        String telefono,
        @NotNull(message = "La disponibilidad es obligatoria")
        Boolean disponible
) {
}
