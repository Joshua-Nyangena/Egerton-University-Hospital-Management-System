package com.eusan.eusanapp.Service;

import com.eusan.eusanapp.Entity.Diagnosis;
import com.eusan.eusanapp.Entity.LabResult;
import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Entity.Status;

import com.eusan.eusanapp.Repository.DiagnosisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiagnosisService {

    @Autowired
    private DiagnosisRepository diagnosisRepository;

    public String getDiagnosisIdByQueueId(String queueId) {
        Optional<Diagnosis> diagnosis = diagnosisRepository.findByPatientQueue_QueueId(queueId);
        return diagnosis.map(d -> String.valueOf(d.getDiagnosisId())).orElse(null);
    }
    public String createDiagnosis(PatientQueue queue) {
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setPatientQueue(queue);
        diagnosis.setLabTest(null); // No lab test since it's a direct prescription
        diagnosis.setCompleted(true); // Mark as completed since it's a direct prescription
        diagnosisRepository.save(diagnosis);
        return diagnosis.getDiagnosisId(); // Return the generated ID
    }
    public List<Diagnosis> getLabTestsSortedByDate() {
        System.out.println("Sorting by date DESC..."); // Debugging
        return diagnosisRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }

    public List<Diagnosis> getAllLabTests() {
        System.out.println("Fetching all lab tests..."); // Debugging
        return diagnosisRepository.findAll();
    }
    public List<Diagnosis> searchPendingTestsByPatientName(String keyword) {
        return diagnosisRepository.searchPendingTestsByPatientName(keyword, Status.LAB_TEST);
    }

    public List<Diagnosis> searchTestsByPatientNameSortedByDate(String keyword) {
        return diagnosisRepository.searchTestsByPatientNameSortedByDate(keyword);
    }

    public List<Diagnosis> searchAllTestsByPatientName(String keyword) {
        return diagnosisRepository.searchAllTestsByPatientName(keyword);
    }
    
    public List<Diagnosis> getAllPendingLabTests() {
        System.out.println("Fetching only pending lab tests..."); // Debugging
        return diagnosisRepository.findByCompletedFalse();
    }
    public List<Diagnosis> getTestsByPatientName(String keyword) {
        return diagnosisRepository.findByPatientName(keyword);
    }
}
