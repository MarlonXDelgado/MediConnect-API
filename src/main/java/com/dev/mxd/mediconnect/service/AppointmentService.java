package com.dev.mxd.mediconnect.service;

import com.dev.mxd.mediconnect.dto.AppointmentRequest;
import com.dev.mxd.mediconnect.dto.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse create(AppointmentRequest request);

    List<AppointmentResponse> findAll();

    AppointmentResponse findById(Long id);

    List<AppointmentResponse> findByDoctorId(Long doctorId);

    AppointmentResponse cancel(Long id);
}
