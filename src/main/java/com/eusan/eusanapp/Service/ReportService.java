package com.eusan.eusanapp.Service; 

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eusan.eusanapp.Entity.PatientQueue;
import com.eusan.eusanapp.Repository.QueueRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportService {
    
    @Autowired
    private QueueRepository queueRepository;
    
    public byte[] exportPatientVisitSummary(LocalDateTime startDate, LocalDateTime endDate) throws JRException {
        List<PatientQueue> queues = queueRepository.findByDateBetween(startDate, endDate);
        
        // Convert LocalDateTime to String
        List<PatientQueue> updatedQueues = queues.stream().map(queue -> {
            queue.setDateFormatted(queue.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            return queue;
        }).collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(updatedQueues);
        
        Map<String, Object> params = new HashMap<>();
        params.put("createdBy", "Egerton Sanitorium System");

        JasperReport compiledReport = JasperCompileManager
                .compileReport(getClass().getResourceAsStream("/reports/patientQueueReport.jrxml"));
        
        JasperPrint print = JasperFillManager.fillReport(compiledReport, params, dataSource);
        
        return JasperExportManager.exportReportToPdf(print);
    }
}
