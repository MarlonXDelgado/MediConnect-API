package com.dev.mxd.mediconnect.repository;

import com.dev.mxd.mediconnect.model.Appointment;
import com.dev.mxd.mediconnect.model.AppointmentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryAppointmentRepositoryTest {

    private final InMemoryAppointmentRepository repository = new InMemoryAppointmentRepository();
    private final LocalDateTime appointmentTime = LocalDateTime.of(2030, 1, 15, 10, 0);

    @Test
    void shouldGenerateIdAndFindAppointmentById() {
        Appointment appointment = repository.save(appointment(AppointmentStatus.SCHEDULED));

        assertThat(appointment.getId()).isEqualTo(1L);
        assertThat(repository.findById(1L)).contains(appointment);
    }

    @Test
    void shouldFindAppointmentsByDoctor() {
        Appointment appointment = repository.save(appointment(AppointmentStatus.SCHEDULED));

        assertThat(repository.findByDoctorId(1L)).containsExactly(appointment);
    }

    @Test
    void shouldDetectActiveScheduleConflict() {
        repository.save(appointment(AppointmentStatus.SCHEDULED));

        assertThat(repository.existsByDoctorIdAndFechaHoraAndActiva(1L, appointmentTime)).isTrue();
    }

    @Test
    void shouldIgnoreCancelledAppointmentsInScheduleConflict() {
        repository.save(appointment(AppointmentStatus.CANCELLED));

        assertThat(repository.existsByDoctorIdAndFechaHoraAndActiva(1L, appointmentTime)).isFalse();
    }

    @Test
    void shouldDeleteAppointment() {
        Appointment appointment = repository.save(appointment(AppointmentStatus.SCHEDULED));

        repository.deleteById(appointment.getId());

        assertThat(repository.findAll()).isEmpty();
    }

    private Appointment appointment(AppointmentStatus status) {
        return new Appointment(null, 1L, "Ana Pérez", "ana@example.com",
                "Consulta general", appointmentTime, status);
    }
}
