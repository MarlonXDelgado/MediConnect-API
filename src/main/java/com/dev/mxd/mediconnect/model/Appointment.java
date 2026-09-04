package com.dev.mxd.mediconnect.model;

import java.time.LocalDateTime;

public class Appointment {

    private Long id;
    private Long doctorId;
    private String pacienteNombre;
    private String pacienteCorreo;
    private String motivo;
    private LocalDateTime fechaHora;
    private AppointmentStatus estado;

    public Appointment() {
    }

    public Appointment(Long id, Long doctorId, String pacienteNombre, String pacienteCorreo,
                       String motivo, LocalDateTime fechaHora, AppointmentStatus estado) {
        this.id = id;
        this.doctorId = doctorId;
        this.pacienteNombre = pacienteNombre;
        this.pacienteCorreo = pacienteCorreo;
        this.motivo = motivo;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public String getPacienteCorreo() {
        return pacienteCorreo;
    }

    public void setPacienteCorreo(String pacienteCorreo) {
        this.pacienteCorreo = pacienteCorreo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public AppointmentStatus getEstado() {
        return estado;
    }

    public void setEstado(AppointmentStatus estado) {
        this.estado = estado;
    }
}
