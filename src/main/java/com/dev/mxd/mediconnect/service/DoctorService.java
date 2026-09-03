package com.dev.mxd.mediconnect.service;

import com.dev.mxd.mediconnect.dto.DoctorRequest;
import com.dev.mxd.mediconnect.dto.DoctorResponse;

import java.util.List;

public interface DoctorService {

    DoctorResponse create(DoctorRequest request);

    List<DoctorResponse> findAll();

    DoctorResponse findById(Long id);

    DoctorResponse update(Long id, DoctorRequest request);

    void delete(Long id);
}
