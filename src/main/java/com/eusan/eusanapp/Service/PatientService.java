package com.eusan.eusanapp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eusan.eusanapp.Entity.Patient;
import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Repository.PatientRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getActivePatients() {
        return patientRepository.findByActiveTrue();  // Fetch only active patients
    }

    public String savePatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return "Patient saved successfully!";
        } catch (DataIntegrityViolationException e) {
            return "Error: Contact or Registration Number already exists!";
        }
    }

 
 @Transactional
public void deactivatePatient(Long id) {
    Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Patient with ID " + id + " not found."));
    patient.setActive(false);  // Ensure 'active' field exists in Patient entity
    patientRepository.save(patient);
}
// ✅ Get all patients
public List<Patient> getAllPatients() {
    return patientRepository.findAll();
}


// ✅ Get patients sorted by date (Assuming 'registrationDate' exists in Patient entity)
public List<Patient> getPatientsSortedByDate() {
    return patientRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
}


    

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id); 
    }

    public List<Patient> searchActivePatients(String keyword) {
        return patientRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword);
    }
    public List<Patient> searchAllPatients(String keyword) {
        return patientRepository.searchAllPatients(keyword);
    }
}
