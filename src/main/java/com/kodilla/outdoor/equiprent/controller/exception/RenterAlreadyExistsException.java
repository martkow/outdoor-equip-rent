package com.kodilla.outdoor.equiprent.controller.exception;

import lombok.Getter;

@Getter
public class RenterAlreadyExistsException extends Exception {
    private String email;

    public RenterAlreadyExistsException(String email) {
        super("Renter with email " + email + " already exists.");
        this.email = email;
    }
}
