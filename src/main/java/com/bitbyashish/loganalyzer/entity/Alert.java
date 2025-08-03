package com.bitbyashish.loganalyzer.entity;

import com.bitbyashish.loganalyzer.model.LogEntry;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String level;

    @Column(length = 1000)
    private String message;

    private String source;

    private LocalDateTime timestamp;

    @ManyToOne
    private LogEntry logEntry;

    // Constructors
    public Alert() {}

    public Alert(String level, String message, String source, LocalDateTime timestamp) {
        this.level = level;
        this.message = message;
        this.source = source;
        this.timestamp = timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
