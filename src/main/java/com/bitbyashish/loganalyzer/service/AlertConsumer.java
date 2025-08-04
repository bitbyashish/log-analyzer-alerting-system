package com.bitbyashish.loganalyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bitbyashish.loganalyzer.entity.Alert;
import com.bitbyashish.loganalyzer.model.AlertEvent;
import com.bitbyashish.loganalyzer.repository.AlertRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertConsumer {

    private final AlertRepository alertRepository;
    private final JavaMailSender mailSender;

    @KafkaListener(topics = "alerts", groupId = "logwatcher-alerts", containerFactory = "kafkaListenerContainerFactory")
    public void consumeAlert(AlertEvent event) {
        log.info("Received alert from Kafka: {}", event);

        Alert alert = Alert.builder()
                .timestamp(event.getTimestamp())
                .level(event.getLevel())
                .source(event.getSource())
                .message(event.getMessage())
                .build();

        alertRepository.save(alert);

        if ("ERROR".equalsIgnoreCase(event.getLevel()) || "CRITICAL".equalsIgnoreCase(event.getLevel())) {
            sendEmailNotification(alert);
        }
    }

    private void sendEmailNotification(Alert alert) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("ashish120201@gmail.com"); // replace with actual recipient
            message.setSubject("Log Alert: " + alert.getLevel());
            message.setText("[" + alert.getLevel() + "] " + alert.getTimestamp() +
                    "\nSource: " + alert.getSource() +
                    "\nMessage: " + alert.getMessage());

            mailSender.send(message);
            log.info("Alert email sent for alert: {}", alert.getId());
        } catch (Exception e) {
            log.error("Failed to send email alert", e);
        }
    }
}
