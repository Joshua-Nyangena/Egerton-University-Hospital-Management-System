package com.eusan.eusanapp.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "diagnosis")
public class Diagnosis {

    @Id
    private String diagnosisId;

    @OneToOne
    @JoinColumn(name = "queue_id", nullable = false, unique = true) // Each queue entry has one diagnosis
    private PatientQueue patientQueue;

    private String labTest; // Requested lab test
    private LocalDateTime date;
    private boolean completed = false; // New field to track completion status

    // No-argument constructor
    public Diagnosis() {
    }

    // All-argument constructor
    public Diagnosis(String diagnosisId, PatientQueue patientQueue, String labTest, LocalDateTime date, boolean completed) {
        this.diagnosisId = diagnosisId;
        this.patientQueue = patientQueue;
        this.labTest = labTest;
        this.date = date;
        this.completed = completed;
    }

    @PrePersist
    public void prePersist() {
        if (this.diagnosisId == null) {
            this.diagnosisId = "D-" + UUID.randomUUID().toString().substring(0, 8); // Generate unique ID
        }
        this.date = LocalDateTime.now();
    }

    // Getters and Setters

    public String getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(String diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public PatientQueue getPatientQueue() {
        return patientQueue;
    }

    public void setPatientQueue(PatientQueue patientQueue) {
        this.patientQueue = patientQueue;
    }

    public String getLabTest() {
        return labTest;
    }

    public void setLabTest(String labTest) {
        this.labTest = labTest;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
