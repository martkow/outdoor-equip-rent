package com.kodilla.outdoor.equiprent.exception;

import lombok.Getter;

@Getter
public class RentalNotFoundException extends Exception {
    private Long rentalId;

    public RentalNotFoundException(Long rentalId) {
        super("Rental with ID " + rentalId + " does not exist.");
        this.rentalId = rentalId;
    }
}
