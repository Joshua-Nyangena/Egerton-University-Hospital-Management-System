package com.eusan.eusanapp.Service;

import com.eusan.eusanapp.Entity.Prescription;
import com.eusan.eusanapp.Entity.Diagnosis;
import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Entity.Status;
import com.eusan.eusanapp.Repository.PrescriptionRepository;
import com.eusan.eusanapp.Repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
private QueueRepository patientQueueRepository;


    // Retrieve all prescriptions
    public List<Prescription> getCompletedQueuePrescriptions() {
        return prescriptionRepository.findPrescriptionsByQueueStatus(Status.COMPLETED);
    }
 // ✅ Add method to get prescriptions sorted by date
 public List<Prescription> getPrescriptionsSortedByDate() {
    return prescriptionRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
}    
public List<Prescription> getAllPrescriptions() {
    return prescriptionRepository.findAll();
}
public List<Prescription> searchPrescriptions(String keyword) {
    return prescriptionRepository.searchPrescriptionsByPatientNameOrId(keyword);
}

    // Issue Medication
    public boolean issueMedication(Long prescriptionId) {
        Optional<Prescription> prescriptionOpt = prescriptionRepository.findById(prescriptionId);
        
        if (prescriptionOpt.isPresent()) {
            Prescription prescription = prescriptionOpt.get();
            
            // Retrieve the associated diagnosis
            Diagnosis diagnosis = prescription.getDiagnosis();
            
            if (diagnosis != null) {
                // Retrieve the associated patient queue entry
                PatientQueue patientQueue = diagnosis.getPatientQueue();
                
                if (patientQueue != null) {
                    // Update the queue status to SERVED instead of deleting the prescription
                    patientQueue.setStatus(Status.SERVED);
                    patientQueueRepository.save(patientQueue); // Save the updated queue status
                }
            }
            
            return true; // Indicate success
        }
        
        return false; // Prescription not found
    }
}    
