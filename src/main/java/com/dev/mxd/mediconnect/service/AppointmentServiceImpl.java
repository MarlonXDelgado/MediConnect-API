package com.dev.mxd.mediconnect.service;

import com.dev.mxd.mediconnect.dto.AppointmentRequest;
import com.dev.mxd.mediconnect.dto.AppointmentResponse;
import com.dev.mxd.mediconnect.exception.AppointmentNotFoundException;
import com.dev.mxd.mediconnect.exception.DoctorNotFoundException;
import com.dev.mxd.mediconnect.exception.ScheduleConflictException;
import com.dev.mxd.mediconnect.model.Appointment;
import com.dev.mxd.mediconnect.model.AppointmentStatus;
import com.dev.mxd.mediconnect.repository.AppointmentRepository;
import com.dev.mxd.mediconnect.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
    }

    @Override
    public AppointmentResponse create(AppointmentRequest request) {
        validateDoctorExists(request.doctorId());
        if (appointmentRepository.existsByDoctorIdAndFechaHoraAndActiva(
                request.doctorId(), request.fechaHora())) {
            throw new ScheduleConflictException(request.doctorId(), request.fechaHora());
        }

        Appointment appointment = new Appointment(
                null,
                request.doctorId(),
                request.pacienteNombre(),
                request.pacienteCorreo(),
                request.motivo(),
                request.fechaHora(),
                AppointmentStatus.SCHEDULED
        );
        return toResponse(appointmentRepository.save(appointment));
    }

    @Override
    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AppointmentResponse findById(Long id) {
        return appointmentRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
    }

    @Override
    public List<AppointmentResponse> findByDoctorId(Long doctorId) {
        validateDoctorExists(doctorId);
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AppointmentResponse cancel(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
        appointment.setEstado(AppointmentStatus.CANCELLED);
        return toResponse(appointmentRepository.save(appointment));
    }

    private void validateDoctorExists(Long doctorId) {
        if (doctorRepository.findById(doctorId).isEmpty()) {
            throw new DoctorNotFoundException(doctorId);
        }
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getDoctorId(),
                appointment.getPacienteNombre(),
                appointment.getPacienteCorreo(),
                appointment.getMotivo(),
                appointment.getFechaHora(),
                appointment.getEstado().name()
        );
    }
}
