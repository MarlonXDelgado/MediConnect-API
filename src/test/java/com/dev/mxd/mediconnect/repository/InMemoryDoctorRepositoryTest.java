package com.dev.mxd.mediconnect.repository;

import com.dev.mxd.mediconnect.model.Doctor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryDoctorRepositoryTest {

    private final InMemoryDoctorRepository repository = new InMemoryDoctorRepository();

    @Test
    void shouldGenerateIdAndFindDoctorById() {
        Doctor doctor = repository.save(doctor("123"));

        assertThat(doctor.getId()).isEqualTo(1L);
        assertThat(repository.findById(doctor.getId())).contains(doctor);
    }

    @Test
    void shouldFindDoctorByProfessionalLicense() {
        Doctor doctor = repository.save(doctor("123"));

        assertThat(repository.findByTarjetaProfesional("123")).contains(doctor);
    }

    @Test
    void shouldDeleteDoctor() {
        Doctor doctor = repository.save(doctor("123"));

        repository.deleteById(doctor.getId());

        assertThat(repository.findAll()).isEmpty();
    }

    private Doctor doctor(String tarjetaProfesional) {
        return new Doctor(null, "Ana", "Pérez", "Cardiología", tarjetaProfesional,
                "ana@example.com", "3001234567", true);
    }
}
