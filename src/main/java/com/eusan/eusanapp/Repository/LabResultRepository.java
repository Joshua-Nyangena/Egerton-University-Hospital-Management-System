package com.eusan.eusanapp.Repository;

import com.eusan.eusanapp.Entity.Diagnosis;
import com.eusan.eusanapp.Entity.LabResult;
import com.eusan.eusanapp.Entity.Status;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabResultRepository extends JpaRepository<LabResult, Long> {
    List<LabResult> findByDiagnosis_PatientQueue_Patient_Id(Long patientId);
    @Query("SELECT lr FROM LabResult lr " +
    "JOIN lr.diagnosis d " +
    "JOIN d.patientQueue q " +
    "WHERE q.status = :status")
    List<LabResult> findLabResultsByQueueStatus(@Param("status") Status status);
    List<LabResult> findAll(Sort sort);

    @Query("SELECT lr FROM LabResult lr " +
           "JOIN lr.diagnosis d " +
           "JOIN d.patientQueue q " +
           "WHERE q.status = :status AND q.specification = 'General Consultation'")
    List<LabResult> findLabResultsForGeneralConsultation(@Param("status") Status status);

    // Fetch lab results for VCT
    @Query("SELECT lr FROM LabResult lr " +
           "JOIN lr.diagnosis d " +
           "JOIN d.patientQueue q " +
           "WHERE q.status = :status AND q.specification = 'VCT'")
    List<LabResult> findLabResultsForVCT(@Param("status") Status status);

    // Fetch lab results for Dentist
    @Query("SELECT lr FROM LabResult lr " +
           "JOIN lr.diagnosis d " +
           "JOIN d.patientQueue q " +
           "WHERE q.status = :status AND q.specification = 'Dentist'")
    List<LabResult> findLabResultsForDentist(@Param("status") Status status);

    // Fetch lab results for CMO
    @Query("SELECT lr FROM LabResult lr " +
           "JOIN lr.diagnosis d " +
           "JOIN d.patientQueue q " +
           "WHERE q.status = :status AND q.specification = 'CMO'")
    List<LabResult> findLabResultsForCMO(@Param("status") Status status);


    // Fetch lab results for a specific patient
    @Query("SELECT lr FROM LabResult lr WHERE lr.diagnosis.patientQueue.patient.id = :patientId")
    List<LabResult> findByPatientId(@Param("patientId") Long patientId);
    
    @Query("SELECT lr FROM LabResult lr JOIN lr.diagnosis.patientQueue pq JOIN pq.patient p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<LabResult> findByPatientName(@Param("keyword") String keyword);

}
