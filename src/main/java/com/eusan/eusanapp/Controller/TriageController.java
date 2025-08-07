package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Config.StaffUserDetails;
import com.eusan.eusanapp.Entity.Triage;
import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Service.QueueService;
import com.eusan.eusanapp.Service.TriageService;
import com.eusan.eusanapp.Service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Controller
@RequestMapping("/triage")
public class TriageController {

    private final QueueService queueService;
    private final TriageService triageService;
    private final AdminService adminService;

    public TriageController(QueueService queueService, TriageService triageService, AdminService adminService) {
        this.queueService = queueService;
        this.triageService = triageService;
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
public String triageDashboard(Model model, @AuthenticationPrincipal StaffUserDetails loggedInUser) {
    Staff staff = adminService.getByWorkId(loggedInUser.getUsername());
    model.addAttribute("staff", staff);
    return "triage_dashboard";
}


     @GetMapping("/pending")
     public String showPendingPatients(Model model) {
          List<PatientQueue> pendingPatients = queueService.getPendingPatients();
          model.addAttribute("patients", pendingPatients);
          return "fragments/triage_pending :: content";
     }

    @GetMapping("/record/{queueId}")
    public String showTriageForm(@PathVariable String queueId, Model model) {
            PatientQueue queueEntry = queueService.getQueueById(queueId);
            model.addAttribute("queueEntry", queueEntry);
            model.addAttribute("triage", new Triage());
            return "fragments/triage_form :: content";
    }

    @PostMapping("/saveTriage")
    public String saveTriageData(@ModelAttribute Triage triage, @RequestParam String queueId) {
        triageService.saveTriageData(triage, queueId);
        return "redirect:/triage/dashboard";
    }

    // @GetMapping("/view/{queueId}")
    // public String viewTriageDetails(@PathVariable String queueId, Model model) {
    //     Triage triage = triageService.getTriageByQueueId(queueId);
    //     model.addAttribute("triage", triage);
    //     return "fragments/triage_details :: content";
    // }
    @GetMapping("/pending/patients")
    public String viewPendingPatients(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<PatientQueue> patients;
        if (keyword != null && !keyword.isEmpty()) {
            patients = queueService.searchPendingPatients(keyword);
        } else {
            patients = queueService.getPendingPatients();
        }
        model.addAttribute("patients", patients);
        model.addAttribute("keyword", keyword);
        return "fragments/triage_pending :: content";
    }
}
