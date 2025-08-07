package com.eusan.eusanapp.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "staff")
public class Staff{ 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required.")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters.")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Contact is required.")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Invalid international phone number format.")
    @Column(unique = true, nullable = false)
    private String contact;

    @NotBlank(message = "National ID is required.")
    @Pattern(regexp = "^[0-9]{6,14}$", message = "National ID must be between 6 and 14 digits.")
    @Column(unique = true, nullable = false)
    private String nationalId;  

    private String workId;
    private String workEmail;
    private String password;

    @Future(message = "Contract expiry date must be in the future.")
    private LocalDate contractExpiryDate;

    @NotBlank(message = "Personal email is required.")
    @Email(message = "Please provide a valid email address.")
    @Column(unique = true, nullable = false)
    private String personalEmail;

    private String resetToken;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is required.")
    private Role role;

    // Constructors
    public Staff() {}

    public Staff(String name, String contact, String nationalId, LocalDate contractExpiryDate, String personalEmail, Role role) {
        this.name = name;
        this.contact = contact;
        this.nationalId = nationalId;
        this.contractExpiryDate = contractExpiryDate;
        this.personalEmail = personalEmail;
        this.role = role;
        this.workId = generateWorkId();
        this.workEmail = generateWorkEmail();
        this.password = this.workId; // Default password is Work ID
    }

    // Custom methods
    private String generateWorkId() {
        return "EUSAN-" + UUID.randomUUID().toString().substring(0, 6);
    }

    private String generateWorkEmail() {
        return name.toLowerCase().replace(" ", ".") + "@eusan.ac.ke";
    }

    public GrantedAuthority getRoleAsAuthority() {
        return new SimpleGrantedAuthority("ROLE_" + this.role.name());
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getContractExpiryDate() {
        return contractExpiryDate;
    }

    public void setContractExpiryDate(LocalDate contractExpiryDate) {
        this.contractExpiryDate = contractExpiryDate;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // toString (optional for logging/debugging)
    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", workId='" + workId + '\'' +
                ", workEmail='" + workEmail + '\'' +
                ", personalEmail='" + personalEmail + '\'' +
                ", role=" + role +
                '}';
    }
}
