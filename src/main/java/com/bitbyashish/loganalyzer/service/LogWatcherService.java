package com.bitbyashish.loganalyzer.service;

import com.bitbyashish.loganalyzer.entity.Alert;
import com.bitbyashish.loganalyzer.model.LogEntry;
import com.bitbyashish.loganalyzer.repository.AlertRepository;
import com.bitbyashish.loganalyzer.repository.LogEntryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.regex.*;

@Service
@RequiredArgsConstructor
public class LogWatcherService {

    private final LogEntryRepository logEntryRepository;
    private final AlertRepository alertRepository;
    private final AlertService alertService;

    private static final Pattern logPattern = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+(ERROR|INFO|WARN)\\s+\\[(.*?)\\]\\s+-\\s+(.*)");

    @PostConstruct
    public void startWatching() {
        File logFile = new File("src/main/resources/logs/app.log");

        TailerListener listener = new LogTailerListener();

        Tailer tailer = Tailer.builder()
                .setFile(logFile)
                .setTailerListener(listener)
                .setDelayDuration(Duration.ofMillis(1000))
                .setCharset(StandardCharsets.UTF_8)
                .setReOpen(true)
                .setTailFromEnd(true)
                .setBufferSize(4096)
                .get();

        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
    }

    private class LogTailerListener extends TailerListenerAdapter {
        @Override
        public void handle(String line) {
            Matcher matcher = logPattern.matcher(line);
            if (matcher.matches()) {
                LocalDateTime timestamp = LocalDateTime.parse(matcher.group(1),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String level = matcher.group(2);
                String source = matcher.group(3);
                String message = matcher.group(4);

                // Save log entry
                LogEntry entry = LogEntry.builder()
                        .timestamp(timestamp)
                        .level(level)
                        .source(source)
                        .message(message)
                        .build();
                logEntryRepository.save(entry);

                // Raise alert for certain levels
                if ("ERROR".equalsIgnoreCase(level) || "CRITICAL".equalsIgnoreCase(level)) {
                    alertService.raiseAlert(entry);
                }

                // 🔔 Save alert if level is ERROR
                if ("ERROR".equalsIgnoreCase(level)) {
                    Alert alert = Alert.builder()
                            .timestamp(timestamp)
                            .level(level)
                            .source(source)
                            .message(message)
                            .build();
                    alertRepository.save(alert);
                }
            }
        }
    }
}

