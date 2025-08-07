package com.eusan.eusanapp.Repository;

import com.eusan.eusanapp.Entity.Admissions;
import com.eusan.eusanapp.Entity.Status;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdmissionRepository extends JpaRepository<Admissions, String> {
    List<Admissions> findByPatientQueueStatus(Status status);
    List<Admissions> findByPatientQueue_StatusAndPatientQueue_Specification(Status status, String specification);

}
