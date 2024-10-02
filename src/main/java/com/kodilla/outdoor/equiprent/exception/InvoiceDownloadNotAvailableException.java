package com.kodilla.outdoor.equiprent.exception;

import lombok.Getter;

@Getter
public class InvoiceDownloadNotAvailableException extends Exception {
    private Long rentalId;

    public InvoiceDownloadNotAvailableException(Long rentalId) {
        super("Invoice for rental with ID " + rentalId + " currently not available to download.");
        this.rentalId = rentalId;
    }
}
