package com.dev.mxd.mediconnect.service;

import com.dev.mxd.mediconnect.dto.DoctorRequest;
import com.dev.mxd.mediconnect.dto.DoctorResponse;
import com.dev.mxd.mediconnect.exception.DoctorNotFoundException;
import com.dev.mxd.mediconnect.exception.DuplicateProfessionalLicenseException;
import com.dev.mxd.mediconnect.model.Doctor;
import com.dev.mxd.mediconnect.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public DoctorResponse create(DoctorRequest request) {
        validateUniqueLicense(request.tarjetaProfesional(), null);
        Doctor doctor = toDoctor(request);
        return toResponse(doctorRepository.save(doctor));
    }

    @Override
    public List<DoctorResponse> findAll() {
        return doctorRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public DoctorResponse findById(Long id) {
        return doctorRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new DoctorNotFoundException(id));
    }

    @Override
    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        validateUniqueLicense(request.tarjetaProfesional(), id);
        updateDoctor(doctor, request);
        return toResponse(doctorRepository.save(doctor));
    }

    @Override
    public void delete(Long id) {
        doctorRepository.findById(id)
                .orElseThrow(() -> new DoctorNotFoundException(id));
        doctorRepository.deleteById(id);
    }

    private void validateUniqueLicense(String tarjetaProfesional, Long doctorId) {
        doctorRepository.findByTarjetaProfesional(tarjetaProfesional)
                .filter(existingDoctor -> !existingDoctor.getId().equals(doctorId))
                .ifPresent(existingDoctor -> {
                    throw new DuplicateProfessionalLicenseException(tarjetaProfesional);
                });
    }

    private Doctor toDoctor(DoctorRequest request) {
        return new Doctor(
                null,
                request.nombre(),
                request.apellido(),
                request.especialidad(),
                request.tarjetaProfesional(),
                request.correo(),
                request.telefono(),
                request.disponible()
        );
    }

    private void updateDoctor(Doctor doctor, DoctorRequest request) {
        doctor.setNombre(request.nombre());
        doctor.setApellido(request.apellido());
        doctor.setEspecialidad(request.especialidad());
        doctor.setTarjetaProfesional(request.tarjetaProfesional());
        doctor.setCorreo(request.correo());
        doctor.setTelefono(request.telefono());
        doctor.setDisponible(request.disponible());
    }

    private DoctorResponse toResponse(Doctor doctor) {
        return new DoctorResponse(
                doctor.getId(),
                doctor.getNombre(),
                doctor.getApellido(),
                doctor.getEspecialidad(),
                doctor.getTarjetaProfesional(),
                doctor.getCorreo(),
                doctor.getTelefono(),
                doctor.getDisponible()
        );
    }
}
