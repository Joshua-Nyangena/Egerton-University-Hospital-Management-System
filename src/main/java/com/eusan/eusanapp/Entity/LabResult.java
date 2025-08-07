package com.eusan.eusanapp.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_results")
public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "diagnosis_id", referencedColumnName = "diagnosisId", nullable = false) // Explicit reference
    private Diagnosis diagnosis;

    @Column(nullable = false)
    private String resultDetails; // Lab test result details

    private LocalDateTime date;

    // No-args constructor
    public LabResult() {
    }

    // All-args constructor
    public LabResult(Long id, Diagnosis diagnosis, String resultDetails, LocalDateTime date) {
        this.id = id;
        this.diagnosis = diagnosis;
        this.resultDetails = resultDetails;
        this.date = date;
    }

    @PrePersist
    public void setRecordedAt() {
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

    public String getResultDetails() {
        return resultDetails;
    }

    public void setResultDetails(String resultDetails) {
        this.resultDetails = resultDetails;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
