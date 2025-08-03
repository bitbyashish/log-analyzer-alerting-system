package com.bitbyashish.loganalyzer.repository;

import com.bitbyashish.loganalyzer.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    @Query("""
        SELECT l FROM LogEntry l
        WHERE (:level IS NULL OR l.level = :level)
        AND (:source IS NULL OR l.source = :source)
        AND (:start IS NULL OR l.timestamp >= :start)
        AND (:end IS NULL OR l.timestamp <= :end)
    """)
    List<LogEntry> findFilteredLogs(
            @Param("level") String level,
            @Param("source") String source,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
