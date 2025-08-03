package com.bitbyashish.loganalyzer.controller;

import com.bitbyashish.loganalyzer.model.LogEntry;
import com.bitbyashish.loganalyzer.repository.LogEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/logs")
@RequiredArgsConstructor
public class LogController {

    private LogEntryRepository logEntryRepository;

    @GetMapping
    public List<LogEntry> getAllLogs() {
        return logEntryRepository.findAll();
    }

    @GetMapping
    public List<LogEntry> getFilteredLog(@RequestParam(required = false) String level,
                                         @RequestParam(required = false) String source,
                                         @RequestParam(required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                         @RequestParam(required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
        return logEntryRepository.findFilteredLogs(level, source, start, end);
    }
}
