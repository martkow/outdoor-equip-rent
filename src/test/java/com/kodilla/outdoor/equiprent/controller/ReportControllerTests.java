package com.kodilla.outdoor.equiprent.controller;

import com.kodilla.outdoor.equiprent.domain.Report;
import com.kodilla.outdoor.equiprent.dto.ReportDto;
import com.kodilla.outdoor.equiprent.service.ReportService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    void shouldCreateRenter() throws Exception {
        // Given
        ReportDto reportDto = new ReportDto(1L, 5, 3, 1, LocalDateTime.of(2024, 9, 27, 0, 0, 0), LocalDateTime.of(2024, 9, 28, 0, 0, 0));

        Mockito.when(reportService.getReportsFromDate(Mockito.any())).thenReturn(List.of(reportDto));
        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/reports?fromDate=2024-09-27")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalRentals", Matchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalReturns", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].overdueRentals", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reportStartDate", Matchers.is("2024-09-27T00:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reportEndDate", Matchers.is("2024-09-28T00:00:00")));
    }
}
