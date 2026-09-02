package com.dev.mxd.mediconnect.repository;

import com.dev.mxd.mediconnect.model.Doctor;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryDoctorRepository implements DoctorRepository {

    private final Map<Long, Doctor> doctors = new LinkedHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public synchronized List<Doctor> findAll() {
        return List.copyOf(doctors.values());
    }

    @Override
    public synchronized Optional<Doctor> findById(Long id) {
        return Optional.ofNullable(doctors.get(id));
    }

    @Override
    public synchronized Optional<Doctor> findByTarjetaProfesional(String tarjetaProfesional) {
        return doctors.values().stream()
                .filter(doctor -> tarjetaProfesional.equals(doctor.getTarjetaProfesional()))
                .findFirst();
    }

    @Override
    public synchronized Doctor save(Doctor doctor) {
        if (doctor.getId() == null) {
            doctor.setId(nextId.getAndIncrement());
        }
        doctors.put(doctor.getId(), doctor);
        return doctor;
    }

    @Override
    public synchronized void deleteById(Long id) {
        doctors.remove(id);
    }
}
