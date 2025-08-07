package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Service.AdminService;
import com.eusan.eusanapp.Entity.Staff;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reset-password")
public class ResetPasswordController {

    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordController(AdminService adminService, PasswordEncoder passwordEncoder) {
        this.adminService = adminService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Staff staff = adminService.findByResetToken(token);

        if (staff == null || staff.getResetToken() == null) {
            model.addAttribute("message", "Invalid or expired token.");
            return "forgot-password";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping
    public String processResetPassword(@RequestParam("token") String token, @RequestParam("password") String password) {
        Staff staff = adminService.findByResetToken(token);

        if (staff != null && staff.getResetToken() != null) {
            // Encrypt and update password
            staff.setPassword(passwordEncoder.encode(password));
            
            // Invalidate the token immediately
            staff.setResetToken(null);
            
            // Save changes
            adminService.save(staff);
            
            return "redirect:/login?resetSuccess";
        }

        return "redirect:/forgot-password?error";
    }
}
