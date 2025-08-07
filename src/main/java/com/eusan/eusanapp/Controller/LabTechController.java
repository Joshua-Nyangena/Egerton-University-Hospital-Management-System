package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Config.StaffUserDetails;
import com.eusan.eusanapp.Entity.Diagnosis;
import com.eusan.eusanapp.Entity.LabResult;
import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Entity.Prescription;
import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Entity.Status;
import com.eusan.eusanapp.Entity.Triage;
import com.eusan.eusanapp.Service.AdminService;
import com.eusan.eusanapp.Service.DiagnosisService;
import com.eusan.eusanapp.Service.QueueService;
import com.eusan.eusanapp.Service.LabResultService;
import com.eusan.eusanapp.Repository.TriageRepository;
import com.eusan.eusanapp.Repository.PrescriptionRepository;
import com.eusan.eusanapp.Repository.DiagnosisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;


@Controller
@RequestMapping("/lab")
public class LabTechController {

    @Autowired
    private LabResultService labResultService;
    @Autowired
    private DiagnosisService diagnosisService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
public String labTechDashboard(Model model, @AuthenticationPrincipal StaffUserDetails loggedInUser) {
    Staff staff = adminService.getByWorkId(loggedInUser.getUsername());
    model.addAttribute("staff", staff);
    return "lab_technician_dashboard";
}


    // View only pending lab tests
    // @GetMapping("/tests")
    // public String viewPendingLabTests(@RequestParam(defaultValue = "pending") String sortType, Model model) {
    //     System.out.println("Received sortType: " + sortType); // Debugging
    
    //     List<Diagnosis> labTests;
    
    //     switch (sortType.toLowerCase()) { // Case-insensitive comparison
    //         case "date":
    //             System.out.println("Sorting by date...");
    //             labTests = diagnosisService.getLabTestsSortedByDate();
    //             break;
    //         case "all":
    //             System.out.println("Fetching all lab tests...");
    //             labTests = diagnosisService.getAllLabTests();
    //             break;
    //         case "pending":
    //         default: // Fallback for unexpected values
    //             System.out.println("Fetching pending lab tests...");
    //             labTests = diagnosisService.getAllPendingLabTests();
    //             break;
    //     }
    
    //     model.addAttribute("labTests", labTests);
    //     model.addAttribute("sortType", sortType);
    //     return "fragments/lab_test_queue :: content"; // Ensure this fragment updates properly
    // }
    




    // Open the lab result form
    @GetMapping("/result/{diagnosisId}")
    public String showLabResultForm(@PathVariable String diagnosisId, Model model) {
        Diagnosis diagnosis = labResultService.getDiagnosisById(diagnosisId);
        model.addAttribute("diagnosis", diagnosis);
        model.addAttribute("labResult", new LabResult());
        return "fragments/lab_result_form ::content"; // lab_result_form.html (Thymeleaf template)
    }

    // Save lab result and mark as completed
    @PostMapping("/saveResult")
    public String saveLabResult(@ModelAttribute LabResult labResult, @RequestParam String diagnosisId) {
        Diagnosis diagnosis = labResultService.getDiagnosisById(diagnosisId);
        labResult.setDiagnosis(diagnosis);
        labResultService.saveLabResult(labResult);
        return "redirect:/lab/dashboard"; // Redirect back to pending tests list
    }
    @GetMapping("/tests/patient")
public String viewLabResultsForPatient(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "pending") String sortType,
        Model model) {

    List<Diagnosis> diagnoses;

    if (keyword != null && !keyword.trim().isEmpty()) {
        switch (sortType.toLowerCase()) {
            case "date":
                diagnoses = diagnosisService.searchTestsByPatientNameSortedByDate(keyword);
                break;
            case "all":
                diagnoses = diagnosisService.searchAllTestsByPatientName(keyword);
                break;
            case "pending":
            default:
                diagnoses = diagnosisService.searchPendingTestsByPatientName(keyword);
                break;
        }
    } else {
        // If no search keyword, fallback to default sorting
        switch (sortType.toLowerCase()) {
            case "date":
                diagnoses = diagnosisService.getLabTestsSortedByDate();
                break;
            case "all":
                diagnoses = diagnosisService.getAllLabTests();
                break;
            case "pending":
            default:
                diagnoses = diagnosisService.getAllPendingLabTests();
                break;
        }
    }

    model.addAttribute("labTests", diagnoses);
    model.addAttribute("keyword", keyword);
    model.addAttribute("sortType", sortType);
    return "fragments/lab_test_queue :: content";
}
}