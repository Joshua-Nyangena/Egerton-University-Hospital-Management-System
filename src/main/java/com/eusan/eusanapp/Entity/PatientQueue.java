package com.eusan.eusanapp.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "queue")
public class PatientQueue {

    @Id
    private String queueId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false) // This sets up the foreign key
    private Patient patient;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING) // Store enum as a String in the database
    @Column(nullable = false, length = 20)
    private Status status; // PENDING, SERVING, COMPLETED

    @Column(nullable = false, length = 50)
    private String specification; // General Consultation, VCT, Dentist, CMO

    @Transient // Tells JPA not to persist this field in the DB
    private String dateFormatted;

    // No-args constructor
    public PatientQueue() {
    }

    // All-args constructor
    public PatientQueue(String queueId, Patient patient, LocalDateTime date, Status status, String specification, String dateFormatted) {
        this.queueId = queueId;
        this.patient = patient;
        this.date = date;
        this.status = status;
        this.specification = specification;
        this.dateFormatted = dateFormatted;
    }

    @PrePersist
    public void generateQueueId() {
        this.queueId = "Q-" + UUID.randomUUID().toString().substring(0, 8);
        this.date = LocalDateTime.now();
        this.status = Status.PENDING;
    }

    // Getters and Setters

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getDateFormatted() {
        return dateFormatted;
    }

    public void setDateFormatted(String dateFormatted) {
        this.dateFormatted = dateFormatted;
    }
}
