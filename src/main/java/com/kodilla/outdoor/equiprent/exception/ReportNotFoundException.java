package com.kodilla.outdoor.equiprent.exception;

import lombok.Getter;

@Getter
public class ReportNotFoundException extends Exception {
    private Long reportId;

    public ReportNotFoundException(Long reportId) {
        super("Report with ID " + reportId + " does not exist.");
        this.reportId = reportId;
    }
}
