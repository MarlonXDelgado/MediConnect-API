package com.dev.mxd.mediconnect.controller;

import com.dev.mxd.mediconnect.dto.AppointmentResponse;
import com.dev.mxd.mediconnect.exception.AppointmentNotFoundException;
import com.dev.mxd.mediconnect.exception.GlobalExceptionHandler;
import com.dev.mxd.mediconnect.exception.ScheduleConflictException;
import com.dev.mxd.mediconnect.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
@Import(GlobalExceptionHandler.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Test
    void shouldCreateAppointment() throws Exception {
        when(appointmentService.create(any())).thenReturn(appointmentResponse());

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validAppointmentJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("SCHEDULED"));
    }

    @Test
    void shouldListAppointments() throws Exception {
        when(appointmentService.findAll()).thenReturn(List.of(appointmentResponse()));

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctor_id").value(1));
    }

    @Test
    void shouldFindAppointmentById() throws Exception {
        when(appointmentService.findById(1L)).thenReturn(appointmentResponse());

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldListAppointmentsByDoctor() throws Exception {
        when(appointmentService.findByDoctorId(1L)).thenReturn(List.of(appointmentResponse()));

        mockMvc.perform(get("/api/appointments/doctor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctor_id").value(1));
    }

    @Test
    void shouldCancelAppointment() throws Exception {
        when(appointmentService.cancel(1L)).thenReturn(
                new AppointmentResponse(1L, 1L, "Ana Pérez", "ana@example.com",
                        "Consulta general", LocalDateTime.now().plusDays(1), "CANCELLED"));

        mockMvc.perform(put("/api/appointments/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELLED"));
    }

    @Test
    void shouldRejectInvalidAppointmentRequest() throws Exception {
        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

            @Test
            void shouldReturnNotFoundWhenAppointmentDoesNotExist() throws Exception {
            when(appointmentService.findById(99L)).thenThrow(new AppointmentNotFoundException(99L));

            mockMvc.perform(get("/api/appointments/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Not Found"));
            }

            @Test
            void shouldReturnConflictWhenScheduleIsUnavailable() throws Exception {
            when(appointmentService.create(any()))
                .thenThrow(new ScheduleConflictException(1L, LocalDateTime.of(2030, 1, 15, 10, 0)));

            mockMvc.perform(post("/api/appointments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(validAppointmentJson()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Conflict"));
            }

    private AppointmentResponse appointmentResponse() {
        return new AppointmentResponse(1L, 1L, "Ana Pérez", "ana@example.com",
                "Consulta general", LocalDateTime.now().plusDays(1), "SCHEDULED");
    }

    private String validAppointmentJson() {
        return """
                {
                  "doctor_id": 1,
                  "paciente_nombre": "Ana Pérez",
                  "paciente_correo": "ana@example.com",
                  "motivo": "Consulta general",
                  "fecha_hora": "2030-01-15T10:00:00"
                }
                """;
    }
}
