package com.kodilla.outdoor.equiprent.controller.exception;

import lombok.Getter;

@Getter
public class RenterNotFoundException extends Exception {
    private Long renterId;

    public RenterNotFoundException(Long renterId) {
        super("Renter with ID " + renterId + " does not exist.");
        this.renterId = renterId;
    }
}
