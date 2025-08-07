package com.eusan.eusanapp.Repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eusan.eusanapp.Entity.Patient;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    List<Patient> findByActiveTrue();
    List<Patient> findAll(Sort sort);
    @Query("SELECT p FROM Patient p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.regNo) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Patient> searchAllPatients(String keyword);
}
