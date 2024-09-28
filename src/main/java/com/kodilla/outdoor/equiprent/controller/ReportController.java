package com.kodilla.outdoor.equiprent.controller;

import com.kodilla.outdoor.equiprent.dto.ReportDto;
import com.kodilla.outdoor.equiprent.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@Tag(name = "Report", description = "Managing reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @Operation(
            summary = "Retrieve reports",
            description = "Fetches reports from a specific date onward"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Reports retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportDto.class))),
    })
    @GetMapping
    public ResponseEntity<List<ReportDto>> getReportsFromDate(
            @Parameter(description = "Date from which reports are fetched (YYYY-MM-DD)", example = "2024-09-27")
            @RequestParam LocalDate fromDate) {
        return ResponseEntity.ok(reportService.getReportsFromDate(fromDate));
    }
}
