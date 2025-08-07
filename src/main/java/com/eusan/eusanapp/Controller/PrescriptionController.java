package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Config.StaffUserDetails;
import com.eusan.eusanapp.Entity.Prescription;
import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Service.PrescriptionService;
import com.eusan.eusanapp.Service.MedicationService;
import com.eusan.eusanapp.Service.AdminService;
import com.eusan.eusanapp.Entity.Medication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pharmacy")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;
    @Autowired
    private MedicationService medicationService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
public String pharmacyDashboard(Model model, @AuthenticationPrincipal StaffUserDetails loggedInUser) {
    Staff staff = adminService.getByWorkId(loggedInUser.getUsername());
    model.addAttribute("staff", staff);
    return "pharmacist_dashboard";
}

    // Display all prescriptions
    
    @GetMapping("/prescriptions")
    public String viewPrescriptions(@RequestParam(defaultValue = "completed") String sortType,
                                     @RequestParam(required = false) String keyword, // New parameter for search
                                     Model model) {
        List<Prescription> prescriptions;
    
        if (keyword != null && !keyword.isEmpty()) {
            prescriptions = prescriptionService.searchPrescriptions(keyword);  // Call the search service method
        } else {
            // Sorting logic based on sortType
            if ("date".equals(sortType)) {
                prescriptions = prescriptionService.getPrescriptionsSortedByDate();
            } else if ("all".equals(sortType)) {
                prescriptions = prescriptionService.getAllPrescriptions();
            } else {
                prescriptions = prescriptionService.getCompletedQueuePrescriptions();
            }
        }
    
        model.addAttribute("prescriptions", prescriptions);
        model.addAttribute("keyword", keyword);  // Include keyword for the search bar
        return "fragments/pending_prescriptions :: content"; // Ensure the fragment is updated
    }
    


    
    // Issue Medication
    @PostMapping("/issueMedication/{prescriptionId}")
    public String issueMedication(@PathVariable Long prescriptionId) {
        boolean success = prescriptionService.issueMedication(prescriptionId);
        return "redirect:/pharmacy/dashboard"; // Refresh the page after issuing
    }
}

