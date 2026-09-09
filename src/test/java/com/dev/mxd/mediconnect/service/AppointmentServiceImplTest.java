package com.dev.mxd.mediconnect.service;

import com.dev.mxd.mediconnect.dto.AppointmentRequest;
import com.dev.mxd.mediconnect.dto.AppointmentResponse;
import com.dev.mxd.mediconnect.exception.AppointmentNotFoundException;
import com.dev.mxd.mediconnect.exception.DoctorNotFoundException;
import com.dev.mxd.mediconnect.exception.ScheduleConflictException;
import com.dev.mxd.mediconnect.model.Appointment;
import com.dev.mxd.mediconnect.model.AppointmentStatus;
import com.dev.mxd.mediconnect.model.Doctor;
import com.dev.mxd.mediconnect.repository.AppointmentRepository;
import com.dev.mxd.mediconnect.repository.DoctorRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AppointmentServiceImplTest {

    private final AppointmentRepository appointmentRepository = mock(AppointmentRepository.class);
    private final DoctorRepository doctorRepository = mock(DoctorRepository.class);
    private final AppointmentServiceImpl service =
            new AppointmentServiceImpl(appointmentRepository, doctorRepository);

    @Test
    void shouldCreateScheduledAppointment() {
        AppointmentRequest request = request();
        Appointment saved = appointment(1L, AppointmentStatus.SCHEDULED);
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor()));
        when(appointmentRepository.existsByDoctorIdAndFechaHoraAndActiva(1L, request.fechaHora()))
                .thenReturn(false);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(saved);

        AppointmentResponse response = service.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.estado()).isEqualTo("SCHEDULED");
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void shouldRejectScheduleConflict() {
        AppointmentRequest request = request();
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor()));
        when(appointmentRepository.existsByDoctorIdAndFechaHoraAndActiva(1L, request.fechaHora()))
                .thenReturn(true);

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(ScheduleConflictException.class);
    }

    @Test
    void shouldRejectUnknownDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(request()))
                .isInstanceOf(DoctorNotFoundException.class);
    }

    @Test
    void shouldCancelAppointment() {
        Appointment appointment = appointment(1L, AppointmentStatus.SCHEDULED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(appointment)).thenReturn(appointment);

        AppointmentResponse response = service.cancel(1L);

        assertThat(response.estado()).isEqualTo("CANCELLED");
        verify(appointmentRepository).save(appointment);
    }

    @Test
    void shouldRejectUnknownAppointment() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(AppointmentNotFoundException.class);
    }

    @Test
    void shouldListAllAppointments() {
        when(appointmentRepository.findAll())
                .thenReturn(List.of(appointment(1L, AppointmentStatus.SCHEDULED)));

        assertThat(service.findAll()).hasSize(1);
    }

    @Test
    void shouldListAppointmentsByDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor()));
        when(appointmentRepository.findByDoctorId(1L))
                .thenReturn(List.of(appointment(1L, AppointmentStatus.SCHEDULED)));

        assertThat(service.findByDoctorId(1L)).hasSize(1);
    }

    @Test
    void shouldRejectAppointmentsByUnknownDoctor() {
        when(doctorRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByDoctorId(99L))
                .isInstanceOf(DoctorNotFoundException.class);
    }

    @Test
    void shouldRejectCancelWhenAppointmentDoesNotExist() {
        when(appointmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cancel(99L))
                .isInstanceOf(AppointmentNotFoundException.class);
    }

    private AppointmentRequest request() {
        return new AppointmentRequest(1L, "Ana Pérez", "ana@example.com",
                "Consulta general", LocalDateTime.now().plusDays(1));
    }

    private Appointment appointment(Long id, AppointmentStatus status) {
        return new Appointment(id, 1L, "Ana Pérez", "ana@example.com",
                "Consulta general", LocalDateTime.now().plusDays(1), status);
    }

    private Doctor doctor() {
        return new Doctor(1L, "Luis", "Gómez", "Cardiología", "123",
                "luis@example.com", "3001234567", true);
    }
}
