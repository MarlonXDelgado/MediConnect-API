package com.dev.mxd.mediconnect.service;

import com.dev.mxd.mediconnect.dto.DoctorRequest;
import com.dev.mxd.mediconnect.dto.DoctorResponse;
import com.dev.mxd.mediconnect.exception.DoctorNotFoundException;
import com.dev.mxd.mediconnect.exception.DuplicateProfessionalLicenseException;
import com.dev.mxd.mediconnect.model.Doctor;
import com.dev.mxd.mediconnect.repository.DoctorRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

class DoctorServiceImplTest {

    private final DoctorRepository repository = mock(DoctorRepository.class);
    private final DoctorServiceImpl service = new DoctorServiceImpl(repository);

    @Test
    void shouldCreateDoctor() {
        DoctorRequest request = request("123");
        Doctor savedDoctor = doctor(1L, "123");
        when(repository.findByTarjetaProfesional("123")).thenReturn(Optional.empty());
        when(repository.save(any(Doctor.class))).thenReturn(savedDoctor);

        DoctorResponse response = service.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.tarjetaProfesional()).isEqualTo("123");
        verify(repository).save(any(Doctor.class));
    }

    @Test
    void shouldRejectDuplicateProfessionalLicense() {
        when(repository.findByTarjetaProfesional("123")).thenReturn(Optional.of(doctor(1L, "123")));

        assertThatThrownBy(() -> service.create(request("123")))
                .isInstanceOf(DuplicateProfessionalLicenseException.class);
        verify(repository, never()).save(any(Doctor.class));
    }

    @Test
    void shouldRejectUnknownDoctor() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(DoctorNotFoundException.class);
    }

    private DoctorRequest request(String tarjetaProfesional) {
        return new DoctorRequest("Ana", "Pérez", "Cardiología", tarjetaProfesional,
                "ana@example.com", "3001234567", true);
    }

    private Doctor doctor(Long id, String tarjetaProfesional) {
        return new Doctor(id, "Ana", "Pérez", "Cardiología", tarjetaProfesional,
                "ana@example.com", "3001234567", true);
    }
}
