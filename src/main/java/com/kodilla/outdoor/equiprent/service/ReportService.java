package com.kodilla.outdoor.equiprent.service;

import com.kodilla.outdoor.equiprent.domain.RentalStatus;
import com.kodilla.outdoor.equiprent.domain.Report;
import com.kodilla.outdoor.equiprent.dto.ReportDto;
import com.kodilla.outdoor.equiprent.mapper.ReportMapper;
import com.kodilla.outdoor.equiprent.repository.RentalRepository;
import com.kodilla.outdoor.equiprent.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final RentalRepository rentalRepository;
    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public ReportDto generateDailyReport() {
        LocalDateTime startOfDay = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        long totalRentals = rentalRepository.countByRentalStartBetween(startOfDay, endOfDay);
        long totalReturns = rentalRepository.countByReturnDateBetween(startOfDay, endOfDay);
        long overdueRentals = rentalRepository.countByStatusAndRentalEndBefore(RentalStatus.ACTIVE, LocalDateTime.now());

        Report report = Report.builder()
                        .totalRentals(totalRentals)
                        .totalReturns(totalReturns)
                        .overdueRentals(overdueRentals)
                        .reportStartDate(startOfDay)
                        .reportEndDate(endOfDay)
                        .creationDate(LocalDateTime.now())
                        .build();

        return reportMapper.mapReportToReportDto(reportRepository.save(report));
    }

    public List<ReportDto> getReportsFromDate(LocalDate date) {
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(0 ,0 ,0));

        return reportMapper.mapReportListToReportDtoList(reportRepository.findByReportStartDateGreaterThanEqual(dateTime));
    }
}
