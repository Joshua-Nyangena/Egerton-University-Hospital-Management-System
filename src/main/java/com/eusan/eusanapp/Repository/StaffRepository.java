package com.eusan.eusanapp.Repository;

import com.eusan.eusanapp.Entity.Staff;
import com.eusan.eusanapp.Entity.Diagnosis;
import com.eusan.eusanapp.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByWorkId(String workId);
    List<Staff> findByRole(Role role);
    Optional<Staff> findByWorkEmail(String workEmail); 


    @Query("SELECT s FROM Staff s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    "OR LOWER(s.workId) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
    "OR LOWER(s.role) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Staff> searchByKeyword(String keyword);
    // Custom query to find staff by personal email
    Staff findByPersonalEmail(String personalEmail);
    Staff findByResetToken(String resetToken);
    // List<Staff> findByStaffName( String keyword);
}
