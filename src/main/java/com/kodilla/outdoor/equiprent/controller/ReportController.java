package com.kodilla.outdoor.equiprent.controller;

import com.kodilla.outdoor.equiprent.dto.RenterDto;
import com.kodilla.outdoor.equiprent.dto.ReportDto;
import com.kodilla.outdoor.equiprent.exception.GlobalHttpErrorHandler;
import com.kodilla.outdoor.equiprent.exception.ReportDownloadNotAvailableException;
import com.kodilla.outdoor.equiprent.exception.ReportNotFoundException;
import com.kodilla.outdoor.equiprent.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
                            array = @ArraySchema(schema = @Schema(implementation = ReportDto.class))
                    ))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ReportDto>> getReportsFromDate(
            @Parameter(description = "Date from which reports are fetched (YYYY-MM-DD)", example = "2024-09-27")
            @RequestParam LocalDate fromDate) {
        return ResponseEntity.ok(reportService.getReportsFromDate(fromDate));
    }

    @Operation(
            summary = "Retrieve a report by its ID",
            description = "Fetches the details of a specific report based on its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Report retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportDto.class))),
            @ApiResponse(responseCode = "400", description = "Report not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)))
    })
    @GetMapping(value = "/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ReportDto> getReportById(
            @Parameter(description = "Unique identifier of the report", example = "1")
            @PathVariable Long reportId) throws ReportNotFoundException {
        return ResponseEntity.ok(reportService.getReportById(reportId));
    }

    @Operation(
            summary = "Download a report as a PDF",
            description = "Generates a PDF file of the report based on its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "PDF report downloaded successfully.",
                    content = @Content(
                            mediaType = "application/pdf")),
            @ApiResponse(responseCode = "400", description = "Report not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalHttpErrorHandler.Error.class)))
    })
    @GetMapping(value = "/{reportId}/download", produces = {MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> downloadReportAsPdf(
            @Parameter(description = "Unique identifier of the report", example = "1")
            @PathVariable Long reportId) throws ReportNotFoundException, ReportDownloadNotAvailableException {
        try {
            byte[] pdfContent = reportService.getReportAsByteArray(reportId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "report_" + reportId + ".pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (ReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new GlobalHttpErrorHandler.Error("ERROR", "report.does.not.exist", "Report with ID " + e.getReportId() + " not found."));
        }
    }
}
