package com.kodilla.outdoor.equiprent.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class ReportDto {
    @Schema(description = "Unique identifier of the report", example = "1")
    private Long id;
    @Schema(description = "Total number of rentals during the report period", example = "15", required = true)
    private long totalRentals;
    @Schema(description = "Total number of returns during the report period", example = "10", required = true)
    private long totalReturns;
    @Schema(description = "Number of overdue rentals", example = "2", required = true)
    private long overdueRentals;
    @Schema(description = "Start of the report period", example = "2024-09-27T00:00:00", required = true)
    private LocalDateTime reportStartDate;
    @Schema(description = "End of the report period", example = "2024-09-27T23:59:59", required = true)
    private LocalDateTime reportEndDate;
}
