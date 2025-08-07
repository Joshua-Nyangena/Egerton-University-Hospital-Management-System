package com.eusan.eusanapp.Repository;

import com.eusan.eusanapp.Entity.Prescription;
import com.eusan.eusanapp.Entity.Status;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    @Query("SELECT p FROM Prescription p " +
       "JOIN p.diagnosis d " +
       "JOIN d.patientQueue q " +
       "WHERE q.status = :status")
List<Prescription> findPrescriptionsByQueueStatus(@Param("status") Status status);
List<Prescription> findAll(Sort sort);

    List<Prescription> findByDiagnosis_PatientQueue_Patient_Id(Long patientId);

        @Query("SELECT p FROM Prescription p WHERE LOWER(p.diagnosis.patientQueue.patient.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
               "OR LOWER(p.diagnosis.patientQueue.patient.regNo) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        List<Prescription> searchPrescriptionsByPatientNameOrId(@Param("keyword") String keyword);
    }
    

