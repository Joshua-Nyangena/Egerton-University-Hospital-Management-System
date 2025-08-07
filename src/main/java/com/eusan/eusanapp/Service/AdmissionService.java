package com.eusan.eusanapp.Service;

import com.eusan.eusanapp.Entity.Admissions;
import com.eusan.eusanapp.Entity.Status;
import com.eusan.eusanapp.Repository.AdmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmissionService {

    @Autowired
    private AdmissionRepository admissionRepository;

    public void save(Admissions admission) {
        admissionRepository.save(admission);
    }
    public List<Admissions> getAdmittedPatients() {
        return admissionRepository.findByPatientQueueStatus(Status.ADMITTED);
    }
    public List<Admissions> getAdmittedPatientsBySpecification(String specification) {
        return admissionRepository.findByPatientQueue_StatusAndPatientQueue_Specification(Status.ADMITTED, specification);
    }
}
