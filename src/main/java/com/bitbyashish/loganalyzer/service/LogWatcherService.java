package com.bitbyashish.loganalyzer.service;

import com.bitbyashish.loganalyzer.model.AlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogWatcherService {

    private final KafkaTemplate<String, AlertEvent> kafkaTemplate;

    private static final Pattern logPattern = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\s+(ERROR|INFO|WARN|CRITICAL)\\s+\\[(.*?)\\]\\s+-\\s+(.*)");

    @PostConstruct
    public void startWatching() {
        File logFile = new File("src/main/resources/logs/app.log");

        Tailer tailer = Tailer.builder()
                .setFile(logFile)
                .setTailerListener(new LogTailerListener())
                .setDelayDuration(Duration.ofMillis(1000))
                .setCharset(StandardCharsets.UTF_8)
                .setReOpen(true)
                .setTailFromEnd(true)
                .setBufferSize(4096)
                .get();

        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();

        log.info("Log tailing started for {}", logFile.getAbsolutePath());
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

                if ("ERROR".equalsIgnoreCase(level) || "CRITICAL".equalsIgnoreCase(level)) {
                    AlertEvent event = AlertEvent.builder()
                            .timestamp(timestamp)
                            .level(level)
                            .source(source)
                            .message(message)
                            .build();

                    kafkaTemplate.send("alerts", event);
                    log.info("Published alert to Kafka: {}", event);
                }
            }
        }
    }
}
