package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Service.EmailService;
import com.eusan.eusanapp.Service.AdminService;
import com.eusan.eusanapp.Entity.Staff;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    private final AdminService adminService;
    private final EmailService emailService;

    public ForgotPasswordController(AdminService adminService, EmailService emailService) {
        this.adminService = adminService;
        this.emailService = emailService;
    }

    @GetMapping
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping
    public String processForgotPassword(@RequestParam("email") String personalEmail, Model model) {
        // Use personalEmail for validation
        Staff staff = adminService.findByPersonalEmail(personalEmail);

        if (staff == null) {
            model.addAttribute("message", "No account found with this email.");
            return "forgot-password";
        }

        // Generate Reset Token
        String resetToken = UUID.randomUUID().toString();
        staff.setResetToken(resetToken);
        adminService.save(staff); // Save the updated staff with the token

        // Construct Reset Link
        String resetLink = "http://localhost:9092/reset-password?token=" + resetToken;
        String emailMessage = "<h3>Password Reset Request</h3>"
                + "<p>Click the link below to reset your password:</p>"
                + "<a href='" + resetLink + "'>Reset Password</a>";

        try {
            emailService.sendEmail(personalEmail, "Password Reset Request", emailMessage);
            model.addAttribute("message", "Password reset link sent to your email.");
        } catch (MessagingException e) {
            model.addAttribute("message", "Error sending email. Please try again.");
        }

        return "forgot-password";
    }
}
