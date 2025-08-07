package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Config.StaffUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import com.eusan.eusanapp.Entity.Patient;
import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Service.PatientService;
import com.eusan.eusanapp.Service.AdminService;
import com.eusan.eusanapp.Service.QueueService;

import jakarta.validation.Valid;

import com.eusan.eusanapp.Repository.PatientRepository;
import org.springframework.http.ResponseEntity;
import java.util.Map;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/receptionist")
public class ReceptionistController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AdminService adminService;

    // Receptionist Dashboard
    @GetMapping("/dashboard")
public String receptionistDashboard(Model model, @AuthenticationPrincipal StaffUserDetails loggedInUser) {
    Staff staff = adminService.getByWorkId(loggedInUser.getUsername());
    model.addAttribute("staff", staff);
    return "receptionist";
}

    @GetMapping("/patientsInterface")
    public String managePatients() {
        return "fragments/receptionint :: content"; 
    }

    // List Patients (Only Active Patients)
    // @GetMapping("/patients") 
    // public String viewPatients(
    //         @RequestParam(defaultValue = "all") String sortType,
    //         @RequestParam(required = false) String keyword,
    //         @RequestParam(required = false, defaultValue = "false") boolean getAll,
    //         Model model) {
    
    //     List<Patient> patients;
    
    //     if (getAll) {
    //         // Fetch all patients (active + inactive)
    //         patients = patientService.getAllPatients();  
    //     } else if ("date".equals(sortType)) {
    //         // Sort active patients by date
    //         patients = patientService.getPatientsSortedByDate();
    //     } else {
    //         // Search active patients if keyword is provided, otherwise get all active patients
    //         patients = (keyword != null && !keyword.isEmpty()) 
    //                 ? patientService.searchActivePatients(keyword) 
    //                 : patientService.getActivePatients();
    //     }
    
    //     model.addAttribute("patients", patients);
    //     model.addAttribute("sortType", sortType);
    //     model.addAttribute("keyword", keyword);
    //     model.addAttribute("getAll", getAll);
    
    //     return "fragments/patients :: content";  // Ensure this Thymeleaf fragment exists
    // }
    // List Active Patients Only
@GetMapping("/activePatients")
public String viewActivePatients(
        @RequestParam(required = false) String keyword,
        Model model) {

    List<Patient> patients = (keyword != null && !keyword.isEmpty()) 
            ? patientService.searchActivePatients(keyword) 
            : patientService.getActivePatients(); 

    model.addAttribute("patients", patients);
    model.addAttribute("keyword", keyword);

    return "fragments/patients :: content";  // Ensure this fragment exists
}

// List All Patients (Active + Inactive)
@GetMapping("/allPatients")
public String viewAllPatients(
        @RequestParam(required = false) String keyword,
        Model model) {

    List<Patient> patients = (keyword != null && !keyword.isEmpty()) 
            ? patientService.searchAllPatients(keyword) 
            : patientService.getAllPatients();

    model.addAttribute("patients", patients);
    model.addAttribute("keyword", keyword);

    return "fragments/patients :: content";  // Ensure this fragment exists
}

    


    // Add New Patient Form
    @GetMapping("/addPatient")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "fragments/add_patient :: content";
    }

    // Save New Patient with Flash Message
    @PostMapping("/savePatient")
public String savePatient(@Valid @ModelAttribute("patient") Patient patient, 
                          BindingResult bindingResult, 
                          Model model) {
    if (bindingResult.hasErrors()) {
        // Return the patient registration form fragment dynamically within the dashboard
        model.addAttribute("patient", patient);
        return "fragments/add_patient :: content"; // Return only the form fragment
    }

    // If no errors, save the patient and add to queue
    patientService.savePatient(patient);
    //queueService.addToQueue(patient);

    return "redirect:/receptionist/activePatients";
}

    

    // Edit Patient Form
    @GetMapping("/editPatient/{id}")
    public String editPatientForm(@PathVariable Long id, Model model) {
        Optional<Patient> patient = patientService.getPatientById(id);
        patient.ifPresent(value -> model.addAttribute("patient", value));
        return "fragments/edit_patient :: content";
    }

    // Update Patient with Flash Message
    @PostMapping("/updatePatient")
public String updatePatient(@Valid @ModelAttribute("patient") Patient patient, 
                            BindingResult bindingResult, 
                            Model model) {
    if (bindingResult.hasErrors()) {
        // Return the patient update form fragment dynamically within the dashboard
        model.addAttribute("patient", patient);
        return "fragments/edit_patient :: content"; // Return only the form fragment
    }

    // If no errors, update the patient
    patientService.savePatient(patient);

    return "redirect:/receptionist/activePatients";
}


    // Deactivate (Delete) Patient with Flash Message
    @GetMapping("/deactivatePatient/{id}")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            patientService.deactivatePatient(id);
            redirectAttributes.addFlashAttribute("successMessage", "Patient deactivated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to deactivate patient.");
        }
        return "redirect:/receptionist/dashboard";
    }

    // Add Existing Patient to Queue with Flash Message
    @PostMapping("/addToQueue/{id}")
public ResponseEntity<?> addToQueue(@PathVariable Long id, 
                               @RequestParam("specification") String specification) {
    Optional<Patient> patient = patientService.getPatientById(id);
    
    if (patient.isPresent()) {
        queueService.addToQueue(patient.get(), specification);
        return ResponseEntity.ok().body(Map.of(
            "success", true,
            "message", "Patient added to queue successfully for " + specification + "!"
        ));
    } else {
        return ResponseEntity.badRequest().body(Map.of(
            "success", false,
            "message", "Failed to add patient to queue."
        ));
    }
}


    // Receptionist Profile
    @GetMapping("/profile")
    public String receptionistProfile(Model model) {
        model.addAttribute("workId", "REC-12345");
        model.addAttribute("contact", "0712345678");
        model.addAttribute("email", "receptionist@hospital.com");
        model.addAttribute("name", "Jane Doe");
        return "fragments/profile :: content";
    }
}
