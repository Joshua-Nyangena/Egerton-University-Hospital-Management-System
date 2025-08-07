package com.eusan.eusanapp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eusan.eusanapp.Entity.Diagnosis;
import com.eusan.eusanapp.Entity.Prescription;
import com.eusan.eusanapp.Entity.Patient;
import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Repository.QueueRepository;
import com.eusan.eusanapp.Repository.DiagnosisRepository;
import com.eusan.eusanapp.Repository.PrescriptionRepository;
import com.eusan.eusanapp.Entity.Status;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

@Service
public class QueueService {

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private DiagnosisRepository diagnosisRepository;
    @Autowired
    private PrescriptionRepository prescriptionRepository;

    // Add a patient to the queue with "PENDING" status
    public void addToQueue(Patient patient, String specification) {
        PatientQueue queue = new PatientQueue();
        queue.setPatient(patient);
        queue.setStatus(Status.PENDING);
        queue.setSpecification(specification); // Ensure status is set
        queueRepository.save(queue);
    }

    // Get all patients in the queue
    public List<PatientQueue> getAllQueueEntries() {
        return queueRepository.findAll();
    }

    public List<PatientQueue> getPendingAndTriagedPatients() {
        return queueRepository.findByStatusInAndSpecification(
            List.of(Status.PENDING, Status.TRIAGED), "General Consultation"
        );
    }
    
    public List<PatientQueue> getPendingAndTriagedPatientsBySpecification(String specification) {
        return queueRepository.findByStatusInAndSpecification(
            List.of(Status.PENDING, Status.TRIAGED), specification
        );
    }
    
    
    
    // Fetch only patients who are "PENDING" (Waiting for triage)
    public List<PatientQueue> getPendingPatients() {
        return queueRepository.findByStatus(Status.PENDING);
    }

    // Retrieve a specific queue entry by queueId
    public PatientQueue getQueueById(String queueId) {
        Optional<PatientQueue> queueEntry = queueRepository.findById(queueId);
        return queueEntry.orElse(null);
    }
    public List<PatientQueue> getPatientsSortedByDate() {
        return queueRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }
    public List<PatientQueue> getAllPatients() {
        return queueRepository.findAll();
    }

    // Update patient status after triage completion
    // Update patient status after triage completion
public void updateQueueStatus(String queueId, String status) {
    Optional<PatientQueue> queueEntry = queueRepository.findById(queueId);
    if (queueEntry.isPresent()) {
        PatientQueue queue = queueEntry.get();
        Status currentStatus = queue.getStatus();
        
        // Only update if current status is NOT ADMITTED
        if (currentStatus != Status.ADMITTED) {
            queue.setStatus(Status.valueOf(status));
            queueRepository.save(queue);
        }
    }
}

public void dischargePatient(String queueId) {
    Optional<PatientQueue> optionalQueue = queueRepository.findById(queueId);
    if (optionalQueue.isPresent()) {
        PatientQueue queue = optionalQueue.get();
        queue.setStatus(Status.DISCHARGED); // or Status.SERVED if you prefer
        queueRepository.save(queue);
    }
}
    

    public void saveLabTest(String queueId, String labTest) {
        // Fetch the queue entry
        PatientQueue queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new RuntimeException("Queue not found for ID: " + queueId));
    
        // Only update status if current status is NOT ADMITTED
        if (queue.getStatus() != Status.ADMITTED) {
            queue.setStatus(Status.LAB_TEST);
            queueRepository.save(queue);
        }
    
        // Create and save a new diagnosis
        Diagnosis diagnosis = new Diagnosis();
        diagnosis.setDiagnosisId("D-" + UUID.randomUUID().toString().substring(0, 8));
        diagnosis.setPatientQueue(queue);
        diagnosis.setLabTest(labTest);
        diagnosisRepository.save(diagnosis);
    }
    
    public List<PatientQueue> searchAllQueuedPatients(String keyword) {
        return queueRepository.searchAllQueuedPatients(keyword);
    }
    
    public List<PatientQueue> searchPendingAndTriagedPatients(String keyword) {
        return queueRepository.searchPendingAndTriagedPatients(keyword);
    } 
    
    
    public List<PatientQueue> searchAllPatientQueueByNameOrRegNo(String keyword) {
        return queueRepository.findByPatient_NameContainingIgnoreCaseOrPatient_RegNoContainingIgnoreCase(keyword, keyword);
    }
    
    public List<PatientQueue> searchPendingAndTriagedQueue(String keyword) {
        return queueRepository.findByStatusInAndPatient_NameContainingIgnoreCaseOrPatient_RegNoContainingIgnoreCase(
            List.of(Status.PENDING, Status.TRIAGED), keyword, keyword
        );
    }
    

    public void admitPatient(String queueId) {
        PatientQueue queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new RuntimeException("Queue not found"));
        queue.setStatus(Status.ADMITTED);
        queueRepository.save(queue);
    }
    public PatientQueue findById(String queueId) {
        Optional<PatientQueue> queue = queueRepository.findById(queueId);
        return queue.orElse(null);
    }
    

    public void save(PatientQueue queue) {
        queueRepository.save(queue);
    }


    public void savePrescription(String queueId, String diagnosisId, String prescription) {
        // Retrieve PatientQueue
        PatientQueue queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new RuntimeException("Queue not found for ID: " + queueId));

        // Mark queue as completed
        queue.setStatus(Status.COMPLETED);
        queueRepository.save(queue);

        // Retrieve Diagnosis
        Diagnosis diagnosis = diagnosisRepository.findById(diagnosisId)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found for ID: " + diagnosisId));

        // Create and save Prescription
        Prescription newPrescription = new Prescription();
        newPrescription.setDiagnosis(diagnosis); // Use the full Diagnosis entity, not just ID
        newPrescription.setPrescriptionDetails(prescription);
        prescriptionRepository.save(newPrescription);
    }
    public List<PatientQueue> searchPendingPatients(String keyword) {
        return queueRepository.searchPendingPatients(keyword);
    }

}
