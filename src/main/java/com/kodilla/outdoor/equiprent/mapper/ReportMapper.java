package com.kodilla.outdoor.equiprent.mapper;

import com.kodilla.outdoor.equiprent.domain.Renter;
import com.kodilla.outdoor.equiprent.domain.Report;
import com.kodilla.outdoor.equiprent.dto.RenterDto;
import com.kodilla.outdoor.equiprent.dto.ReportDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportMapper {
    public ReportDto mapReportToReportDto(Report report) {
        return ReportDto.builder()
                .id(report.getId())
                .totalRentals(report.getTotalRentals())
                .totalReturns(report.getTotalReturns())
                .overdueRentals(report.getOverdueRentals())
                .reportStartDate(report.getReportStartDate())
                .reportEndDate(report.getReportEndDate())
                .build();
    }

    public List<ReportDto> mapReportListToReportDtoList(List<Report> reports) {
        return reports.stream()
                .map(this::mapReportToReportDto)
                .toList();
    }
}
