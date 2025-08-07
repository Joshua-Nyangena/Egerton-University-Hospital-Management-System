package com.eusan.eusanapp.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "triage")
public class Triage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "queueId", nullable = false)
    private PatientQueue queue;

    @NotNull(message = "Temperature is required")
    @DecimalMin(value = "35.0", message = "Temperature must be at least 35.0°C")
    @DecimalMax(value = "42.0", message = "Temperature must be at most 42.0°C")
    private double temperature;

    @NotNull(message = "Pulse rate is required")
    @Min(value = 40, message = "Pulse rate must be at least 40 BPM")
    @Max(value = 200, message = "Pulse rate must be at most 200 BPM")
    private int pulseRate;

    @NotNull(message = "Blood pressure is required")
    @Min(value = 70, message = "Blood pressure must be at least 70 mmHg")
    @Max(value = 200, message = "Blood pressure must be at most 200 mmHg")
    private int bloodPressure;

    @NotNull(message = "Weight is required")
    @DecimalMin(value = "2.0", message = "Weight must be at least 2.0 kg")
    @DecimalMax(value = "300.0", message = "Weight must be at most 300.0 kg")
    private double weight;

    @NotNull(message = "Height is required")
    @DecimalMin(value = "30.0", message = "Height must be at least 30.0 cm")
    @DecimalMax(value = "250.0", message = "Height must be at most 250.0 cm")
    private double height;

    private LocalDateTime recordedAt;

    // No-args constructor
    public Triage() {
    }

    // All-args constructor
    public Triage(Long id, PatientQueue queue, double temperature, int pulseRate, int bloodPressure, double weight, double height, LocalDateTime recordedAt) {
        this.id = id;
        this.queue = queue;
        this.temperature = temperature;
        this.pulseRate = pulseRate;
        this.bloodPressure = bloodPressure;
        this.weight = weight;
        this.height = height;
        this.recordedAt = recordedAt;
    }

    @PrePersist
    public void setTimestamp() {
        this.recordedAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PatientQueue getQueue() {
        return queue;
    }

    public void setQueue(PatientQueue queue) {
        this.queue = queue;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getPulseRate() {
        return pulseRate;
    }

    public void setPulseRate(int pulseRate) {
        this.pulseRate = pulseRate;
    }

    public int getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(int bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}