package com.bitbyashish.loganalyzer.service;

import com.bitbyashish.loganalyzer.entity.Alert;
import com.bitbyashish.loganalyzer.model.LogEntry;
import com.bitbyashish.loganalyzer.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final EmailService emailService;

    private static final String TO_EMAIL = "58a8c90c9d-d80100+1@inbox.mailtrap.io";

    public void raiseAlert(LogEntry logEntry) {
        Alert alert = Alert.builder()
                .level(logEntry.getLevel())
                .message(logEntry.getMessage())
                .source(logEntry.getSource())
                .timestamp(logEntry.getTimestamp())
                .logEntry(logEntry)
                .build();

        alertRepository.save(alert);

        // Send alert email
        String subject = "🚨 Log Alert: " + logEntry.getLevel();
        String content = "Timestamp: " + logEntry.getTimestamp() + "\n"
                + "Source: " + logEntry.getSource() + "\n"
                + "Message: " + logEntry.getMessage();

        emailService.sendAlertEmail(TO_EMAIL, subject, content);
    }
}
