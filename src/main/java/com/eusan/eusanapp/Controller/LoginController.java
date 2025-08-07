package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Repository.StaffRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private final StaffRepository staffRepository;

    public LoginController(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/redirect")
    public String redirectUser(Authentication authentication) {
        String workId = authentication.getName();
        Staff staff = staffRepository.findByWorkId(workId).orElseThrow();

        switch (staff.getRole()) {
            case ADMIN: return "redirect:/admin/dashboard";
            case DOCTOR: return "redirect:/doctor/dashboard";
            case TRIAGE_OFFICER: return "redirect:/triage/dashboard";
            case RECEPTIONIST: return "redirect:/receptionist/dashboard";
            case LAB_TECHNICIAN: return "redirect:/lab/dashboard";
            case PHARMACIST: return "redirect:/pharmacy/dashboard";
            case DENTIST: return "redirect:/dentist/dashboard";
            case INFECTIOUS_DOCTOR: return "redirect:/vct/dashboard";
            case CHIEF_MEDICAL_OFFICER: return "redirect:/cmo/dashboard";
            default: return "redirect:/login";
        }
    }
}

