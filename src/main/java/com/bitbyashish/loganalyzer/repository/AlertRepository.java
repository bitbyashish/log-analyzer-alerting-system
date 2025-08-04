package com.bitbyashish.loganalyzer.repository;

import com.bitbyashish.loganalyzer.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
}
