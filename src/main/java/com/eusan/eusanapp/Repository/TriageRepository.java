package com.eusan.eusanapp.Repository;

import com.eusan.eusanapp.Entity.Triage;
import com.eusan.eusanapp.Entity.PatientQueue;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriageRepository extends JpaRepository<Triage, Long> {
    Triage findByQueueQueueId(String queueId);
    Triage findByQueue_QueueId(String queueId);



}
