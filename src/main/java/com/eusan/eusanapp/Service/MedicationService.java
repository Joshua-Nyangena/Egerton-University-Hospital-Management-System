package com.eusan.eusanapp.Service;

import com.eusan.eusanapp.Entity.Medication;
import com.eusan.eusanapp.Repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    // Get all medications
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }
    public void saveMedication(Medication medication) {
        medicationRepository.save(medication);
    }
    
public void recordDrugDelivery(Long medicationId, int quantityDelivered) {
    Optional<Medication> medicationOpt = medicationRepository.findById(medicationId);
    medicationOpt.ifPresent(medication -> {
        medication.setQuantity(medication.getQuantity() + quantityDelivered);

        // If the drug was flagged for restocking and is now available, remove the flag
        if (medication.getQuantity() > 0) {
            medication.setNeedsRestock(false);
        }

        medicationRepository.save(medication);
    });
}


    // Deduct stock when issuing medication
    public boolean issueMedication(String medicationName) {
        Optional<Medication> medicationOpt = medicationRepository.findByName(medicationName);
        if (medicationOpt.isPresent()) {
            Medication medication = medicationOpt.get();
            if (medication.getQuantity() > 0) {
                medication.setQuantity(medication.getQuantity() - 1);
                
                // If stock is zero, flag it for restocking
                if (medication.getQuantity() == 0) {
                    medication.setNeedsRestock(true);
                }

                medicationRepository.save(medication);
                return true;
            }
        }
        return false;
    }

    // Place a restock request
    public void requestRestock(Long medicationId) {
        Optional<Medication> medicationOpt = medicationRepository.findById(medicationId);
        medicationOpt.ifPresent(medication -> {
            medication.setNeedsRestock(true);
            medicationRepository.save(medication);
        });
    }
    public List<Medication> searchMedications(String keyword) {
        return medicationRepository.searchMedications(keyword);
    }
}
