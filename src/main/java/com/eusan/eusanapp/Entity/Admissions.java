package com.eusan.eusanapp.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admissions")
public class Admissions {

    @Id
    private String admissionId;

    @OneToOne
    @JoinColumn(name = "queue_id", nullable = false, unique = true)
    private PatientQueue patientQueue;

    private String ward;
    private String admissionReason;
    private String guardianEmail;
    private LocalDateTime admissionDate;

    @Column(nullable = false)
    private String doctorName;

    @Column(nullable = false)
    private String doctorWorkId;

    // No-argument constructor
    public Admissions() {
    }

    // All-arguments constructor
    public Admissions(String admissionId, PatientQueue patientQueue, String ward, String admissionReason,
                      String guardianEmail, LocalDateTime admissionDate, String doctorName, String doctorWorkId) {
        this.admissionId = admissionId;
        this.patientQueue = patientQueue;
        this.ward = ward;
        this.admissionReason = admissionReason;
        this.guardianEmail = guardianEmail;
        this.admissionDate = admissionDate;
        this.doctorName = doctorName;
        this.doctorWorkId = doctorWorkId;
    }

    // @PrePersist method to auto-generate admissionId and admissionDate
    @PrePersist
    public void prePersist() {
        if (this.admissionId == null) {
            this.admissionId = "A-" + UUID.randomUUID().toString().substring(0, 8);
        }
        this.admissionDate = LocalDateTime.now();
    }

    // Getters and Setters

    public String getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(String admissionId) {
        this.admissionId = admissionId;
    }

    public PatientQueue getPatientQueue() {
        return patientQueue;
    }

    public void setPatientQueue(PatientQueue patientQueue) {
        this.patientQueue = patientQueue;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getAdmissionReason() {
        return admissionReason;
    }

    public void setAdmissionReason(String admissionReason) {
        this.admissionReason = admissionReason;
    }

    public String getGuardianEmail() {
        return guardianEmail;
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail = guardianEmail;
    }

    public LocalDateTime getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDateTime admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorWorkId() {
        return doctorWorkId;
    }

    public void setDoctorWorkId(String doctorWorkId) {
        this.doctorWorkId = doctorWorkId;
    }
}
