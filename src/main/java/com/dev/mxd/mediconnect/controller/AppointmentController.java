package com.dev.mxd.mediconnect.controller;

import com.dev.mxd.mediconnect.dto.AppointmentRequest;
import com.dev.mxd.mediconnect.dto.AppointmentResponse;
import com.dev.mxd.mediconnect.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Citas", description = "Gestión de citas médicas")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @Operation(summary = "Programar una cita")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cita programada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Doctor no encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflicto de agenda")
    })
    public ResponseEntity<AppointmentResponse> create(
            @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.create(request));
    }

    @GetMapping
    @Operation(summary = "Listar citas")
    @ApiResponse(responseCode = "200", description = "Lista de citas")
    public ResponseEntity<List<AppointmentResponse>> findAll() {
        return ResponseEntity.ok(appointmentService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar una cita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita encontrada"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    public ResponseEntity<AppointmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.findById(id));
    }

    @GetMapping("/doctor/{doctorId}")
    @Operation(summary = "Listar citas de un doctor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Citas del doctor"),
            @ApiResponse(responseCode = "404", description = "Doctor no encontrado")
    })
    public ResponseEntity<List<AppointmentResponse>> findByDoctorId(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.findByDoctorId(doctorId));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancelar una cita")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita cancelada correctamente"),
            @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    public ResponseEntity<AppointmentResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancel(id));
    }
}
