package com.kodilla.outdoor.equiprent.controller;

import com.kodilla.outdoor.equiprent.dto.ReportDto;
import com.kodilla.outdoor.equiprent.exception.ReportDownloadNotAvailableException;
import com.kodilla.outdoor.equiprent.exception.ReportNotFoundException;
import com.kodilla.outdoor.equiprent.service.ReportService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

@DisplayName("Tests for ReportController")
@SpringJUnitWebConfig
@WebMvcTest(ReportController.class)
public class ReportControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReportService reportService;

    @Test
    @DisplayName("Test case for fetching reports from a specific date onward")
    void shouldFindReportsFromDateOnward() throws Exception {
        // Given
        ReportDto reportDto = new ReportDto(1L, 5, 3, 1, LocalDateTime.of(2024, 9, 27, 0, 0, 0), LocalDateTime.of(2024, 9, 28, 0, 0, 0));

        Mockito.when(reportService.getReportsFromDate(Mockito.any())).thenReturn(List.of(reportDto));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reports?fromDate=2024-09-27"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalRentals", Matchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalReturns", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].overdueRentals", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reportStartDate", Matchers.is("2024-09-27T00:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reportEndDate", Matchers.is("2024-09-28T00:00:00")));
    }

    @Test
    @DisplayName("Test case for fetching a specific report by its id")
    void shouldCreateRenter() throws Exception {
        // Given
        ReportDto reportDto = new ReportDto(1L, 5, 3, 1, LocalDateTime.of(2024, 9, 27, 0, 0, 0), LocalDateTime.of(2024, 9, 28, 0, 0, 0));

        Mockito.when(reportService.getReportById(Mockito.anyLong())).thenReturn(reportDto);
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reports/1"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalRentals", Matchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalReturns", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.overdueRentals", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reportStartDate", Matchers.is("2024-09-27T00:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.reportEndDate", Matchers.is("2024-09-28T00:00:00")));
    }

    @Test
    @DisplayName("Test case for ReportNotFoundException when fetching report which does not exist")
    void shouldHandleReportNotFoundException() throws Exception {
        // Given
        Mockito.when(reportService.getReportById(Mockito.anyLong())).thenThrow(new ReportNotFoundException(1L));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reports/1"))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("report.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Report with ID 1 not found.")));
    }

    @Test
    @DisplayName("Test case for downloading a report as a PDF")
    void shouldDownloadReportAsPdf() throws Exception {
        // Given
        byte[] pdfContent = "Sample PDF content".getBytes();
        Mockito.when(reportService.getReportAsByteArray(Mockito.anyLong())).thenReturn(pdfContent);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reports/1/download"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE))
                .andExpect(MockMvcResultMatchers.header().string(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=\"attachment\"; filename=\"report_1.pdf\""))
                .andExpect(MockMvcResultMatchers.content().bytes(pdfContent));
    }

    @Test
    @DisplayName("Test case for handling ReportNotFoundException when downloading PDF")
    void shouldHandleReportNotFoundExceptionWhenDownloadingPdf() throws Exception {
        // Given
        Mockito.when(reportService.getReportAsByteArray(Mockito.anyLong())).thenThrow(new ReportNotFoundException(1L));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reports/1/download"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("report.does.not.exist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Report with ID 1 not found.")));
    }

    @Test
    @DisplayName("Test case for handling ReportDownloadNotAvailableException when downloading PDF")
    void shouldHandleReportDownloadNotAvailableExceptionWhenDownloadingPdf() throws Exception {
        // Given
        Mockito.when(reportService.getReportAsByteArray(Mockito.anyLong())).thenThrow(new ReportDownloadNotAvailableException(1L));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reports/1/download"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.level", Matchers.is("ERROR")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code", Matchers.is("report.download.not.available")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.is("Report with ID 1 currently not available to download.")));
    }
}
