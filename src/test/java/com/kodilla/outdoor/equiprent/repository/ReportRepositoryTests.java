package com.kodilla.outdoor.equiprent.repository;

import com.kodilla.outdoor.equiprent.domain.Report;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DisplayName("Tests for ReportRepository")
@SpringBootTest
@Transactional
public class ReportRepositoryTests {
    @Autowired
    private ReportRepository reportRepository;

    @DisplayName("Test case for save method")
    @Test
    void shouldSaveReport() {
        // Given
        Report report = new Report(null, 5, 3, 1, LocalDateTime.of(2024, 9, 27, 0, 0, 0), LocalDateTime.of(2024, 9, 28, 0, 0, 0), LocalDateTime.of(2024, 9, 27, 0, 0, 0));
        // When
        Report savedReport = reportRepository.save(report);
        Long savedReportId = savedReport.getId();
        // Then
        Assertions.assertTrue(reportRepository.findById(savedReportId).isPresent());
    }

    @DisplayName("Test case for findByReportStartDateGreaterThanEqual method")
    @Test
    void shouldFindReportsFromDateOnward() {
        // Given
        Report report1 = new Report(null, 5, 3, 1, LocalDateTime.of(2024, 9, 27, 0, 0, 0), LocalDateTime.of(2024, 9, 28, 0, 0, 0), LocalDateTime.of(2024, 9, 27, 0, 0, 0));
        Report report2 = new Report(null, 5, 3, 1, LocalDateTime.of(2024, 9, 25, 0, 0, 0), LocalDateTime.of(2024, 9, 26, 0, 0, 0), LocalDateTime.of(2024, 9, 25, 0, 0, 0));

        reportRepository.save(report1);
        reportRepository.save(report2);
        // When
        List<Report> result = reportRepository.findByReportStartDateGreaterThanEqual(LocalDateTime.of(2024, 9, 26, 0, 0, 0));
        // Then
        Assertions.assertEquals(1, result.size());
    }

    @DisplayName("Test case for findById method")
    @Test
    void shouldFindReportById() {
        // Given
        Report report = new Report(null, 5, 3, 1, LocalDateTime.of(2024, 9, 27, 0, 0, 0), LocalDateTime.of(2024, 9, 28, 0, 0, 0), LocalDateTime.of(2024, 9, 27, 0, 0, 0));
        Report savedReport = reportRepository.save(report);
        Long savedReportId = savedReport.getId();
        // When
        Optional<Report> result = reportRepository.findById(savedReportId);
        // Then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(savedReportId, result.get().getId());
    }
}
