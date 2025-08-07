package com.eusan.eusanapp.Service;

import com.eusan.eusanapp.Entity.Role;
import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Repository.StaffRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;


import java.util.List;
import java.util.Optional;
import java.time.LocalDate;


@Service
public class AdminService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;
    private final EmailService emailService;

    public AdminService(StaffRepository staffRepository, PasswordEncoder passwordEncoder, SmsService smsService, EmailService emailService) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
        this.smsService = smsService;
        this.emailService = emailService;
    }

    public Staff findByWorkEmail(String workEmail) {
        return staffRepository.findByWorkEmail(workEmail).orElse(null); // Correctly extract from Optional
    }
    public Staff registerStaff(String name, String contact, String nationalId, LocalDate contractExpiryDate, String personalEmail, Role role) {
        Staff staff = new Staff(name, contact, nationalId, contractExpiryDate, personalEmail, role);
        String encodedPassword = passwordEncoder.encode(staff.getPassword());
        staff.setPassword(encodedPassword);
        staffRepository.save(staff);
    
        String message = String.format(
            "Welcome to EUSAN, %s! Your Work ID: %s, Email: %s, Password: %s",
            name, staff.getWorkId(), staff.getWorkEmail(), staff.getWorkId()
        );
        smsService.sendSms(contact, message);
    
        String emailMessage = String.format(
            "<h3>Welcome to EUSAN, %s!</h3>"
                + "<p>Your account has been created. Here are your login credentials:</p>"
                + "<ul><li><b>Work ID:</b> %s</li><li><b>Work Email:</b> %s</li><li><b>Password:</b> %s</li></ul>"
                + "<p>Contract expires on: <b>%s</b></p>"
                + "<p>Kindly log in and change your password immediately.</p>",
            name, staff.getWorkId(), staff.getWorkEmail(), staff.getWorkId(), contractExpiryDate
        );
    
        try {
            emailService.sendEmail(personalEmail, "Welcome to EUSAN - Your Account Details", emailMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    
        return staff;
    }
    

    public Staff getByWorkEmail(String workEmail) {
        Optional<Staff> staff = staffRepository.findByWorkEmail(workEmail);
        return staff.orElse(null); // or throw an exception if preferred
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }
    public Staff findByPersonalEmail(String personalEmail) {
        return staffRepository.findByPersonalEmail(personalEmail);
    }
    public Staff findByResetToken(String resetToken) {
        return staffRepository.findByResetToken(resetToken);
    }
    public List<Staff> getStaffSortedByDate() {
        return staffRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }
    public void save(Staff staff) {
        staffRepository.save(staff);
    }
    
    // ✅ **Search Staff by Name, Work ID, or Role**
    // public List<Staff> searchStaff(String keyword) {
    //     return staffRepository.findByNameContainingIgnoreCaseOrWorkIdContainingIgnoreCaseOrRole(
    //             keyword, keyword, Role.valueOf(keyword.toUpperCase())
    //     );
    // }
    public List<Staff> searchStaff(String keyword) {
        return staffRepository.searchByKeyword(keyword);
    }
// Get staff by ID
public Staff getStaffById(Long id) {
    return staffRepository.findById(id).orElse(null);
}

// Update staff details
public void updateStaff(Staff staff) {
    Optional<Staff> existingStaff = staffRepository.findById(staff.getId());
    if (existingStaff.isPresent()) {
        Staff updatedStaff = existingStaff.get();
        updatedStaff.setName(staff.getName());
        updatedStaff.setContact(staff.getContact());
        updatedStaff.setNationalId(staff.getNationalId());
        updatedStaff.setRole(staff.getRole());
        updatedStaff.setWorkEmail(staff.getWorkEmail());
        staffRepository.save(updatedStaff);
    }
}

// Delete staff by ID
public void deleteStaff(Long id) {
    staffRepository.deleteById(id);
}

// Reset staff details (e.g., reset password and contact details)
public void resetStaffDetails(Long id) {
    Optional<Staff> existingStaff = staffRepository.findById(id);
    if (existingStaff.isPresent()) {
        Staff staff = existingStaff.get();
        staff.setPassword(passwordEncoder.encode("default123")); // Reset password
        staff.setContact("0000000000"); // Reset contact to a default value
        staffRepository.save(staff);
    }
    
    
}
public Staff getByWorkId(String workId) {
    return staffRepository.findByWorkId(workId)
            .orElseThrow(() -> new RuntimeException("Staff not found with work ID: " + workId));
}

}
