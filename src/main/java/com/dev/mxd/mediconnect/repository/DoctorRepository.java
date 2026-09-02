package com.dev.mxd.mediconnect.repository;

import com.dev.mxd.mediconnect.model.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository {

    List<Doctor> findAll();

    Optional<Doctor> findById(Long id);

    Optional<Doctor> findByTarjetaProfesional(String tarjetaProfesional);

    Doctor save(Doctor doctor);

    void deleteById(Long id);
}
