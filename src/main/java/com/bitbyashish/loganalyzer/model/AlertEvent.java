package com.bitbyashish.loganalyzer.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertEvent {
    private String level;
    private String source;
    private String message;
    private LocalDateTime timestamp;
}
