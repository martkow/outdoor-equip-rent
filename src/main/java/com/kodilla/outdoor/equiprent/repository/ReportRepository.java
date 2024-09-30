package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.Report;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportRepository extends CrudRepository<Report, Long> {
    @Override
    Report save(Report report);
    List<Report> findByReportStartDateGreaterThanEqual(LocalDateTime dateTime);
    @Override
    Optional<Report> findById(Long id);
}
