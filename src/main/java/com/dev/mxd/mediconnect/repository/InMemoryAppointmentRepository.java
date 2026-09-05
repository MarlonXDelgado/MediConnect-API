package com.dev.mxd.mediconnect.repository;

import com.dev.mxd.mediconnect.model.Appointment;
import com.dev.mxd.mediconnect.model.AppointmentStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryAppointmentRepository implements AppointmentRepository {

    private final Map<Long, Appointment> appointments = new LinkedHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public synchronized List<Appointment> findAll() {
        return List.copyOf(appointments.values());
    }

    @Override
    public synchronized Optional<Appointment> findById(Long id) {
        return Optional.ofNullable(appointments.get(id));
    }

    @Override
    public synchronized List<Appointment> findByDoctorId(Long doctorId) {
        return appointments.values().stream()
                .filter(appointment -> doctorId.equals(appointment.getDoctorId()))
                .toList();
    }

    @Override
    public synchronized boolean existsByDoctorIdAndFechaHoraAndActiva(Long doctorId, LocalDateTime fechaHora) {
        return appointments.values().stream()
                .anyMatch(appointment -> doctorId.equals(appointment.getDoctorId())
                        && fechaHora.equals(appointment.getFechaHora())
                        && appointment.getEstado() != AppointmentStatus.CANCELLED);
    }

    @Override
    public synchronized Appointment save(Appointment appointment) {
        if (appointment.getId() == null) {
            appointment.setId(nextId.getAndIncrement());
        }
        appointments.put(appointment.getId(), appointment);
        return appointment;
    }

    @Override
    public synchronized void deleteById(Long id) {
        appointments.remove(id);
    }
}
