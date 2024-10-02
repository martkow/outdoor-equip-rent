package mapper;

import com.kodilla.outdoor.equiprent.domain.Report;
import com.kodilla.outdoor.equiprent.dto.ReportDto;
import com.kodilla.outdoor.equiprent.mapper.ReportMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

@DisplayName("Tests for ReportMapper class")
@ExtendWith(MockitoExtension.class)
public class ReportMapperTests {
    @InjectMocks
    private ReportMapper reportMapper;

    @DisplayName("Test case for mapReportToReportDto method")
    @Test
    void shouldReturnReportDto() {
        // Given
        Report report = new Report(1L, 10L, 8L, 2L, LocalDateTime.of(2024, 9, 1, 0, 0), LocalDateTime.of(2024, 9, 30, 23, 59), LocalDateTime.of(2024, 9, 1, 0, 0));
        // When
        ReportDto result = reportMapper.mapReportToReportDto(report);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals(10L, result.getTotalRentals());
        Assertions.assertEquals(8L, result.getTotalReturns());
        Assertions.assertEquals(2L, result.getOverdueRentals());
        Assertions.assertEquals(LocalDateTime.of(2024, 9, 1, 0, 0), result.getReportStartDate());
        Assertions.assertEquals(LocalDateTime.of(2024, 9, 30, 23, 59), result.getReportEndDate());
    }

    @DisplayName("Test case for mapReportListToReportDtoList method")
    @Test
    void shouldReturnReportDtoList() {
        // Given
        Report report = new Report(1L, 10L, 8L, 2L, LocalDateTime.of(2024, 9, 1, 0, 0), LocalDateTime.of(2024, 9, 30, 23, 59), LocalDateTime.of(2024, 9, 1, 0, 0));
        List<Report> reportList = List.of(report);
        // When
        List<ReportDto> result = reportMapper.mapReportListToReportDtoList(reportList);
        // Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(10L, result.get(0).getTotalRentals());
        Assertions.assertEquals(8L, result.get(0).getTotalReturns());
        Assertions.assertEquals(2L, result.get(0).getOverdueRentals());
        Assertions.assertEquals(LocalDateTime.of(2024, 9, 1, 0, 0), result.get(0).getReportStartDate());
        Assertions.assertEquals(LocalDateTime.of(2024, 9, 30, 23, 59), result.get(0).getReportEndDate());
    }
}
