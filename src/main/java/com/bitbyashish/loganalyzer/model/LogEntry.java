package com.bitbyashish.loganalyzer.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String level; // e.g., ERROR, INFO, WARN

    private String source; // e.g., AuthService, UserService

    @Column(length = 1000)
    private String message;
}