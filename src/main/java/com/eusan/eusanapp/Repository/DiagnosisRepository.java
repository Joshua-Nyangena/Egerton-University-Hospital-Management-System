package com.eusan.eusanapp.Repository;

import com.eusan.eusanapp.Entity.Diagnosis;
import com.eusan.eusanapp.Entity.LabResult;
import com.eusan.eusanapp.Entity.Status;
import com.eusan.eusanapp.Entity.Prescription;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, String> {
    @Query("SELECT d FROM Diagnosis d " +
           "JOIN d.patientQueue pq " +
           "JOIN pq.patient p " +
           "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Diagnosis> findByPatientName(@Param("keyword") String keyword);
    @Query("SELECT d FROM Diagnosis d " +
           "JOIN d.patientQueue pq " +
           "JOIN pq.patient p " +
           "WHERE p.name LIKE %:keyword% AND pq.status = :status")
    List<Diagnosis> searchPendingTestsByPatientName(@Param("keyword") String keyword, @Param("status") Status status);

    @Query("SELECT d FROM Diagnosis d " +
           "JOIN d.patientQueue pq " +
           "JOIN pq.patient p " +
           "WHERE p.name LIKE %:keyword% ORDER BY d.date ASC")
    List<Diagnosis> searchTestsByPatientNameSortedByDate(@Param("keyword") String keyword);

    @Query("SELECT d FROM Diagnosis d " +
           "JOIN d.patientQueue pq " +
           "JOIN pq.patient p " +
           "WHERE p.name LIKE %:keyword%")
    List<Diagnosis> searchAllTestsByPatientName(@Param("keyword") String keyword);


    Optional<Diagnosis> findByPatientQueue_QueueId(String queueId);
    List<Diagnosis> findByCompletedFalse();
    List<Diagnosis> findAll(Sort sort);
    List<Diagnosis> findAllByOrderByDateAsc();
}
