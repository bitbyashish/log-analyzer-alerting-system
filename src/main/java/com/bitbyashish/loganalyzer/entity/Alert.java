package com.bitbyashish.loganalyzer.entity;

import com.bitbyashish.loganalyzer.model.LogEntry;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
