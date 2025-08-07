package com.eusan.eusanapp.Repository;

import com.eusan.eusanapp.Entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {
    Optional<Medication> findByName(String name);
    @Query("SELECT m FROM Medication m WHERE " +
           "LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.supplier) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "CAST(m.id AS string) LIKE CONCAT('%', :keyword, '%')")
    List<Medication> searchMedications(String keyword);
}
