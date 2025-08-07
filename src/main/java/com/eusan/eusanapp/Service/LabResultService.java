package com.eusan.eusanapp.Service;

import com.eusan.eusanapp.Entity.Diagnosis;
import com.eusan.eusanapp.Entity.LabResult;
import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Entity.Status;
import com.eusan.eusanapp.Repository.DiagnosisRepository;
import com.eusan.eusanapp.Repository.LabResultRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LabResultService {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    @Autowired
    private LabResultRepository labResultRepository;

    // Fetch only pending lab tests
    
    public List<LabResult> getLabResultsSortedByDate() {
        return labResultRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    // Fetch a specific diagnosis by ID
    
        public Diagnosis getDiagnosisById(String diagnosisId) {
            return diagnosisRepository.findById(diagnosisId)
                    .orElseThrow(() -> new RuntimeException("Diagnosis not found for ID: " + diagnosisId));
        }
    

    // Save lab result and mark the test as completed
    public void saveLabResult(LabResult labResult) {
        labResultRepository.save(labResult);

        // Mark diagnosis as completed
        Diagnosis diagnosis = labResult.getDiagnosis();
        diagnosis.setCompleted(true);
        diagnosisRepository.save(diagnosis);
    }
     // ✅ Get all lab results
    //  public List<LabResult> getTestLabResults() {
    //     return labResultRepository.findLabResultsByQueueStatus(Status.LAB_TEST);
    // }
    // public List<LabResult> getAllLabResults() {
    //     return labResultRepository.findAll();
    // }
    public List<LabResult> getLabResultsForGeneralConsultation() {
        return labResultRepository.findLabResultsForGeneralConsultation(Status.LAB_TEST);
    }

    public List<LabResult> getLabResultsForVCT() {
        return labResultRepository.findLabResultsForVCT(Status.LAB_TEST);
    }

    public List<LabResult> getLabResultsForDentist() {
        return labResultRepository.findLabResultsForDentist(Status.LAB_TEST);
    }

    public List<LabResult> getLabResultsForCMO() {
        return labResultRepository.findLabResultsForCMO(Status.LAB_TEST);
    }
    

    // ✅ Get lab results for a specific patient
    public List<LabResult> getLabResultsByPatient(Long patientId) {
        return labResultRepository.findByPatientId(patientId);
    }
    public List<LabResult> getLabResultsByPatientName(String keyword) {
        return labResultRepository.findByPatientName(keyword);
    }
}
