package com.eusan.eusanapp.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PatientQueue> queue;

    
    @NotBlank(message = "Name is required.")
    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", 
             message = "Name must contain only letters, spaces, and simple punctuation.")
    @Length(min = 2, max = 100, message = "Name must be between 2 and 100 characters.")
    @Column(nullable = false)
    private String name;
    
    @NotBlank(message = "Registration number is required.")
    @Pattern(regexp = "^[A-Z][0-9]{2}/[0-9]{5}/[0-9]{2}$", 
             message = "Registration number must be in format 'S13/04346/21'.")
    @Column(unique = true, nullable = false)
    private String regNo;
    
    @NotBlank(message = "Contact is required.")
    @Pattern(
        regexp = "^\\+[1-9]\\d{1,14}$",
        message = "Phone number must be in international format (e.g., +254712345678)."
    )
    @Column(unique = true, nullable = false)
    private String contact;
    
    @Past(message = "Date of birth must be in the past.")
    @NotNull(message = "Date of birth is required.")
    private LocalDate dob;
    
    @Transient
    private int age;
    
    @Column(nullable = false)
    private boolean active = true;
    
    // Constructors, getters and setters remain the same

    public Patient() {
    }

    public Patient(Long id, List<PatientQueue> queue, String name, String regNo, String contact,
                   LocalDate dob, int age, boolean active) {
        this.id = id;
        this.queue = queue;
        this.name = name;
        this.regNo = regNo;
        this.contact = contact;
        this.dob = dob;
        this.age = age;
        this.active = active;
    }

    // ===== Getters and Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PatientQueue> getQueue() {
        return queue;
    }

    public void setQueue(List<PatientQueue> queue) {
        this.queue = queue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }


    public int getAge() {
        return dob != null ? Period.between(this.dob, LocalDate.now()).getYears() : 0;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void setActive(boolean active) {
        this.active = active;
    }

    // ===== Additional Methods =====
}
