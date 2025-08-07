package com.eusan.eusanapp.Controller;

import com.eusan.eusanapp.Config.StaffUserDetails;
import com.eusan.eusanapp.Entity.Diagnosis;
import com.eusan.eusanapp.Entity.LabResult;
import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Entity.Prescription;
import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Entity.Status;
import com.eusan.eusanapp.Entity.Triage;
import com.eusan.eusanapp.Entity.Admissions;
import com.eusan.eusanapp.Service.DiagnosisService;
import com.eusan.eusanapp.Service.AdmissionService;
import com.eusan.eusanapp.Service.EmailService;
import com.eusan.eusanapp.Service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.mail.MessagingException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.eusan.eusanapp.Service.LabResultService;
import com.eusan.eusanapp.Service.AdminService;
import com.eusan.eusanapp.Repository.TriageRepository;
import com.eusan.eusanapp.Repository.PrescriptionRepository;
import com.eusan.eusanapp.Repository.DiagnosisRepository;
import com.eusan.eusanapp.Repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class DoctorController {
    private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);


    @Autowired
    private QueueService queueService;

    @Autowired
    private LabResultService labResultService;

    @Autowired
    private AdmissionService admissionService;

    @Autowired
    private EmailService emailService;
    @Autowired
    private AdminService adminService;

    @Autowired
    private DiagnosisService diagnosisService;
    @Autowired
    private TriageRepository triageRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private DiagnosisRepository diagnosisRepository;
    @Autowired
    private QueueRepository queueRepository;

    @GetMapping("/doctor/dashboard")
public String doctorDashboard(Model model, @AuthenticationPrincipal StaffUserDetails loggedInUser) {
    Staff staff = adminService.getByWorkId(loggedInUser.getUsername());
    model.addAttribute("staff", staff);
    return "doctor_dashboard";
}

@GetMapping("/cmo/dashboard")
public String cmoDashboard(Model model, @AuthenticationPrincipal StaffUserDetails loggedInUser) {
    Staff staff = adminService.getByWorkId(loggedInUser.getUsername());
    model.addAttribute("staff", staff);
    return "cmo_dashboard";
}

    @GetMapping("/vct/dashboard")
public String vctDashboard(Model model, @AuthenticationPrincipal StaffUserDetails loggedInUser) {
    Staff staff = adminService.getByWorkId(loggedInUser.getUsername());
    model.addAttribute("staff", staff);
    return "infectious_unit";
}

    @GetMapping("/doctor/patientQueue")
    public String patientQueue() {
        return "fragments/patientqueueint :: content"; // Main dashboard
    }
    @GetMapping("/doctor/lab-results")
    public String labResults() {
        return "fragments/labResultsint :: content"; // Main dashboard
    }
    
    @GetMapping("/cmo/cases")
    public String cmoCases() {
        return "fragments/cmo_int :: content"; // Main dashboard
    }
    @GetMapping("/vct/cases")
    public String vctCases() {
        return "fragments/infectious_cases :: content"; // Main dashboard
    }
    @GetMapping("/vct/results")
    public String vctResults() {
        return "fragments/vct_results_int :: content"; // Main dashboard
    }
    
    // Load Doctor Dashboard with Pending and Triaged Patients
    // @GetMapping("/doctor/patientQueue")
    // public String viewPatientQueue(@RequestParam(defaultValue = "pending") String sortType, Model model) {
    //     List<PatientQueue> queueList;

    //     if ("date".equals(sortType)) {
    //         queueList = queueService.getPatientsSortedByDate();
    //     } else if ("all".equals(sortType)) {
    //         queueList = queueService.getAllPatients();
    //     } else {
    //         queueList = queueService.getPendingAndTriagedPatients();
    //     }

    //     model.addAttribute("queueList", queueList);
    //     return "fragments/patientQueue :: content";
    // }
    // ✅ View All Patients in the Queue
    // Full All Patients Queue
@GetMapping("/doctor/allPatientsQueue")
public String viewAllPatientsQueue(Model model) {
    List<PatientQueue> queueList = queueService.getAllPatients();

    model.addAttribute("queueList", queueList);
    model.addAttribute("keyword", null);
    model.addAttribute("getAll", true); // Used to switch action path in form

    return "fragments/patientQueue :: content";
}

// Search All Patients Queue
@GetMapping("/doctor/searchAllPatientsQueue")
public String searchAllPatientsQueue(@RequestParam("keyword") String keyword, Model model) {
    List<PatientQueue> queueList = queueService.searchAllQueuedPatients(keyword);

    model.addAttribute("queueList", queueList);
    model.addAttribute("keyword", keyword);
    model.addAttribute("getAll", true);

    return "fragments/patientQueue :: content";
}

// Full Pending & Triaged Queue 
@GetMapping("/doctor/pendingTriagedQueue")
public String viewPendingAndTriagedQueue(Model model) {
    List<PatientQueue> queueList = queueService.getPendingAndTriagedPatients();

    model.addAttribute("queueList", queueList);
    model.addAttribute("keyword", null);
    model.addAttribute("getAll", false); // Used to switch action path in form

    return "fragments/patientQueue :: content";
}

// Search Pending & Triaged Queue
@GetMapping("/doctor/searchPendingTriagedQueue")
public String searchPendingAndTriagedQueue(@RequestParam("keyword") String keyword, Model model) {
    List<PatientQueue> queueList = queueService.searchPendingAndTriagedPatients(keyword);

    model.addAttribute("queueList", queueList);
    model.addAttribute("keyword", keyword);
    model.addAttribute("getAll", false);

    return "fragments/patientQueue :: content";
}

    



@GetMapping("/cmo/pendingTriagedQueue/cmo") 
public String viewCMOQueue(Model model) {
    List<PatientQueue> queueList = queueService.getPendingAndTriagedPatientsBySpecification("CMO");
    model.addAttribute("queueList", queueList);
    return "fragments/cmopatientQueue :: content";
}

@GetMapping("/vct/pendingTriagedQueue/vct") 
public String viewInfectiousDoctorQueue(Model model) {
    List<PatientQueue> queueList = queueService.getPendingAndTriagedPatientsBySpecification("VCT");
    model.addAttribute("queueList", queueList);
    return "fragments/patientQueue :: content";
}


    @GetMapping("/doctor/labTest/{queueId}")
    public String showLabTestForm(@PathVariable String queueId, Model model) {
        PatientQueue queue = queueService.getQueueById(queueId);
        model.addAttribute("queue", queue);
        return "fragments/lab_test_form :: content";
    }

    @PostMapping("/doctor/saveLabTest")
public String saveLabTest(@RequestParam String queueId, @RequestParam String labTest, RedirectAttributes redirectAttributes) {
    queueService.saveLabTest(queueId, labTest);
    queueService.updateQueueStatus(queueId, "LAB_TEST"); // Safe now with ADMITTED check

    redirectAttributes.addFlashAttribute("successMessage", "Lab test request submitted successfully!");
    return "fragments/patientQueue :: content";
}



@GetMapping("/doctor/admittedPatients")
    public String showAdmittedPatients(Model model) {
        model.addAttribute("admittedPatients", admissionService.getAdmittedPatients());
        return "fragments/admitted_patients :: content";
    }


    @GetMapping("/doctor/prescription/{queueId}")
public String showPrescriptionForm(@PathVariable String queueId, Model model) {
    PatientQueue queue = queueService.getQueueById(queueId);
    
    // Check if a diagnosis already exists for the patient
    String diagnosisId = diagnosisService.getDiagnosisIdByQueueId(queueId);

    // If no diagnosis exists, generate a new one (for direct pharmacy cases)
    if (diagnosisId == null) {
        diagnosisId = diagnosisService.createDiagnosis(queue);
    }

    model.addAttribute("queue", queue);
    model.addAttribute("diagnosisId", diagnosisId);
    return "fragments/prescription_form :: content";
}

@GetMapping("/doctor/admitForm/{queueId}")
public String showAdmitForm(@PathVariable String queueId, Model model, @AuthenticationPrincipal StaffUserDetails loggedInUser){
    logger.debug("Handling admit form for queueId: {}", queueId);
    logger.debug("Logged-in user email: {}", loggedInUser.getUsername());

    Staff doctor = adminService.getByWorkId(loggedInUser.getUsername()); // ✅ correct lookup

    
    if (doctor == null) {
        logger.error("No doctor found with email: {}", loggedInUser.getUsername());
        throw new RuntimeException("Doctor not found");
    }

    PatientQueue queue = queueService.getQueueById(queueId);

    model.addAttribute("queue", queue);
    model.addAttribute("doctorName", doctor.getName());
    model.addAttribute("doctorWorkId", doctor.getWorkId());

    return "fragments/admit_form :: admittedFragment";
}
@GetMapping("/doctor/discharge/{queueId}")
    public String dischargePatient(@PathVariable String queueId) {
       queueService.dischargePatient(queueId);
        // Redirect or return appropriate view
        return "redirect:/doctor/dashboard"; // or wherever you want to return
    }



    @PostMapping("/doctor/saveAdmit")
public String saveAdmit(@RequestParam String queueId, 
                        @RequestParam String ward, 
                        @RequestParam String admissionReason, 
                        @RequestParam String guardianEmail, 
                        @RequestParam String doctorName, 
                        @RequestParam String doctorWorkId, 
                        RedirectAttributes redirectAttributes) {

    PatientQueue queue = queueService.findById(queueId);
    if (queue == null) {
        redirectAttributes.addFlashAttribute("error", "Invalid Queue ID");
        return "redirect:/doctor/admitPatient?queueId=" + queueId;
    }

    // Save admission details
    Admissions admission = new Admissions();
    admission.setPatientQueue(queue);
    admission.setWard(ward);
    admission.setAdmissionReason(admissionReason);
    admission.setGuardianEmail(guardianEmail);
    admission.setAdmissionDate(LocalDateTime.now());
    admission.setDoctorName(doctorName);  // Add the doctor's name
    admission.setDoctorWorkId(doctorWorkId);  // Add the doctor's work ID
    admissionService.save(admission);

    // Update queue status
    queue.setStatus(Status.ADMITTED); // Correct ENUM usage
    queueService.save(queue);

    // Send email notification to guardian
    try {
        emailService.sendEmail(guardianEmail, 
            "Patient Admission Notice", 
            "Dear Guardian, your patient has been admitted to " + ward + ".\n\nReason: " + admissionReason);
    } catch (MessagingException e) {
        // Log error and notify the user
        e.printStackTrace(); // Log error (use a logger in production)
        redirectAttributes.addFlashAttribute("error", "Patient admitted, but email could not be sent.");
    }
    redirectAttributes.addFlashAttribute("success", "Patient admitted successfully.");
    return "redirect:/doctor/dashboard";
}


@PostMapping("/doctor/savePrescription")
    public String savePrescription(@RequestParam String queueId, 
                                   @RequestParam String diagnosisId, 
                                   @RequestParam String prescriptionDetails) {
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
                                .orElseThrow(() -> new RuntimeException("Diagnosis not found"));

        Prescription newPrescription = new Prescription();
        newPrescription.setDiagnosis(diagnosis);
        newPrescription.setPrescriptionDetails(prescriptionDetails);
        prescriptionRepository.save(newPrescription);

        // Update queue status to COMPLETED
        queueService.updateQueueStatus(queueId, "COMPLETED");

        return "fragments/patientQueue :: content";
    }




     // View Patient Medical History
     @GetMapping("/doctor/viewMedicalHistory/{patientId}") 
public String viewMedicalHistory(@PathVariable Long patientId, Model model) {
    List<Diagnosis> diagnoses = diagnosisRepository.findAll()
            .stream()
            .filter(d -> d.getPatientQueue().getPatient().getId().equals(patientId))
            .toList();

    List<Prescription> prescriptions = prescriptionRepository.findByDiagnosis_PatientQueue_Patient_Id(patientId);

    List<LabResult> labResults = labResultService.getLabResultsByPatient(patientId);

    model.addAttribute("diagnoses", diagnoses);
    model.addAttribute("prescriptions", prescriptions);
    model.addAttribute("labResults", labResults);
    
    return "fragments/view_medical_history :: content";
}
 



     // ✅ View all lab results
    //  @GetMapping("/doctor/lab-results")
    //  public String viewAllLabResults(@RequestParam(defaultValue = "pending") String sortType, Model model) {
    //      List<LabResult> labResults;
         
    //      if ("date".equals(sortType)) {
    //          labResults = labResultService.getLabResultsSortedByDate();
    //      }  else if ("all".equals(sortType)) {
    //         labResults = labResultService.getAllLabResults();
    //        }  else {
    //          labResults = labResultService.getTestLabResults();
    //      }
         
    //      model.addAttribute("labResults", labResults);
    //      return "fragments/doctor_view_lab_results :: content";
    //  }
//      @GetMapping("/doctor/allLabResults")
// public String viewAllLabResults(Model model) {
//     List<LabResult> labResults = labResultService.getAllLabResults();
//     model.addAttribute("labResults", labResults);
//     return "fragments/doctor_view_lab_results :: content";
// }
// @GetMapping("/doctor/pendingLabResults")
// public String viewPendingLabResults(Model model) {
//     List<LabResult> labResults = labResultService.getTestLabResults();
//     model.addAttribute("labResults", labResults);
//     return "fragments/doctor_view_lab_results :: content";
// }
@GetMapping("/doctor/labResults/general")
    public String viewGeneralConsultationLabResults(Model model) {
        List<LabResult> labResults = labResultService.getLabResultsForGeneralConsultation();
        model.addAttribute("labResults", labResults);
        return "fragments/doctor_view_lab_results :: content";
    }

    // View lab results for VCT
    @GetMapping("/vct/labResults/vct")
    public String viewVCTLabResults(Model model) {
        List<LabResult> labResults = labResultService.getLabResultsForVCT();
        model.addAttribute("labResults", labResults);
        return "fragments/doctor_view_lab_results :: content";
    }

    // View lab results for Dentist
    

    // View lab results for CMO
    @GetMapping("/cmo/labResults/cmo")
    public String viewCMOLabResults(Model model) {
        List<LabResult> labResults = labResultService.getLabResultsForCMO();
        model.addAttribute("labResults", labResults);
        return "fragments/doctor_view_lab_results :: content";
    }
 
     // ✅ View lab results for a specific patient
     @GetMapping("/doctor/lab-results/patient")
public String viewLabResultsForPatient(@RequestParam String keyword, Model model) {
    List<LabResult> labResults = labResultService.getLabResultsByPatientName(keyword);
    model.addAttribute("labResults", labResults);
    model.addAttribute("keyword", keyword); // Add keyword to the model for the search input
    return "fragments/doctor_view_lab_results :: content";
}
@GetMapping("/doctor/view/{queueId}")
public String viewTriage(@PathVariable String queueId, Model model, RedirectAttributes redirectAttributes) {
    // Retrieve triage details using correct method
    Triage triage = triageRepository.findByQueue_QueueId(queueId);

    if (triage == null) {
        redirectAttributes.addFlashAttribute("errorMessage", "Triage details not found for this patient.");
        return "redirect:/doctor/patientQueue";
    }

    model.addAttribute("triage", triage);
    return "fragments/view_triage :: content"; // Thymeleaf fragment
}
@PostMapping("/cmo/acceptQueue/{queueId}")
  @ResponseBody // Ensure response is returned as plain text
public String acceptQueue(@PathVariable String queueId) {
    Optional<PatientQueue> optionalQueue = queueRepository.findById(queueId);

    if (optionalQueue.isPresent()) {
        PatientQueue queue = optionalQueue.get();
        queue.setStatus(Status.ACCEPTED); // Update status to ACCEPTED
        queueRepository.save(queue);
        return "Success"; // Return success message
    }
    return "Error"; // Return error if queue not found
}


@GetMapping("/vct/admittedPatients")
public String viewVCTAdmittedPatients(Model model) {
    List<Admissions> admittedPatients = admissionService.getAdmittedPatientsBySpecification("VCT");
    model.addAttribute("admittedPatients", admittedPatients);
    return "fragments/admitted_patients :: content";
}


@GetMapping("/dentist/admittedPatients")
public String viewDentistAdmittedPatients(Model model) {
    List<Admissions> admittedPatients = admissionService.getAdmittedPatientsBySpecification("Dentist");
    model.addAttribute("admittedPatients", admittedPatients);
    return "fragments/admitted_patients :: content";
}

@GetMapping("/doctor/general/admittedPatients")
public String viewGeneralConsultationAdmittedPatients(Model model) {
    List<Admissions> admittedPatients = admissionService.getAdmittedPatientsBySpecification("General Consultation");
    model.addAttribute("admittedPatients", admittedPatients);
    return "fragments/admitted_patients :: content";
}

   


}
