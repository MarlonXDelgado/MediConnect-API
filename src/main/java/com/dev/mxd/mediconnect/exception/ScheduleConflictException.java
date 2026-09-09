package com.dev.mxd.mediconnect.exception;

import java.time.LocalDateTime;

public class ScheduleConflictException extends RuntimeException {

    public ScheduleConflictException(Long doctorId, LocalDateTime fechaHora) {
        super("El doctor " + doctorId + " ya tiene una cita programada para: " + fechaHora);
    }
}
