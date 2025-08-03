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

    public void raiseAlert(LogEntry logEntry) {
        Alert alert = Alert.builder()
                .level(logEntry.getLevel())
                .message(logEntry.getMessage())
                .source(logEntry.getSource())
                .timestamp(logEntry.getTimestamp())
                .logEntry(logEntry)
                .build();

        alertRepository.save(alert);
    }
}
