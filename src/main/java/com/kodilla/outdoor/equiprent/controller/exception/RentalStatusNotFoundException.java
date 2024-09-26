package com.kodilla.outdoor.equiprent.controller.exception;

import lombok.Getter;

@Getter
public class RentalStatusNotFoundException extends Exception {
    private String status;

    public RentalStatusNotFoundException(String status) {
        super("Rental status " + status + " does not exist.");
        this.status = status;
    }
}
