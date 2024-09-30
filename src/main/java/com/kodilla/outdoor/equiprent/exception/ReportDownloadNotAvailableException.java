package com.kodilla.outdoor.equiprent.exception;

import lombok.Getter;

@Getter
public class ReportDownloadNotAvailableException extends Exception {
    private Long reportId;

    public ReportDownloadNotAvailableException(Long reportId) {
        super("Report with ID " + reportId + " currently not available to download.");
        this.reportId = reportId;
    }
}
