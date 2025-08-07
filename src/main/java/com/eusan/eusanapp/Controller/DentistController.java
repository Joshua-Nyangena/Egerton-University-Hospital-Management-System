package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Entity.Diagnosis;
import com.eusan.eusanapp.Entity.LabResult;
import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Entity.Prescription;
import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Entity.Status;
import com.eusan.eusanapp.Entity.Triage;
import com.eusan.eusanapp.Config.StaffUserDetails;
import com.eusan.eusanapp.Entity.Admissions;
import com.eusan.eusanapp.Service.DiagnosisService;
import com.eusan.eusanapp.Service.AdminService;
import com.eusan.eusanapp.Service.AdmissionService;
import com.eusan.eusanapp.Service.EmailService;
import com.eusan.eusanapp.Service.QueueService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import jakarta.mail.MessagingException;

import com.eusan.eusanapp.Service.LabResultService;
import com.eusan.eusanapp.Repository.TriageRepository;
import com.eusan.eusanapp.Repository.PrescriptionRepository;
import com.eusan.eusanapp.Repository.DiagnosisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DentistController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private LabResultService labResultService;

    @Autowired
    private AdmissionService admissionService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DiagnosisService diagnosisService;
    @Autowired
    private TriageRepository triageRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @GetMapping("/dentist/cases")
    public String dentistCases() {
        return "fragments/dentist_int :: content"; // Main dashboard
    }

    @GetMapping("/dentist/results")
    public String dentistResults() {
        return "fragments/dental_results_int :: content"; // Main dashboard
    }
    @GetMapping("/dentist/pendingTriagedQueue/dentist") 
public String viewDentistQueue(Model model) {
    List<PatientQueue> queueList = queueService.getPendingAndTriagedPatientsBySpecification("Dentist");
    model.addAttribute("queueList", queueList);
    return "fragments/patientQueue :: content";
}
@GetMapping("/dentist/labResults/dentist")
    public String viewDentistLabResults(Model model) {
        List<LabResult> labResults = labResultService.getLabResultsForDentist();
        model.addAttribute("labResults", labResults);
        return "fragments/doctor_view_lab_results :: content";
    }
    @GetMapping("/dentist/dashboard")
public String dentistDashboard(Model model, @AuthenticationPrincipal StaffUserDetails loggedInUser) {
    Staff staff = adminService.getByWorkId(loggedInUser.getUsername());
    model.addAttribute("staff", staff);
    return "dentist_dashboard";
}

     
}
