package com.bitbyashish.loganalyzer.controller;

import com.bitbyashish.loganalyzer.model.LogEntry;
import com.bitbyashish.loganalyzer.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private final LogEntryRepository logEntryRepository;

    @GetMapping
    public List<LogEntry> getAllLogs() {
        return logEntryRepository.findAll();
    }
}
