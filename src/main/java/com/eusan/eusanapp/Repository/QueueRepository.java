package com.eusan.eusanapp.Repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Queue;
import java.time.LocalDateTime;

import com.eusan.eusanapp.Entity.Status;

import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Entity.LabResult;

@Repository
public interface QueueRepository extends JpaRepository<PatientQueue, String> {
    List<PatientQueue> findByStatus(Status status);
    // Custom Query to fetch only PENDING and TRIAGED patients
    // Modified query to filter by status and specification
    List<PatientQueue> findByStatusInAndSpecification(List<Status> statuses, String specification);

    List<PatientQueue> findAll(Sort sort);
    @Query("SELECT q FROM PatientQueue q WHERE q.status = 'PENDING' AND " +
           "(LOWER(q.patient.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(q.patient.regNo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(q.patient.contact) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<PatientQueue> searchPendingPatients(String keyword); 

    List<PatientQueue> findByPatient_NameContainingIgnoreCaseOrPatient_RegNoContainingIgnoreCase(String name, String regNo);

List<PatientQueue> findByStatusIn(List<Status> statuses);

@Query("SELECT pq FROM PatientQueue pq WHERE pq.status IN :statuses AND " +
       "(LOWER(pq.patient.name) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
       "LOWER(pq.patient.regNo) LIKE LOWER(CONCAT('%', :regNo, '%')))")
List<PatientQueue> findByStatusInAndPatient_NameContainingIgnoreCaseOrPatient_RegNoContainingIgnoreCase(
    @Param("statuses") List<Status> statuses,
    @Param("name") String name,
    @Param("regNo") String regNo
);

@Query("SELECT q FROM PatientQueue q " +
       "WHERE LOWER(q.patient.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
       "OR LOWER(q.patient.regNo) LIKE LOWER(CONCAT('%', :keyword, '%'))")
List<PatientQueue> searchAllQueuedPatients(@Param("keyword") String keyword);

@Query("SELECT q FROM PatientQueue q " +
       "WHERE (q.status = com.eusan.eusanapp.Entity.Status.PENDING OR q.status = com.eusan.eusanapp.Entity.Status.TRIAGED) " +
       "AND (LOWER(q.patient.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
       "OR LOWER(q.patient.regNo) LIKE LOWER(CONCAT('%', :keyword, '%')))")
List<PatientQueue> searchPendingAndTriagedPatients(@Param("keyword") String keyword);
List<PatientQueue> findByDateBetween(LocalDateTime start, LocalDateTime end);
}
