package com.eusan.eusanapp.Service;

import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Entity.Triage;
import com.eusan.eusanapp.Repository.QueueRepository;
import com.eusan.eusanapp.Repository.TriageRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import com.eusan.eusanapp.Entity.Status;

@Service
public class TriageService {

    private final TriageRepository triageRepository;
    private final QueueRepository queueRepository;

    public TriageService(TriageRepository triageRepository, QueueRepository queueRepository) {
        this.triageRepository = triageRepository;
        this.queueRepository = queueRepository;
    }

    public void saveTriageData(Triage triage, String queueId) {
        // Ensure the queue entry exists
        PatientQueue queue = queueRepository.findById(queueId)
                .orElseThrow(() -> new RuntimeException("Queue not found for ID: " + queueId));

        // Associate queue with triage
        triage.setQueue(queue);
        triageRepository.save(triage);

        // Update queue status
        queue.setStatus(Status.TRIAGED);
        queueRepository.save(queue);
    }

    public PatientQueue getQueueById(String queueId) {
        return queueRepository.findById(queueId)
                .orElseThrow(() -> new RuntimeException("Queue not found for ID: " + queueId));
    }

    public Triage getTriageByQueueId(String queueId) {
        return triageRepository.findByQueueQueueId(queueId);
    }
    
}
