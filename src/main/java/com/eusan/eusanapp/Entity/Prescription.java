package com.eusan.eusanapp.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "diagnosis_id", referencedColumnName = "diagnosisId", nullable = false, unique = true)
    private Diagnosis diagnosis;

    private String prescriptionDetails; // Medication details
    private LocalDateTime date;

    @PrePersist
    public void prePersist() {
        this.date = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getPrescriptionDetails() {
        return prescriptionDetails;
    }

    public void setPrescriptionDetails(String prescriptionDetails) {
        this.prescriptionDetails = prescriptionDetails;
    }

    public LocalDateTime getDateTime() {
        return date;
    }

    public void setDateTime(LocalDateTime date) {
        this.date = date;
    }
}
