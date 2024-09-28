package com.kodilla.outdoor.equiprent.scheduler;

import com.kodilla.outdoor.equiprent.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReportScheduler {
    private final ReportService reportService;

    @Scheduled(cron = "1 0 0 * * *")
    public void generateDailyReport() {
        reportService.generateDailyReport();
    }
}
