package com.bitbyashish.loganalyzer.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.bitbyashish.loganalyzer.model.AlertEvent;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlertProducerService {

    private final KafkaTemplate<String, AlertEvent> kafkaTemplate;

    public void sendAlert(AlertEvent alertEvent){
        kafkaTemplate.send("alerts", alertEvent);
    }
}
