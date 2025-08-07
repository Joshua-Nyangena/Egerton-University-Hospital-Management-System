package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Repository.StaffRepository;
import com.eusan.eusanapp.Service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final StaffRepository staffRepository;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    public ProfileController(StaffRepository staffRepository, AdminService adminService, PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String viewProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String workId = auth.getName();
        
        Staff staff = staffRepository.findByWorkId(workId).orElse(null);
        
        if (staff == null) {
            return "redirect:/login"; // Redirect to login if not found
        }
        
        model.addAttribute("staff", staff);
        return "fragments/profile ::content";
    }

    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestParam("currentPassword") String currentPassword,
                                                  @RequestParam("newPassword") String newPassword,
                                                  @RequestParam("confirmPassword") String confirmPassword) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String workId = auth.getName();

        Staff staff = staffRepository.findByWorkId(workId).orElse(null);

        if (staff == null) {
            return ResponseEntity.status(401).body("Session expired. Please log in again.");
        }

        if (!passwordEncoder.matches(currentPassword, staff.getPassword())) {
            return ResponseEntity.badRequest().body("Current password is incorrect.");
        }

        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.badRequest().body("New passwords do not match.");
        }

        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";
        if (!newPassword.matches(passwordPattern)) {
            return ResponseEntity.badRequest().body("New password does not meet security requirements.");
        }

        // Save new password
        staff.setPassword(passwordEncoder.encode(newPassword));
        staffRepository.save(staff);

        return ResponseEntity.ok("success");
    }
}
