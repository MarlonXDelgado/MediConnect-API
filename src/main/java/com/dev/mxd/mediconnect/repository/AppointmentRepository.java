package com.dev.mxd.mediconnect.repository;

import com.dev.mxd.mediconnect.model.Appointment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {

    List<Appointment> findAll();

    Optional<Appointment> findById(Long id);

    List<Appointment> findByDoctorId(Long doctorId);

    boolean existsByDoctorIdAndFechaHoraAndActiva(Long doctorId, LocalDateTime fechaHora);

    Appointment save(Appointment appointment);

    void deleteById(Long id);
}
